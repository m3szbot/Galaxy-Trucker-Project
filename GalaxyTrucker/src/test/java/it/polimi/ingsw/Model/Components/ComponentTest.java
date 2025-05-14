package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {

    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.SMOOTH,
            SideType.DOUBLE,
            SideType.SINGLE,
            SideType.UNIVERSAL
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Component component = new Component(TEST_SIDES);

        // Assert
        assertEquals(SideType.SMOOTH, component.getFront());
        assertEquals(SideType.DOUBLE, component.getRight());
        assertEquals(SideType.SINGLE, component.getBack());
        assertEquals(SideType.UNIVERSAL, component.getLeft());
    }

    @Test
    void defaultConstructor_shouldInitializeWithNullSides() {
        // Arrange & Act
        Component component = new Component();

        // Assert
        assertNull(component.getFront());
        assertNull(component.getRight());
        assertNull(component.getBack());
        assertNull(component.getLeft());
    }

    @Test
    void setSides_shouldUpdateAllSides() {
        // Arrange
        Component component = new Component();
        SideType[] newSides = {
                SideType.SPECIAL,
                SideType.UNIVERSAL,
                SideType.SMOOTH,
                SideType.DOUBLE
        };

        // Act
        component.setSides(newSides);

        // Assert
        assertEquals(SideType.SPECIAL, component.getFront());
        assertEquals(SideType.UNIVERSAL, component.getRight());
        assertEquals(SideType.SMOOTH, component.getBack());
        assertEquals(SideType.DOUBLE, component.getLeft());
    }

    @Test
    void rotate_shouldRotateSidesClockwise() {
        // Arrange
        Component component = new Component(TEST_SIDES);

        // Act
        component.rotate();

        // Assert
        assertEquals(SideType.UNIVERSAL, component.getFront()); // Original left
        assertEquals(SideType.SMOOTH, component.getRight());    // Original front
        assertEquals(SideType.DOUBLE, component.getBack());     // Original right
        assertEquals(SideType.SINGLE, component.getLeft());     // Original back
    }

    @Test
    void getComponentName_shouldReturnConnector() {
        // Arrange
        Component component = new Component();

        // Act & Assert
        assertEquals("Connector", component.getComponentName());
    }

    @Test
    void defaultMethods_shouldReturnDefaultValues() {
        // Arrange
        Component component = new Component();

        // Act & Assert
        assertEquals(0, component.getDrivingPower());
        assertEquals(0.0f, component.getFirePower());
        assertEquals(0, component.getCrewMembers());
        assertEquals(0, component.getBatteryPower());
        assertArrayEquals(new boolean[]{false, false, false, false}, component.getCoveredSides());
        assertEquals(0, component.getAvailableRedSlots());
        assertEquals(0, component.getAvailableBlueSlots());
        assertFalse(component.amIASupport());
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Component",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        Component component = mapper.readValue(json, Component.class);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                component.getFront(),
                component.getRight(),
                component.getBack(),
                component.getLeft()
        });
        assertEquals("Connector", component.getComponentName());
    }
}
