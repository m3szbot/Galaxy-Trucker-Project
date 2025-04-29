package it.polimi.ingsw.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.SideType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BatteryTest {

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
        Battery battery = new Battery(TEST_SIDES, 3);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                battery.getFront(),
                battery.getRight(),
                battery.getBack(),
                battery.getLeft()
        });
        assertEquals(3, battery.getNumberOfCurrentBatteries());
        assertEquals("Battery", battery.getComponentName());
        assertEquals(3, battery.getBatteryPower());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Battery battery = new Battery();

        // Assert
        assertNotNull(battery);
        assertEquals(0, battery.getNumberOfCurrentBatteries()); // Default int is 0
    }

    @Test
    void removeBattery_shouldDecrementCount() {
        // Arrange
        Battery battery = new Battery(TEST_SIDES, 2);

        // Act
        battery.removeBattery();

        // Assert
        assertEquals(1, battery.getNumberOfCurrentBatteries());
    }

    @Test
    void removeBattery_whenZero_shouldNotGoNegative() {
        // Arrange
        Battery battery = new Battery(TEST_SIDES, 0);

        // Act
        battery.removeBattery();

        // Assert
        assertEquals(0, battery.getNumberOfCurrentBatteries());
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Battery",
                    "numberOfCurrentBatteries": 2,
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Battery battery = mapper.readValue(json, Battery.class);

        // Assert
        assertEquals(2, battery.getNumberOfCurrentBatteries());
        assertArrayEquals(TEST_SIDES, new SideType[]{
                battery.getFront(),
                battery.getRight(),
                battery.getBack(),
                battery.getLeft()
        });
    }

    @Test
    void jsonDeserialization_withMissingBatteries_shouldDefaultToZero() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Battery",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Battery battery = mapper.readValue(json, Battery.class);

        // Assert
        assertEquals(0, battery.getNumberOfCurrentBatteries());
    }
}