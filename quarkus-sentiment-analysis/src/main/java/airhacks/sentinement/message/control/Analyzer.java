package airhacks.sentinement.message.control;

import airhacks.sentinement.message.entity.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService
public interface Analyzer {

    @SystemMessage("""
            You are working for social media company. You are an AI analyzing messages. You need to categorize the reviews into positive and negative ones.
            You will always answer with a JSON document, and only this JSON document.
            """)
    @UserMessage("""
            Your task is to analyze the message delimited by ---.
            Apply a sentiment analysis to the passed review to determine if it is positive or negative.

            For example:
            - "I love Java", this is a 'POSITIVE' message
            - "Java is outdated", this is a 'NEGATIVE' review
            - "I hate Java, Java is terrible. I need hours for setup and configuration", this is a 'NEGATIVE' review

             Answer with a JSON document containing:
            - the 'result' key set to 'POSITIVE' if the review is positive, 'NEGATIVE' otherwise, depending if the review is positive or negative
            - the 'message' key set to a message explaining the analysis and providing background information

            ---
            {review}
            ---
            """)
    Result analyze(String review);

}
