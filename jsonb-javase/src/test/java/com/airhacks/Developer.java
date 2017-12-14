
package com.airhacks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.json.bind.annotation.JsonbCreator;
import javax.json.bind.annotation.JsonbDateFormat;
import javax.json.bind.annotation.JsonbProperty;

/**
 *
 * @author airhacks.com
 */
public class Developer {

    public LocalDateTime birthdate;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    public LocalDate firstHack;

       @JsonbCreator
    public Developer(
            @JsonbProperty("birthdate") LocalDateTime birthdate,
            @JsonbProperty("firstHack") LocalDate firstHack) {
        this.birthdate = birthdate;
        this.firstHack = firstHack;
    }

    public Developer() {
    }


}
