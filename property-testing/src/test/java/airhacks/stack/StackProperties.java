package airhacks.stack;

import airhacks.stack.entity.Stack;
import net.jqwik.api.ForAll;
import net.jqwik.api.Property;
import net.jqwik.api.PropertyDefaults;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Property-based tests for Stack using jqwik.
 * Each property test verifies correctness properties across randomly generated inputs.
 */
@PropertyDefaults(tries = 100)
class StackProperties {

    /**
     * Feature: stack-pbt-demo, Property 1: Push-Pop Round Trip (LIFO)
     * For any stack and any element, push then pop returns that element.
     * **Validates: Requirements 2.4, 1.2**
     */
    @Property
    void pushThenPopReturnsElement(@ForAll String element) {
        var stack = new Stack<String>();
        
        stack.push(element);
        var popped = stack.pop();
        
        assertThat(popped).isEqualTo(element);
    }

    /**
     * Feature: stack-pbt-demo, Property 2: Size Consistency
     * Push increases size by 1, pop decreases by 1, K pushes on empty gives size K.
     * **Validates: Requirements 1.1, 2.2, 5.2, 5.3**
     */
    @Property
    void pushAndPopMaintainCorrectSize(@ForAll List<String> elements) {
        var stack = new Stack<String>();
        
        // K pushes on empty stack gives size K
        for (var element : elements) {
            int sizeBefore = stack.size();
            stack.push(element);
            assertThat(stack.size()).isEqualTo(sizeBefore + 1);
        }
        assertThat(stack.size()).isEqualTo(elements.size());
        
        // Pop decreases size by 1
        for (int i = 0; i < elements.size(); i++) {
            int sizeBefore = stack.size();
            stack.pop();
            assertThat(stack.size()).isEqualTo(sizeBefore - 1);
        }
    }

    /**
     * Feature: stack-pbt-demo, Property 3: Peek Idempotence (Non-Destructive)
     * Multiple peeks return same value, peek doesn't change size.
     * **Validates: Requirements 3.1, 3.2, 3.3**
     */
    @Property
    void peekIsIdempotent(@ForAll String element) {
        var stack = new Stack<String>();
        stack.push(element);
        
        int sizeBefore = stack.size();
        var firstPeek = stack.peek();
        var secondPeek = stack.peek();
        var thirdPeek = stack.peek();
        int sizeAfter = stack.size();
        
        // Multiple peeks return same value
        assertThat(firstPeek).isEqualTo(secondPeek).isEqualTo(thirdPeek);
        // Peek doesn't change size
        assertThat(sizeAfter).isEqualTo(sizeBefore);
        // Peek returns same value that pop would return
        assertThat(stack.pop()).isEqualTo(firstPeek);
    }

    /**
     * Feature: stack-pbt-demo, Property 4: Sequence Reversal
     * Pushing N elements then popping N returns them in reverse order.
     * **Validates: Requirements 6.1, 1.3**
     */
    @Property
    void sequenceIsReversedOnPop(@ForAll List<String> elements) {
        var stack = new Stack<String>();
        
        // Push all elements
        for (var element : elements) {
            stack.push(element);
        }
        
        // Pop all and collect
        var popped = new ArrayList<String>();
        while (!stack.isEmpty()) {
            popped.add(stack.pop());
        }
        
        // Popped sequence should be reverse of pushed sequence
        var expected = new ArrayList<>(elements);
        Collections.reverse(expected);
        assertThat(popped).isEqualTo(expected);
    }

    /**
     * Feature: stack-pbt-demo, Property 5: Empty After Equal Push/Pop Count
     * Pushing N elements then popping N results in empty stack.
     * **Validates: Requirements 6.2, 4.3**
     */
    @Property
    void emptyAfterEqualPushPop(@ForAll List<String> elements) {
        var stack = new Stack<String>();
        
        // Push N elements
        for (var element : elements) {
            stack.push(element);
        }
        
        // Pop N elements
        for (int i = 0; i < elements.size(); i++) {
            stack.pop();
        }
        
        // Stack should be empty
        assertThat(stack.isEmpty()).isTrue();
        assertThat(stack.size()).isZero();
    }

    /**
     * Feature: stack-pbt-demo, Property 6: Non-Empty After Push
     * After any push, isEmpty returns false.
     * **Validates: Requirements 4.2**
     */
    @Property
    void nonEmptyAfterPush(@ForAll List<String> initialElements, @ForAll String newElement) {
        var stack = new Stack<String>();
        
        // Start with some initial elements (could be empty)
        for (var element : initialElements) {
            stack.push(element);
        }
        
        // Push new element
        stack.push(newElement);
        
        // Stack should not be empty
        assertThat(stack.isEmpty()).isFalse();
    }
}
