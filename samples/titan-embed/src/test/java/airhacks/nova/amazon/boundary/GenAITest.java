package airhacks.nova.amazon.boundary;

import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.Test;

public class GenAITest {


    @Test
    void info() {
        Embeddings.info();
    }


    @Test
    void createEmbeddings() {
        var answer = Embeddings.createEmbeddings("hello, java",256);
        System.out.println(Arrays.toString(answer));
    }
}
