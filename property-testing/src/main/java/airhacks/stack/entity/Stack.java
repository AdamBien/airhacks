package airhacks.stack.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic Stack data structure following the Last-In-First-Out (LIFO) principle.
 * 
 * @param <T> the type of elements stored in this stack
 */
public class Stack<T> {
    
    private final List<T> elements = new ArrayList<>();
    
    public void push(T element) {
        this.elements.addLast(element);
    }
    
    public T pop() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return this.elements.removeLast();
    }
    
    public T peek() {
        if (isEmpty()) {
            throw new EmptyStackException();
        }
        return this.elements.getLast();
    }
    
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }
    
    public int size() {
        return this.elements.size();
    }
}
