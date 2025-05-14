package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EngineTest {

    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.SMOOTH,
            SideType.DOUBLE,
            SideType.SPECIAL,  // Back side is SPECIAL
            SideType.UNIVERSAL
    };

    private static final SideType[] ROTATABLE_SIDES = {
            SideType.SPECIAL,// Front side is SPECIAL (should rotate)
            SideType.UNIVERSAL,//Right
            SideType.DOUBLE,//Back
            SideType.SMOOTH//Left
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Engine engine = new Engine(TEST_SIDES, true);

        // Assert
        assertEquals(SideType.SMOOTH, engine.getFront());
        assertEquals(SideType.DOUBLE, engine.getRight());
        assertEquals(SideType.SPECIAL, engine.getBack());  // SPECIAL should be at back
        assertEquals(SideType.UNIVERSAL, engine.getLeft());
        assertTrue(engine.isSingle());
        assertEquals("Engine", engine.getComponentName());
    }

    @Test
    void constructor_shouldRotateUntilSpecialIsAtBack() {
        // Arrange & Act
        Engine engine = new Engine(ROTATABLE_SIDES, false);

        // Assert - Should have rotated until SPECIAL is at back
        assertEquals(SideType.SPECIAL, engine.getBack());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Engine engine = new Engine();

        // Assert
        assertNotNull(engine);
        assertFalse(engine.isSingle()); // Default boolean is false
        assertNull(engine.getBack());   // No sides initialized
    }

    @Test
    void getDrivingPower_whenSingle_shouldReturn1() {
        // Arrange
        Engine engine = new Engine(TEST_SIDES, true);

        // Act & Assert
        assertEquals(1, engine.getDrivingPower());
    }

    @Test
    void getDrivingPower_whenNotSingle_shouldReturn2() {
        // Arrange
        Engine engine = new Engine(TEST_SIDES, false);

        // Act & Assert
        assertEquals(2, engine.getDrivingPower());
    }

    @Test
    void rotate_whenSpecialNotAtBack_shouldRotate() {
        // Arrange
        Engine engine = new Engine(ROTATABLE_SIDES, true);
        SideType originalFront = engine.getFront();
        SideType originalRight = engine.getRight();
        SideType originalBack = engine.getBack();
        SideType originalLeft = engine.getLeft();

        assertEquals(originalFront, engine.getFront());
        assertEquals(originalRight, engine.getRight());
        assertEquals(originalBack, engine.getBack());
        assertEquals(originalLeft, engine.getLeft());

        // Act
        engine.rotate();

        // Assert - Should rotate normally
        assertEquals(originalLeft, engine.getFront());
        assertEquals(originalFront, engine.getRight());
        assertEquals(originalRight, engine.getBack());
        assertEquals(originalBack, engine.getLeft());
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Engine",
                    "single": true,
                    "sides": ["Smooth", "Double", "Special", "Universal"]
                }
                """;

        // Act
        Engine engine = mapper.readValue(json, Engine.class);

        // Assert
        assertTrue(engine.isSingle());
        assertEquals(SideType.SMOOTH, engine.getFront());
        assertEquals(SideType.DOUBLE, engine.getRight());
        assertEquals(SideType.SPECIAL, engine.getBack());
        assertEquals(SideType.UNIVERSAL, engine.getLeft());
    }

    @Test
    void jsonDeserialization_withMissingSingle_shouldDefaultToFalse() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Engine",
                    "sides": ["Smooth", "Double", "Special", "Universal"]
                }
                """;

        // Act
        Engine engine = mapper.readValue(json, Engine.class);

        // Assert
        assertFalse(engine.isSingle());
    }
}