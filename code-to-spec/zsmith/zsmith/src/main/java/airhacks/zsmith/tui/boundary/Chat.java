package airhacks.zsmith.tui.boundary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import airhacks.zsmith.logging.control.Log;
import airhacks.zsmith.tui.control.ChatClient;
import airhacks.zsmith.tui.entity.Config;
import airhacks.zsmith.tui.entity.Response;

public class Chat {

    static final String USAGE = "Usage: zschat [--host HOST] [--port PORT] [--session ID] [--timeout SECS]";

    static final String HELP = """
            /help          Show this help
            /act [seed]    Trigger autonomous action
            /session       Show current session ID
            /quit, /exit   Exit""";

    String host = "localhost";
    int port = 8080;
    String sessionId;
    int timeout = 120;
    ChatClient client;

    public Chat withHost(String host) {
        this.host = host;
        return this;
    }

    public Chat withPort(int port) {
        this.port = port;
        return this;
    }

    public Chat withSession(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Chat withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }

    public void start() {
        this.client = new ChatClient(new Config(this.host, this.port, this.timeout));
        banner();
        try (var input = new BufferedReader(new InputStreamReader(System.in))) {
            prompt();
            while (input.readLine() instanceof String line && handle(line)) {
                prompt();
            }
        } catch (IOException problem) {
            Log.error("Fatal: " + problem.getMessage());
        }
        Log.info("Bye.");
    }

    Chat parse(String... args) {
        for (var i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--host" -> withHost(args[++i]);
                case "--port" -> withPort(Integer.parseInt(args[++i]));
                case "--session" -> withSession(args[++i]);
                case "--timeout" -> withTimeout(Integer.parseInt(args[++i]));
                default -> throw new IllegalArgumentException(USAGE);
            }
        }
        return this;
    }

    boolean handle(String line) {
        var input = line.strip();
        if (input.isEmpty()) {
            return true;
        }
        if (input.equals("/quit") || input.equals("/exit")) {
            return false;
        }
        if (input.startsWith("/")) {
            command(input);
            return true;
        }
        show(this.client.chat(this.sessionId, input));
        return true;
    }

    void command(String line) {
        var parts = line.split("\\s+", 2);
        switch (parts[0]) {
            case "/help" -> Log.info(HELP);
            case "/session" -> Log.info("Session: " + (this.sessionId != null ? this.sessionId : "(not yet established)"));
            case "/act" -> show(this.client.act(this.sessionId, parts.length > 1 ? parts[1] : ""));
            default -> Log.error("Unknown command: " + parts[0] + ". Type /help");
        }
    }

    void show(Response response) {
        if (response.status() != 200) {
            Log.error("Error " + response.status() + ": " + response.body());
            return;
        }
        if (this.sessionId == null) {
            this.sessionId = response.sessionId();
        }
        Log.answer(response.body());
    }

    void banner() {
        Log.info("zschat — connecting to " + this.host + ":" + this.port);
        Log.info("Type a message to chat, /help for commands, /quit to exit");
        if (this.sessionId != null) {
            Log.info("Resuming session: " + this.sessionId);
        }
    }

    void prompt() {
        System.out.print("> ");
        System.out.flush();
    }

    void main(String... args) {
        try {
            parse(args);
        } catch (IllegalArgumentException invalid) {
            Log.error(invalid.getMessage());
            return;
        }
        start();
    }
}
