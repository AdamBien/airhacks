package airhacks.zsmith.http.boundary;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

import airhacks.zsmith.http.control.ActHandler;
import airhacks.zsmith.http.control.ChatHandler;
import airhacks.zsmith.http.control.Sessions;
import airhacks.zsmith.logging.control.Log;

public class AgentHttpServer {

    static String HOST = "0.0.0.0";
    static int BACKLOG = 0;

    HttpServer server;
    ExecutorService executor;

    AgentHttpServer(HttpServer server, ExecutorService executor) {
        this.server = server;
        this.executor = executor;
    }

    public static AgentHttpServer start(ChatEngine engine, int port) {
        try {
            var server = HttpServer.create(new InetSocketAddress(HOST, port), BACKLOG);
            var sessions = new Sessions();
            server.createContext("/chat", new ChatHandler(engine, sessions));
            server.createContext("/act", new ActHandler(engine, sessions));
            var executor = Executors.newCachedThreadPool();
            server.setExecutor(executor);
            server.start();
            Log.agent("HTTP server listening on " + HOST + ":" + server.getAddress().getPort());
            return new AgentHttpServer(server, executor);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to start HTTP server on port " + port, e);
        }
    }

    public int port() {
        return this.server.getAddress().getPort();
    }

    public void stop() {
        this.server.stop(0);
        this.executor.shutdownNow();
    }
}
