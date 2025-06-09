package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComponentTest {
    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.Smooth,
            SideType.Double,
            SideType.Single,
            SideType.Universal
    };

    ComponentAttributesVisitor visitor = new ComponentAttributesVisitor();

    Component singleEngine = new Engine(new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single}, true);
    Component doubleEngine = new Engine(new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single}, false);

    @Test
    void testSingleEnginePower() {
        assertTrue(doubleEngine.isSingle());
        assertTrue(doubleEngine.isSingle(visitor));
        assertEquals(1, singleEngine.getDrivingPower());
        assertEquals(1, singleEngine.getDrivingPower(visitor));
    }

    @Test
    void testDoubleEnginePower() {
        assertFalse(doubleEngine.isSingle());
        assertFalse(doubleEngine.isSingle(visitor));
        assertEquals(2, doubleEngine.getDrivingPower());
        assertEquals(2, doubleEngine.getDrivingPower(visitor));
    }

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Component component = new Component(TEST_SIDES);

        // Assert
        assertEquals(SideType.Smooth, component.getFront());
        assertEquals(SideType.Double, component.getRight());
        assertEquals(SideType.Single, component.getBack());
        assertEquals(SideType.Universal, component.getLeft());
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
                SideType.Special,
                SideType.Universal,
                SideType.Smooth,
                SideType.Double
        };

        // Act
        component.setSides(newSides);

        // Assert
        assertEquals(SideType.Special, component.getFront());
        assertEquals(SideType.Universal, component.getRight());
        assertEquals(SideType.Smooth, component.getBack());
        assertEquals(SideType.Double, component.getLeft());
    }

    @Test
    void rotate_shouldRotateSidesClockwise() {
        // Arrange
        Component component = new Component(TEST_SIDES);

        // Act
        component.accept(new ComponentRotatorVisitor());

        // Assert
        assertEquals(SideType.Universal, component.getFront()); // Original left
        assertEquals(SideType.Smooth, component.getRight());    // Original front
        assertEquals(SideType.Double, component.getBack());     // Original right
        assertEquals(SideType.Single, component.getLeft());     // Original back
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
