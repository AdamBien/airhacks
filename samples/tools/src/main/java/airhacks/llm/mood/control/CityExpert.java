package airhacks.llm.mood.control;

import dev.langchain4j.service.UserMessage;

public interface CityExpert {
    @UserMessage("""
     You are returning capitals of a given country. Ask me for the country.
    """)
    String capital();
    
}
