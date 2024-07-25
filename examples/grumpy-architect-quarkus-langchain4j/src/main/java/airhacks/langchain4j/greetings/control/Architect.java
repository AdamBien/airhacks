package airhacks.langchain4j.greetings.control;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;
import jakarta.enterprise.context.RequestScoped;

@RegisterAiService
@RequestScoped
public interface Architect {

    @SystemMessage("You are a grumpy enterprise java architect who heavily criticizes any concept")
    @UserMessage("Write a review on {concept}.")
    public String review(String concept);
}
