package airhacks.qmp.smoker.control;

import airhacks.qmp.smoker.entity.Smoker;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SmokerServiceTest {

    private SmokerService smokerService;

    @BeforeEach
    void setUp() {
        smokerService = new SmokerService();
    }

    @Test
    void testGetAllSmokers() {
        var smokers = smokerService.getAllSmokers();
        assertNotNull(smokers);
        assertFalse(smokers.isEmpty());
        assertEquals(3, smokers.size());
    }

    @Test
    void testGetSmokerByName() {
        Smoker smoker = smokerService.getSmokerByName("John Doe");
        assertNotNull(smoker);
        assertEquals("John Doe", smoker.name());
        assertEquals("Marlboro", smoker.brand());
        assertEquals(10, smoker.cigarettesPerDay());
    }

    @Test
    void testGetSmokerByNameNotFound() {
        assertThrows(NotFoundException.class, () -> {
            smokerService.getSmokerByName("Unknown");
        });
    }

    @Test
    void testAddSmoker() {
        Smoker newSmoker = new Smoker("Test User", "Test Brand", 5);
        Smoker created = smokerService.addSmoker(newSmoker);
        assertNotNull(created);
        assertEquals("Test User", created.name());
        
        // Verify it was added
        Smoker retrieved = smokerService.getSmokerByName("Test User");
        assertNotNull(retrieved);
        assertEquals("Test Brand", retrieved.brand());
    }

    @Test
    void testDeleteSmoker() {
        // Add a smoker first
        smokerService.addSmoker(new Smoker("To Delete", "Brand", 1));
        
        // Verify it exists
        assertNotNull(smokerService.getSmokerByName("To Delete"));
        
        // Delete it
        smokerService.deleteSmoker("To Delete");
        
        // Verify it's gone
        assertThrows(NotFoundException.class, () -> {
            smokerService.getSmokerByName("To Delete");
        });
    }

    @Test
    void testGetSmokingAnalysis() {
        String analysis = smokerService.getSmokingAnalysis("John Doe");
        assertNotNull(analysis);
        assertTrue(analysis.contains("John Doe"));
        assertTrue(analysis.contains("Marlboro"));
        assertTrue(analysis.contains("Moderate smoker"));
        
        String heavyAnalysis = smokerService.getSmokingAnalysis("Bob Johnson");
        assertTrue(heavyAnalysis.contains("Warning: Heavy smoking detected"));
    }

    @Test
    void testSmokerEntityValidation() {
        assertThrows(BadRequestException.class, () -> {
            new Smoker("", "Brand", 5);
        });
        
        assertThrows(BadRequestException.class, () -> {
            new Smoker("Name", "", 5);
        });
        
        assertThrows(BadRequestException.class, () -> {
            new Smoker("Name", "Brand", -1);
        });
    }

    @Test
    void testSmokingHabitClassification() {
        Smoker light = new Smoker("Light", "Brand", 3);
        assertEquals("Light smoker", light.smokingHabit());
        
        Smoker moderate = new Smoker("Moderate", "Brand", 10);
        assertEquals("Moderate smoker", moderate.smokingHabit());
        
        Smoker heavy = new Smoker("Heavy", "Brand", 20);
        assertEquals("Heavy smoker", heavy.smokingHabit());
    }
}