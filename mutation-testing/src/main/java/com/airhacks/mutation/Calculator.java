package com.airhacks.mutation;

/**
 *
 * @author airhacks.com
 */
public class Calculator {

    private int a;
    private int b;
    private int c;

    public Calculator(int a, int b) {
        this.a = a;
        this.b = b;
    }

    public void multiply() {
        this.c = a * b;
    }

    public int getResult() {
        return c;
    }

}
