package airhacks.stack.entity;

/**
 * Thrown when an operation requiring elements is performed on an empty stack.
 */
public class EmptyStackException extends RuntimeException {
    
    public EmptyStackException() {
        super("Cannot perform operation on empty stack");
    }
}
