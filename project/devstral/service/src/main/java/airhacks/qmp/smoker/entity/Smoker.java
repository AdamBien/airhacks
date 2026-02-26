package airhacks.qmp.smoker.entity;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;

public record Smoker(String name, String brand, int cigarettesPerDay) {
    
    public Smoker {
        if (name == null || name.trim().isEmpty()) {
            throw new BadRequestException("Smoker name cannot be null or empty");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new BadRequestException("Cigarette brand cannot be null or empty");
        }
        if (cigarettesPerDay < 0) {
            throw new BadRequestException("Cigarettes per day cannot be negative");
        }
    }
    
    public String smokingHabit() {
        if (cigarettesPerDay < 5) {
            return "Light smoker";
        } else if (cigarettesPerDay < 15) {
            return "Moderate smoker";
        } else {
            return "Heavy smoker";
        }
    }
}