package airhacks.functionurl.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.junit.jupiter.api.Test;

import software.amazon.awscdk.App;

public class FunctionURLStackTest {

    private final static ObjectMapper JSON = new ObjectMapper().configure(SerializationFeature.INDENT_OUTPUT, true);

    @Test
    void functionURLSynth() throws IOException {
        App app = new App();
        var stack = new ServerlessApp(app, "test")
                .functionName("test")
                .build();

        // synthesize the stack to a CloudFormation template
        var actual = JSON.valueToTree(app.synth().getStackArtifact(stack.getArtifactId()).getTemplate());

        // Update once resources have been added to the stack
        assertThat(actual.get("Resources")).isNotEmpty();
    }


    @Test
    void verifyFunctionZip() throws IOException {
        var path = Files.createTempDirectory("function");
        var functionZip = path.resolve("function.zip");
        var existingFile = Files.createFile(functionZip);
        System.out.println("existingFile = " + existingFile);

        var exception = assertThrows(IllegalArgumentException.class,() -> ServerlessApp.verifyFunctionZip("notFunction.zip"));
        assertThat(exception.getMessage()).contains("notFunction.zip");

        exception = assertThrows(IllegalArgumentException.class,() -> ServerlessApp.verifyFunctionZip("/hello/function.zip"));
        assertThat(exception.getMessage()).contains("function.zip not found at:");

        assertTrue(ServerlessApp.verifyFunctionZip(existingFile.toString()));
    }

}
