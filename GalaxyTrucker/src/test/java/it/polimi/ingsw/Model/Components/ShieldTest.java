package it.polimi.ingsw.Model.Components;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldTest {

    // Test constants
    private static final SideType[] FRONT_RIGHT_SPECIAL_SIDES = {
            SideType.Special, // front (0)
            SideType.Special, // right (1)
            SideType.Smooth,  // back (2)
            SideType.Universal // left (3)
    };

    private static final SideType[] FRONT_BACK_SPECIAL_SIDES = {
            SideType.Special, // front (0)
            SideType.Double,   // right (1)
            SideType.Special,  // back (2)
            SideType.Single    // left (3)
    };

    private static final SideType[] RIGHT_LEFT_SPECIAL_SIDES = {
            SideType.Smooth,   // front (0)
            SideType.Special,  // right (1)
            SideType.Double,   // back (2)
            SideType.Special   // left (3)
    };

    private static final SideType[] BACK_LEFT_SPECIAL_SIDES = {
            SideType.Smooth,   // front (0)
            SideType.Universal,// right (1)
            SideType.Special,  // back (2)
            SideType.Special   // left (3)
    };

    @Test
    void constructor_shouldInitializeFieldsCorrectly() {
        // Arrange & Act
        Shield shield = new Shield(FRONT_RIGHT_SPECIAL_SIDES, 0, 1);

        // Assert
        assertTrue(shield.getCoveredSides()[0]);
        assertTrue(shield.getCoveredSides()[1]);
        assertFalse(shield.getCoveredSides()[2]);
        assertFalse(shield.getCoveredSides()[3]);
        assertEquals("Shield", shield.getComponentName());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Shield shield = new Shield();

        // Assert
        assertNotNull(shield);
        assertFalse(shield.getCoveredSides()[0]);
        assertFalse(shield.getCoveredSides()[1]);
        assertFalse(shield.getCoveredSides()[2]);
        assertFalse(shield.getCoveredSides()[3]);
    }

    @Test
    void setCoveredSides_withFrontAndRightSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(FRONT_RIGHT_SPECIAL_SIDES, 0, 1);

        // Assert
        assertTrue(shield.getCoveredSides()[0]);
        assertTrue(shield.getCoveredSides()[1]);
        assertFalse(shield.getCoveredSides()[2]);
        assertFalse(shield.getCoveredSides()[3]);
    }

    @Test
    void setCoveredSides_withFrontAndBackSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(FRONT_BACK_SPECIAL_SIDES, 0, 2);

        // Assert
        assertTrue(shield.getCoveredSides()[0]);
        assertFalse(shield.getCoveredSides()[1]);
        assertTrue(shield.getCoveredSides()[2]);
        assertFalse(shield.getCoveredSides()[3]);
    }

    @Test
    void setCoveredSides_withRightAndLeftSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(RIGHT_LEFT_SPECIAL_SIDES, 1, 3);

        // Assert
        assertFalse(shield.getCoveredSides()[0]);
        assertTrue(shield.getCoveredSides()[1]);
        assertFalse(shield.getCoveredSides()[2]);
        assertTrue(shield.getCoveredSides()[3]);
    }

    @Test
    void setCoveredSides_withBackAndLeftSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(BACK_LEFT_SPECIAL_SIDES, 2, 3);

        // Assert
        assertFalse(shield.getCoveredSides()[0]);
        assertFalse(shield.getCoveredSides()[1]);
        assertTrue(shield.getCoveredSides()[2]);
        assertTrue(shield.getCoveredSides()[3]);
    }


    @Test
    void jsonDeserialization_shouldWorkCorrectly() throws Exception {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        String json = """
                {
                    "name": "Shield",
                    "sides": ["Special", "Special", "Smooth", "Universal"]
                }
                """;

        // Act
        Shield shield = mapper.readValue(json, Shield.class);

        // Assert


        assertArrayEquals(FRONT_RIGHT_SPECIAL_SIDES, new SideType[]{
                shield.getFront(),
                shield.getRight(),
                shield.getBack(),
                shield.getLeft()
        });
    }
}
