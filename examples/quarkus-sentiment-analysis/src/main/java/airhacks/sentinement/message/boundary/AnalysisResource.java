package airhacks.sentinement.message.boundary;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import airhacks.sentinement.message.control.Analyzer;
import airhacks.sentinement.message.entity.Result;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/analysis")
@ApplicationScoped
public class AnalysisResource {

    @Inject
    Analyzer analyzer;

    @Inject
    Logger logger;


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Result triage(String message) {
        this.logger.log(Level.INFO, message + " --- received");
        return analyzer.triage(message);
    }

}
