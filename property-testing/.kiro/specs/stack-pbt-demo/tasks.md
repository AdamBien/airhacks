# Implementation Plan: Stack PBT Demo

## Overview

This plan implements a generic Stack<T> data structure with comprehensive property-based tests using jqwik. The implementation follows a test-driven approach where core functionality is implemented first, followed by property tests that verify correctness properties.

## Tasks

- [x] 1. Set up project dependencies and package structure
  - Add jqwik dependency to pom.xml
  - Create package `airhacks.stack.entity`
  - _Requirements: Project setup_

- [x] 2. Implement Stack entity and exception
  - [x] 2.1 Create EmptyStackException class
    - Implement as unchecked RuntimeException subclass
    - Add descriptive error message
    - _Requirements: 2.3, 3.4_
  
  - [x] 2.2 Implement Stack<T> class with all operations
    - Implement push(T element) method using ArrayList.addLast()
    - Implement pop() method using ArrayList.removeLast() with empty check
    - Implement peek() method using ArrayList.getLast() with empty check
    - Implement isEmpty() method
    - Implement size() method
    - _Requirements: 1.1, 1.2, 2.1, 2.2, 3.1, 4.1, 4.2, 5.1_

- [x] 3. Write unit tests for edge cases
  - [x] 3.1 Create StackTest class with edge case tests
    - Test new stack is empty and has size zero
    - Test pop on empty stack throws EmptyStackException
    - Test peek on empty stack throws EmptyStackException
    - _Requirements: 2.3, 3.4, 4.1, 5.1_

- [x] 4. Checkpoint - Verify basic functionality
  - Ensure all unit tests pass, ask the user if questions arise.

- [x] 5. Implement property-based tests
  - [x] 5.1 Create StackProperties class with jqwik setup
    - Set up test class with @PropertyDefaults for minimum 100 tries
    - _Requirements: Testing infrastructure_

  - [x] 5.2 Write property test for Push-Pop Round Trip
    - **Property 1: Push-Pop Round Trip (LIFO)**
    - For any stack and any element, push then pop returns that element
    - **Validates: Requirements 2.4, 1.2**

  - [x] 5.3 Write property test for Size Consistency
    - **Property 2: Size Consistency**
    - Push increases size by 1, pop decreases by 1, K pushes on empty gives size K
    - **Validates: Requirements 1.1, 2.2, 5.2, 5.3**

  - [x] 5.4 Write property test for Peek Idempotence
    - **Property 3: Peek Idempotence (Non-Destructive)**
    - Multiple peeks return same value, peek doesn't change size
    - **Validates: Requirements 3.1, 3.2, 3.3**

  - [x] 5.5 Write property test for Sequence Reversal
    - **Property 4: Sequence Reversal**
    - Pushing N elements then popping N returns them in reverse order
    - **Validates: Requirements 6.1, 1.3**

  - [x] 5.6 Write property test for Empty After Equal Push/Pop
    - **Property 5: Empty After Equal Push/Pop Count**
    - Pushing N elements then popping N results in empty stack
    - **Validates: Requirements 6.2, 4.3**

  - [x] 5.7 Write property test for Non-Empty After Push
    - **Property 6: Non-Empty After Push**
    - After any push, isEmpty returns false
    - **Validates: Requirements 4.2**

- [x] 6. Final checkpoint - Verify all tests pass
  - Ensure all unit tests and property tests pass, ask the user if questions arise.

## Notes

- Tasks marked with `*` are optional and can be skipped for faster MVP
- Each property test references specific requirements for traceability
- jqwik will run each property test with 100+ random inputs by default
- Property tests use jqwik's @ForAll annotation for random data generation
