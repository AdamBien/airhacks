package com.airhacks;

/**
 *
 * @author airhacks.com
 */
public class MethodCall {

    private String methodName;
    private int duration;

    public MethodCall(String methodName, int duration) {
        this.methodName = methodName;
        this.duration = duration;
    }

    public String getMethodName() {
        return methodName;
    }

    public int getDuration() {
        return duration;
    }
}
