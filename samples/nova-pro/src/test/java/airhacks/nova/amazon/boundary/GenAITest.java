package airhacks.nova.amazon.boundary;

import org.junit.jupiter.api.Test;

import airhacks.nova.logging.control.Log;

public class GenAITest {

    @Test
    void info() {
        GenAI.info();
    }

    @Test
    void infer() {
        var answer = GenAI.infer("Is AEG a programming language? Answer only with true or false. Lower case. Nothing else");
        Log.ANSWER.out(answer);
    }
}
