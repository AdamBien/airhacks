package airhacks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class CDKStackTest {
 
    
    @Test
    public void extractPolicyName() {
        var input = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryFullAccess";
        var expected = "AmazonEC2ContainerRegistryFullAccess";
        var actual = CDKStack.extractPolicyName(input);
        assertEquals(expected, actual);
    }
}
