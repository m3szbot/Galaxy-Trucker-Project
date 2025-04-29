package it.polimi.ingsw.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Components.AlienSupport;
import it.polimi.ingsw.Model.Components.SideType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlienSupportTest {

    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.Smooth,
            SideType.Double,
            SideType.Single,
            SideType.Universal
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        AlienSupport support = new AlienSupport(TEST_SIDES, true);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                support.getFront(),
                support.getRight(),
                support.getBack(),
                support.getLeft()
        });
        assertTrue(support.isPurple());
        assertEquals("AlienSupport", support.getComponentName());
        assertTrue(support.amIASupport());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        AlienSupport support = new AlienSupport();

        // Assert
        assertNotNull(support);
        assertFalse(support.isPurple()); // Default boolean is false
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "AlienSupport",
                    "purple": true,
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        AlienSupport support = mapper.readValue(json, AlienSupport.class);

        // Assert
        assertTrue(support.isPurple());
        assertArrayEquals(TEST_SIDES, new SideType[]{
                support.getFront(),
                support.getRight(),
                support.getBack(),
                support.getLeft()
        });
    }

    @Test
    void jsonDeserialization_withMissingPurple_shouldDefaultToFalse() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "AlienSupport",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        AlienSupport support = mapper.readValue(json, AlienSupport.class);

        // Assert
        assertFalse(support.isPurple());
    }
}