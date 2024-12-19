package airhacks.ndarray;

import static java.lang.System.out;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ai.djl.Device;
import ai.djl.ndarray.NDManager;
import ai.djl.ndarray.types.Shape;

/**
 *
 * @author airhacks.com
 */
public class NDArrayPlaygroundTest {

    NDManager manager;

    @BeforeEach
    void createNDManager() {
        this.manager = NDManager.newBaseManager();

    }

    @Test
    public void commonFunctions() {
        float[][] data = {
                { 1.0f, 2.0f },
                { 3.0f, 4.0f },
                { 5.0f, 6.0f }
        };

            var array = manager.create(data);
            out.println(array);
            
            var transposed = array.transpose();
            out.println(transposed);
            var sum = array.sum();
            var mean = array.mean();
            out.println("Original shape: " + array.getShape());
            out.println("Transposed shape: " + transposed.getShape());
            out.println("Sum: " + sum.toFloatArray()[0]);
            out.println("Mean: " + mean.toFloatArray()[0]);
        }
    

    @Test
    public void creation() {
        var ones = manager.ones(new Shape(2, 3));
        out.println(ones);
        var zeros = manager.zeros(new Shape(2, 3));
        out.println(zeros);
        
        var list = manager.arange(12);
        out.println(list);
        out.println(list.div(5));
    }

    @Test
    public void reshaping() {
        var list = manager.arange(16);
        out.println(list);
        var square = list.reshape(4,4);
        out.println(square);

        var rect = list.reshape(2,8);
        out.println(rect);
    }

    @Test
    public void multiplication() {
        var list = manager.arange(16);
        var square = list.reshape(4,4);
        out.println(square);
        var result = square.mul(2);
        out.println(result);
               
    }

    @Test
    void randomFill(){
        var random = manager.randomUniform(0, 1, new Shape(4,4));
        out.println(random);
    }


    @Test
    public void gpuTransfer() {
        var list = manager.arange(12);
        var gpu = list.toDevice(Device.gpu(), false);
        out.println(gpu);
    }


    @AfterEach
    void closeManager() {
        this.manager.close();
    }


}
