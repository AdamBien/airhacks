package airhacks.a2a.greeter.boundary;

import java.util.List;

import io.a2a.server.PublicAgentCard;
import io.a2a.spec.AgentCapabilities;
import io.a2a.spec.AgentCard;
import io.a2a.spec.AgentInterface;
import io.a2a.spec.AgentSkill;
import io.a2a.spec.TransportProtocol;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

@ApplicationScoped
public class GreetingCardProducer {

    @Produces
    @PublicAgentCard
    AgentCard agentCard() {
        var url = "http://localhost:8282";
        return new AgentCard.Builder()
                .name("Greeter Agent")
                .description("Greets Java developers")
                .url(url)
                .version("1.0.0")
                .preferredTransport(TransportProtocol.HTTP_JSON.asString())
                .additionalInterfaces(List.of(
                        new AgentInterface(TransportProtocol.HTTP_JSON.asString(), url)))
                .capabilities(new AgentCapabilities.Builder()
                        .streaming(false)
                        .pushNotifications(false)
                        .stateTransitionHistory(false)
                        .build())
                .defaultInputModes(List.of("text"))
                .defaultOutputModes(List.of("text"))
                .skills(List.of(new AgentSkill.Builder()
                        .id("greeting")
                        .name("Greet developers")
                        .description("Greets Java developers with a friendly message")
                        .tags(List.of("greeting", "java","developer"))
                        .examples(List.of("Hello Java developer"))
                        .build()))
                .build();
    }
}
