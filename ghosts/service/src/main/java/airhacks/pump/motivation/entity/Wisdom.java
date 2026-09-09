package airhacks.pump.motivation.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public enum Wisdom {

    ONE_MORE_REP("Come on, you can do it! One more rep!"),
    LAST_REPS("The last three or four reps is what makes the muscle grow."),
    MIND_IS_THE_LIMIT("The mind is the limit. As long as the mind can envision it, you can do it."),
    REPS_REPS_REPS("There are no shortcuts. Everything is reps, reps, reps."),
    EARN_THE_JEALOUSY("Everybody pities the weak. Jealousy you have to earn."),
    STRUGGLE_BUILDS("Strength does not come from winning. Your struggles develop your strengths."),
    NO_GIRLY_SETS("Stop whining and put another plate on the bar.");

    String saying;

    Wisdom(String saying) {
        this.saying = saying;
    }

    public String saying() {
        return this.saying;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("wisdom", name())
                .add("saying", this.saying)
                .build();
    }
}
