import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import airhacks.zsmith.http.boundary.AgentHttpServer;
import airhacks.zsmith.http.boundary.ChatEngine;

Duration timeout = Duration.ofMillis(100);
HttpClient client = HttpClient.newHttpClient();
ConcurrentMap<String, Throwable> failures = new ConcurrentHashMap<>();

void main() {
    var tests = List.<Runnable>of(
        this::chatEchoesBodyAndGeneratesSessionId,
        this::chatReusesClientProvidedSessionId,
        this::actWithEmptyBodyDefaultsToGo,
        this::actWithBodyPassesBodyAsMessage,
        this::getOnChatReturns405,
        this::unknownPathReturns404,
        this::differentSessionsAreIndependent);

    try (var ignored = this.client) {
        tests.parallelStream().forEach(this::run);
    }

    if (!this.failures.isEmpty()) {
        this.failures.values().forEach(Throwable::printStackTrace);
        throw new AssertionError(this.failures.size() + " of " + tests.size() + " tests failed");
    }
    IO.println("AgentHttpServerTest: all " + tests.size() + " tests passed");
}

void run(Runnable test) {
    try {
        test.run();
    } catch (Throwable failure) {
        this.failures.put(test.toString(), failure);
    }
}

void chatEchoesBodyAndGeneratesSessionId() {
    var calls = new ConcurrentHashMap<String, String>();
    ChatEngine engine = (sessionId, message) -> {
        calls.put(sessionId, message);
        return "echo:" + message;
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var response = post(server, "/chat", null, "hello");
        assert response.statusCode() == 200 : "expected 200 but got " + response.statusCode();
        assert "echo:hello".equals(response.body()) : "expected 'echo:hello' but got: " + response.body();
        var sessionId = response.headers().firstValue("X-Session-Id").orElse(null);
        assert sessionId != null && !sessionId.isBlank() : "expected server-generated X-Session-Id header";
        assert "hello".equals(calls.get(sessionId)) : "stub should have been called with session " + sessionId;
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void chatReusesClientProvidedSessionId() {
    var calls = new ConcurrentHashMap<String, String>();
    ChatEngine engine = (sessionId, message) -> {
        calls.put(sessionId, message);
        return "ok";
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var response = post(server, "/chat", "client-session-42", "question");
        assert response.statusCode() == 200 : "expected 200 but got " + response.statusCode();
        var returnedId = response.headers().firstValue("X-Session-Id").orElse(null);
        assert "client-session-42".equals(returnedId) : "expected echoed session id but got: " + returnedId;
        assert "question".equals(calls.get("client-session-42")) : "stub should see message under client session id";
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void actWithEmptyBodyDefaultsToGo() {
    var seen = new java.util.concurrent.atomic.AtomicReference<String>();
    ChatEngine engine = (sessionId, message) -> {
        seen.set(message);
        return "done";
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var response = post(server, "/act", null, "");
        assert response.statusCode() == 200 : "expected 200 but got " + response.statusCode();
        assert "done".equals(response.body()) : "expected 'done' but got: " + response.body();
        assert "go".equals(seen.get()) : "expected stub to receive 'go' seed but got: " + seen.get();
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void actWithBodyPassesBodyAsMessage() {
    var seen = new java.util.concurrent.atomic.AtomicReference<String>();
    ChatEngine engine = (sessionId, message) -> {
        seen.set(message);
        return "ran";
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var response = post(server, "/act", null, "kickoff");
        assert response.statusCode() == 200 : "expected 200 but got " + response.statusCode();
        assert "kickoff".equals(seen.get()) : "expected stub to receive 'kickoff' but got: " + seen.get();
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void getOnChatReturns405() {
    var invocations = new AtomicInteger();
    ChatEngine engine = (sessionId, message) -> {
        invocations.incrementAndGet();
        return "should not be called";
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var request = HttpRequest.newBuilder(uri(server, "/chat"))
                .timeout(timeout)
                .GET()
                .build();
        var response = this.client.send(request, BodyHandlers.ofString());
        assert response.statusCode() == 405 : "expected 405 but got " + response.statusCode();
        assert invocations.get() == 0 : "engine should not have been invoked for rejected method";
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void unknownPathReturns404() {
    ChatEngine engine = (sessionId, message) -> "noop";
    var server = AgentHttpServer.start(engine, 0);
    try {
        var response = post(server, "/unknown-path", null, "body");
        assert response.statusCode() == 404 : "expected 404 but got " + response.statusCode();
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

void differentSessionsAreIndependent() {
    ConcurrentMap<String, Integer> perSessionCount = new ConcurrentHashMap<>();
    ChatEngine engine = (sessionId, message) -> {
        perSessionCount.merge(sessionId, 1, Integer::sum);
        return sessionId + ":" + message;
    };
    var server = AgentHttpServer.start(engine, 0);
    try {
        var a1 = post(server, "/chat", "alpha", "first");
        var a2 = post(server, "/chat", "alpha", "second");
        var b1 = post(server, "/chat", "beta", "solo");

        assert a1.statusCode() == 200 && a2.statusCode() == 200 && b1.statusCode() == 200 : "all posts should return 200";
        assert "alpha:first".equals(a1.body()) : "expected 'alpha:first' but got: " + a1.body();
        assert "alpha:second".equals(a2.body()) : "expected 'alpha:second' but got: " + a2.body();
        assert "beta:solo".equals(b1.body()) : "expected 'beta:solo' but got: " + b1.body();
        assert Integer.valueOf(2).equals(perSessionCount.get("alpha")) : "alpha should have 2 calls, got " + perSessionCount.get("alpha");
        assert Integer.valueOf(1).equals(perSessionCount.get("beta")) : "beta should have 1 call, got " + perSessionCount.get("beta");
    }catch(Exception ex){
        throw new RuntimeException(ex);
    } finally {
        server.stop();
    }
}

HttpResponse<String> post(AgentHttpServer server, String path, String sessionId, String body) throws Exception{
    var builder = HttpRequest.newBuilder(uri(server, path))
            .timeout(timeout)
            .header("Content-Type", "text/plain; charset=utf-8")
            .POST(BodyPublishers.ofString(body));
    if (sessionId != null) {
        builder.header("X-Session-Id", sessionId);
    }
    return this.client.send(builder.build(), BodyHandlers.ofString());
}

URI uri(AgentHttpServer server, String path) {
    return URI.create("http://localhost:" + server.port() + path);
}
