package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.ValueInstantiationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CannonTest {

    // Test constants
    private static final SideType[] TEST_SIDES = {
            SideType.Smooth,
            SideType.Special,
            SideType.Single,
            SideType.Universal
    };

    private static final SideType[] SPECIAL_FRONT_SIDES = {
            SideType.Special, // Front side is Special
            SideType.Double,
            SideType.Single,
            SideType.Universal
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Cannon cannon = new Cannon(TEST_SIDES, true);

        // Assert
        assertArrayEquals(TEST_SIDES, new SideType[]{
                cannon.getFront(),
                cannon.getRight(),
                cannon.getBack(),
                cannon.getLeft()
        });
        assertTrue(cannon.isSingle());
        assertEquals("Cannon", cannon.getComponentName());
    }

    @Test
    void specialSidesExceptions() {
        // a cannon must have exactly 1 special side
        Cannon cannon = new Cannon(new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single}, true);
        assertThrows(IllegalArgumentException.class, () -> {
            final Cannon cannon1 = new Cannon(new SideType[]{SideType.Special, SideType.Special, SideType.Single, SideType.Single}, true);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            final Cannon cannon2 = new Cannon(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single}, true);
        });
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Cannon cannon = new Cannon();

        // Assert
        assertNotNull(cannon);
        assertFalse(cannon.isSingle()); // Default boolean is false
    }

    @Test
    void getFirePower_whenSingleWithSpecialFront_shouldReturn1() {
        // Arrange
        Cannon cannon = new Cannon(SPECIAL_FRONT_SIDES, true);

        // Act & Assert
        assertEquals(1.0f, cannon.getFirePower(), 0.001f);
    }

    @Test
    void getFirePower_whenSingleWithNonSpecialFront_shouldReturn05() {
        // Arrange
        Cannon cannon = new Cannon(TEST_SIDES, true);

        // Act & Assert
        assertEquals(0.5f, cannon.getFirePower(), 0.001f);
    }

    @Test
    void getFirePower_Double_Lateral() {
        // Arrange
        Cannon cannon = new Cannon(TEST_SIDES, false);

        // Act & Assert
        assertEquals(1.0f, cannon.getFirePower(), 0.001f);
    }

    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Cannon",
                    "single": true,
                    "sides": ["Smooth", "Special", "Single", "Universal"]
                }
                """;

        // Act
        Cannon cannon = mapper.readValue(json, Cannon.class);

        // Assert
        assertTrue(cannon.isSingle());
        assertArrayEquals(TEST_SIDES, new SideType[]{
                cannon.getFront(),
                cannon.getRight(),
                cannon.getBack(),
                cannon.getLeft()
        });
    }

    @Test
    void jsonDeserialization_withMissingSingle_shouldDefaultToFalse() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Cannon",
                    "sides": ["Smooth", "Double", "Single", "Universal"]
                }
                """;

        // Act
        assertThrows(ValueInstantiationException.class, () -> {
            final Cannon cannon = mapper.readValue(json, Cannon.class);
        });

    }
}