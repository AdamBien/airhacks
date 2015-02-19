package com.airhacks.beanvalidation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 *
 * @author airhacks.com
 */
public class Workshop {

    @Size(min = 5, max = 20)
    private String name;

    @Min(3)
    @Max(50)
    private int numberOfAttendees;

    public Workshop(String name, int numberOfAttendees) {
        this.name = name;
        this.numberOfAttendees = numberOfAttendees;
    }

}
