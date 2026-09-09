package airhacks.pump.motivation.control;

import static java.lang.System.Logger.Level.INFO;

import java.util.concurrent.ThreadLocalRandom;

import airhacks.pump.motivation.entity.Wisdom;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Arnold {

    static final System.Logger LOGGER = System.getLogger(Arnold.class.getName());

    static final double GIRLY_SET_KG = 1_000;
    static final double SOLID_SESSION_KG = 5_000;

    public Wisdom encouragement() {
        var sayings = Wisdom.values();
        return sayings[ThreadLocalRandom.current().nextInt(sayings.length)];
    }

    public Wisdom verdict(double volumeKg) {
        var wisdom = judge(volumeKg);
        LOGGER.log(INFO, "%.1f kg moved: %s".formatted(volumeKg, wisdom));
        return wisdom;
    }

    Wisdom judge(double volumeKg) {
        if (volumeKg < GIRLY_SET_KG) {
            return Wisdom.NO_GIRLY_SETS;
        }
        if (volumeKg < SOLID_SESSION_KG) {
            return Wisdom.LAST_REPS;
        }
        return Wisdom.EARN_THE_JEALOUSY;
    }
}
