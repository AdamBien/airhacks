package airhacks.pump.workouts.boundary;

import airhacks.pump.motivation.control.Arnold;
import airhacks.pump.workouts.control.WorkoutLog;
import airhacks.pump.workouts.entity.Workout;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("workouts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class WorkoutsResource {

    @Inject
    WorkoutLog workoutLog;

    @Inject
    Arnold arnold;

    @POST
    public Response add(JsonObject body) {
        var workout = readWorkout(body);
        this.workoutLog.add(workout);
        var logged = Json.createObjectBuilder()
                .add("workout", workout.toJSON())
                .add("motivation", this.arnold.verdict(workout.volumeKg()).saying())
                .build();
        return Response.status(Response.Status.CREATED).entity(logged).build();
    }

    @GET
    public Response session() {
        var totalVolumeKg = this.workoutLog.totalVolumeKg();
        var session = Json.createObjectBuilder()
                .add("workouts", loggedWorkouts())
                .add("totalVolumeKg", totalVolumeKg)
                .add("verdict", this.arnold.verdict(totalVolumeKg).saying())
                .build();
        return Response.ok(session).build();
    }

    JsonArray loggedWorkouts() {
        return this.workoutLog.workouts().stream()
                .map(Workout::toJSON)
                .collect(Json::createArrayBuilder, JsonArrayBuilder::add, JsonArrayBuilder::addAll)
                .build();
    }

    Workout readWorkout(JsonObject body) {
        try {
            return Workout.fromJSON(body);
        } catch (IllegalArgumentException | NullPointerException | ClassCastException _) {
            throw new BadRequestException(
                    "expected exercise, sets, reps and weightKg -- don't be a girly man");
        }
    }
}
