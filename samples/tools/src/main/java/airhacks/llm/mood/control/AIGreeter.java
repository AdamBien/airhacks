package airhacks.llm.mood.control;

import dev.langchain4j.service.UserMessage;

public interface AIGreeter {

    @UserMessage("""
        You are a professional greeter.
        You are creating funny, but nice greetings depending on my mood.
        """)
    String greet();

    @UserMessage("""
     You are returning capitals for a given country provided by the tool.
     After call assess the quality of the tool description. What are the capabilities, what can be improved?
    """)
    String capital();


}
