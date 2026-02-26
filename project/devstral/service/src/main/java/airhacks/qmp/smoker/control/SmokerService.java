package airhacks.qmp.smoker.control;

import airhacks.qmp.smoker.entity.Smoker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class SmokerService {

    private static final Logger LOG = Logger.getLogger(SmokerService.class.getName());
    
    private final List<Smoker> smokers = new ArrayList<>();
    
    public SmokerService() {
        // Initialize with some sample data
        smokers.add(new Smoker("John Doe", "Marlboro", 10));
        smokers.add(new Smoker("Jane Smith", "Camel", 3));
        smokers.add(new Smoker("Bob Johnson", "Lucky Strike", 20));
    }
    
    public List<Smoker> getAllSmokers() {
        LOG.info("Retrieving all smokers");
        return new ArrayList<>(smokers);
    }
    
    public Smoker getSmokerByName(String name) {
        LOG.info("Searching for smoker: " + name);
        return smokers.stream()
                .filter(s -> s.name().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Smoker not found: " + name));
    }
    
    public Smoker addSmoker(Smoker smoker) {
        LOG.info("Adding new smoker: " + smoker.name());
        smokers.add(smoker);
        return smoker;
    }
    
    public void deleteSmoker(String name) {
        LOG.info("Deleting smoker: " + name);
        smokers.removeIf(s -> s.name().equalsIgnoreCase(name));
    }
    
    public String getSmokingAnalysis(String name) {
        Smoker smoker = getSmokerByName(name);
        String habit = smoker.smokingHabit();
        
        String analysis = "Smoker: " + smoker.name() + "\n" +
                         "Brand: " + smoker.brand() + "\n" +
                         "Cigarettes per day: " + smoker.cigarettesPerDay() + "\n" +
                         "Smoking habit: " + habit + "\n";
        
        if (smoker.cigarettesPerDay() > 15) {
            analysis += "Warning: Heavy smoking detected. Consider quitting or reducing.";
        }
        
        LOG.info("Generated analysis for: " + name);
        return analysis;
    }
}