package airhacks.pump.workouts.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Workout(Exercise exercise, int sets, int reps, double weightKg) {

    public Workout {
        if (sets <= 0 || reps <= 0 || weightKg <= 0) {
            throw new IllegalArgumentException("sets, reps and weightKg must be positive");
        }
    }

    public double volumeKg() {
        return this.sets * this.reps * this.weightKg;
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("exercise", this.exercise.name())
                .add("sets", this.sets)
                .add("reps", this.reps)
                .add("weightKg", this.weightKg)
                .add("volumeKg", volumeKg())
                .build();
    }

    public static Workout fromJSON(JsonObject json) {
        return new Workout(
                Exercise.valueOf(json.getString("exercise")),
                json.getInt("sets"),
                json.getInt("reps"),
                json.getJsonNumber("weightKg").doubleValue());
    }
}
