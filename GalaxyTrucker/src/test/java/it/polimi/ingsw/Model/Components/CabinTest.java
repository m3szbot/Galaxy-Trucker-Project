package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CabinTest {

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
        Cabin cabin = new Cabin(TEST_SIDES, CrewType.Human, 2);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                cabin.getFront(),
                cabin.getRight(),
                cabin.getBack(),
                cabin.getLeft()
        });
        assertEquals(2, cabin.getCrewMembers()); // Default value
        assertEquals("Cabin", cabin.getComponentName());
        assertEquals(2, cabin.getCrewMembers());
        assertNull(cabin.getCrewType()); // Not set initially
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Cabin cabin = new Cabin();

        // Assert
        assertNotNull(cabin);
        assertEquals(2, cabin.getCrewMembers());
        assertNull(cabin.getCrewType());
    }

    @Test
    void removeInhabitant_shouldDecrementCount() {
        // Arrange
        Cabin cabin = new Cabin(TEST_SIDES, CrewType.Human, 2);
        cabin.setCrewType(CrewType.Human); // Defaults to 2 inhabitants

        // Act
        cabin.removeInhabitant();

        // Assert
        assertEquals(1, cabin.getCrewMembers());
    }

    @Test
    void removeInhabitant_whenZero_shouldNotGoNegative() {
        // Arrange
        Cabin cabin = new Cabin(TEST_SIDES, CrewType.Human, 2);
        cabin.setCrewType(CrewType.Purple); // Sets to 1 inhabitant
        cabin.removeInhabitant(); // Should be 0 now

        // Act
        cabin.removeInhabitant(); // Attempt to go below 0

        // Assert
        assertEquals(0, cabin.getCrewMembers());
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Cabin",
                    "crewType": "Human",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Cabin cabin = mapper.readValue(json, Cabin.class);

        // Assert
        assertEquals(CrewType.Human, cabin.getCrewType());
        assertEquals(2, cabin.getCrewMembers());
        assertArrayEquals(TEST_SIDES, new SideType[]{
                cabin.getFront(),
                cabin.getRight(),
                cabin.getBack(),
                cabin.getLeft()
        });
    }

    @Test
    void jsonDeserialization_withMissingCrewType_shouldDefaultToNull() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Cabin",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Cabin cabin = mapper.readValue(json, Cabin.class);

        // Assert
        assertNull(cabin.getCrewType());
        assertEquals(2, cabin.getCrewMembers());
    }

}