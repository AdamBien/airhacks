/*
 */
package com.airhacks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author airhacks.com
 */
public class DateSerializationTest {
    
    private Jsonb jsonb;
    
    @Before
    public void init() {
        this.jsonb = JsonbBuilder.newBuilder().build();
    }

    @Test
    public void serialize() {
        /* Developer developer = new Developer(LocalDateTime.of(1995, Month.MARCH, 12, 12, 42),
                LocalDate.of(1998, Month.MARCH, 12));
         */
        Developer developer = new Developer();
        developer.birthdate = LocalDateTime.of(1995, Month.MARCH, 12, 12, 42);
        developer.firstHack = LocalDate.of(1998, Month.MARCH, 12);
        String serialized = this.jsonb.toJson(developer);
        System.out.println("serialized = " + serialized);
        assertThat(serialized, containsString("birthdate"));

    }

    @Test
    public void deserialize() {
        String deserialzed = " {\"birthdate\":\"1995-03-12T12:42:00\",\"firstHack\":\"1998-03-12\"}";
        Developer duke = this.jsonb.fromJson(deserialzed, Developer.class);
        assertThat(duke.birthdate.getYear(), is(1995));
    }

}
