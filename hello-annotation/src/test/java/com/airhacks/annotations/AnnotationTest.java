package com.airhacks.annotations;

import static junit.framework.Assert.assertTrue;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class AnnotationTest {

    @Test
    public void checkAnnotationExistence() throws ClassNotFoundException {
        Class<?> clazz = Class.forName("com.airhacks.annotations.MessageService");
        boolean annotationPresent = clazz.
                isAnnotationPresent(CustomFilter.class);
        assertTrue(annotationPresent);

        CustomFilter customFilter = clazz.getAnnotation(CustomFilter.class);
        System.out.println("customFilter = " + customFilter.name());

    }

}
