package airhacks.a2a.greeter.boundary;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.Map;

import io.a2a.server.agentexecution.AgentExecutor;
import io.a2a.server.agentexecution.RequestContext;
import io.a2a.server.events.EventQueue;
import io.a2a.server.tasks.TaskUpdater;
import io.a2a.spec.JSONRPCError;
import io.a2a.spec.Message;
import io.a2a.spec.Part;
import io.a2a.spec.TextPart;
import io.a2a.spec.UnsupportedOperationError;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class AgentExecutorProducer {

    static final Logger LOGGER = System.getLogger(AgentExecutorProducer.class.getName());

    static void logMessageContent(Message message) {
        if (message == null || message.getParts() == null) {
            LOGGER.log(Level.INFO, "No message content");
            return;
        }
        message.getParts().stream()
                .filter(TextPart.class::isInstance)
                .map(TextPart.class::cast)
                .map(TextPart::getText)
                .forEach(text -> LOGGER.log(Level.INFO, "Message content: {0}", text));
    }

    @Produces
    AgentExecutor agentExecutor() {
        return new AgentExecutor() {
            @Override
            public void execute(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                logMessageContent(context.getMessage());
                var updater = new TaskUpdater(context, eventQueue);
                updater.submit();
                updater.startWork();

                var responsePart = new TextPart("Hello World", Map.of());
                List<Part<?>> parts = List.of(responsePart);
                updater.addArtifact(parts, null, null, null);
                updater.complete();
                LOGGER.log(Level.INFO, "Task completed");
            }

            @Override
            public void cancel(RequestContext context, EventQueue eventQueue) throws JSONRPCError {
                LOGGER.log(Level.WARNING, "Cancel requested (not supported)");
                throw new UnsupportedOperationError();
            }
        };
    }
}
