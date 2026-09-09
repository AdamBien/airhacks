package airhacks.pump.workouts.control;

import static java.lang.System.Logger.Level.INFO;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import airhacks.pump.workouts.entity.Workout;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class WorkoutLog {

    static final System.Logger LOGGER = System.getLogger(WorkoutLog.class.getName());

    List<Workout> workouts = new CopyOnWriteArrayList<>();

    public void add(Workout workout) {
        LOGGER.log(INFO, "logging " + workout);
        this.workouts.add(workout);
    }

    public List<Workout> workouts() {
        return List.copyOf(this.workouts);
    }

    public double totalVolumeKg() {
        return this.workouts.stream()
                .mapToDouble(Workout::volumeKg)
                .sum();
    }
}
