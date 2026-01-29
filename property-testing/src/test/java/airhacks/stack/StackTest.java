package airhacks.stack;

import airhacks.stack.entity.EmptyStackException;
import airhacks.stack.entity.Stack;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for Stack edge cases.
 * Validates: Requirements 2.3, 3.4, 4.1, 5.1
 */
class StackTest {

    @Test
    void newStackIsEmptyWithSizeZero() {
        var stack = new Stack<String>();
        
        assertThat(stack.isEmpty()).isTrue();
        assertThat(stack.size()).isZero();
    }

    @Test
    void popOnEmptyStackThrowsException() {
        var stack = new Stack<Integer>();
        
        assertThatThrownBy(stack::pop)
                .isInstanceOf(EmptyStackException.class);
    }

    @Test
    void peekOnEmptyStackThrowsException() {
        var stack = new Stack<Integer>();
        
        assertThatThrownBy(stack::peek)
                .isInstanceOf(EmptyStackException.class);
    }
}
