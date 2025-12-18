package airhacks.a2a.greeter;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import io.a2a.A2A;
import io.a2a.client.Client;
import io.a2a.client.ClientEvent;
import io.a2a.client.MessageEvent;
import io.a2a.client.TaskEvent;
import io.a2a.client.TaskUpdateEvent;
import io.a2a.client.config.ClientConfig;
import io.a2a.client.http.A2ACardResolver;
import io.a2a.client.transport.rest.RestTransport;
import io.a2a.client.transport.rest.RestTransportConfig;
import io.a2a.spec.AgentCard;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

@Startup
@ApplicationScoped
public class GreeterAgentClient {

    static final Logger LOGGER = System.getLogger(GreeterAgentClient.class.getName());

    Client client;

    @PostConstruct
    public void init() {
        var agentUrl = "http://localhost:8282";
        LOGGER.log(Level.INFO, "Resolving agent card from {0}", agentUrl);
        var agentCard = new A2ACardResolver(agentUrl).getAgentCard();
        LOGGER.log(Level.INFO, "Agent card resolved: {0}", agentCard.name());

        var clientConfig = new ClientConfig.Builder()
                .setAcceptedOutputModes(List.of("text"))
                .build();

        List<BiConsumer<ClientEvent, AgentCard>> consumers = List.of(
                (event, card) -> {
                    if (event instanceof MessageEvent messageEvent) {
                        LOGGER.log(Level.INFO, "Message received from {0}: {1}", card.name(), messageEvent);
                    } else if (event instanceof TaskEvent taskEvent) {
                        LOGGER.log(Level.INFO, "Task event from {0}: {1}", card.name(), taskEvent.getTask());
                    } else if (event instanceof TaskUpdateEvent updateEvent) {
                        LOGGER.log(Level.DEBUG, "Task update from {0}: {1}", card.name(), updateEvent);
                    }
                });

        Consumer<Throwable> errorHandler = error ->
            LOGGER.log(Level.ERROR, "Streaming error occurred", error);

        this.client = Client
                .builder(agentCard)
                .clientConfig(clientConfig)
                .withTransport(RestTransport.class, new RestTransportConfig())
                .addConsumers(consumers)
                .streamingErrorHandler(errorHandler)
                .build();

        LOGGER.log(Level.INFO, "A2A client initialized with RestTransport");
    }

    public void sendMessage(String textMessage) {
        LOGGER.log(Level.DEBUG, "Sending message: {0}", textMessage);
        var message = A2A.toUserMessage(textMessage);
        client.sendMessage(message);
        LOGGER.log(Level.INFO, "Message sent to agent");
    }

}
