
import java.util.Arrays;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/*
 */
/**
 *
 * @author adam-bien.com
 */
@RunWith(Parameterized.class)
public class ParameterizedTest {

    @Parameterized.Parameter(0)
    public int input;

    @Parameterized.Parameter(1)
    public int expected;

    @Parameters(name = "Test nbr: {index} -> multiply({0} {1})")
    public static List<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {0, 0},
            {1, 2},
            {2, 4},
            {3, 6},
            //fails on purpose
            {4, 4}});
    }

    @Before
    public void init() {
        System.out.printf("Initializing: Input %s output %s", input, expected);
    }

    @Test
    public void compute() {
        System.out.printf("Input %s output %s", input, expected);
        assertThat(expected, is(input * 2));
    }

}
