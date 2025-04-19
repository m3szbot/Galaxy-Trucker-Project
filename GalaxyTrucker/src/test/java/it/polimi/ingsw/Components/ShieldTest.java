package it.polimi.ingsw.Components;

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
        Shield shield = new Shield(FRONT_RIGHT_SPECIAL_SIDES);

        // Assert
        assertEquals(0, shield.getCoveredSide1());
        assertEquals(1, shield.getCoveredSide2());
        assertEquals("Shield", shield.getComponentName());
    }

    @Test
    void defaultConstructor_shouldInitializeWithDefaultValues() {
        // Arrange & Act
        Shield shield = new Shield();

        // Assert
        assertNotNull(shield);
        assertEquals(0, shield.getCoveredSide1()); // Default int is 0
        assertEquals(0, shield.getCoveredSide2()); // Default int is 0
    }

    @Test
    void setCoveredSides_withFrontAndRightSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(FRONT_RIGHT_SPECIAL_SIDES);

        // Assert
        assertEquals(0, shield.getCoveredSide1());
        assertEquals(1, shield.getCoveredSide2());
    }

    @Test
    void setCoveredSides_withFrontAndBackSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(FRONT_BACK_SPECIAL_SIDES);

        // Assert
        assertEquals(0, shield.getCoveredSide1());
        assertEquals(2, shield.getCoveredSide2());
    }

    @Test
    void setCoveredSides_withRightAndLeftSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(RIGHT_LEFT_SPECIAL_SIDES);

        // Assert
        assertEquals(1, shield.getCoveredSide1());
        assertEquals(3, shield.getCoveredSide2());
    }

    @Test
    void setCoveredSides_withBackAndLeftSpecial_shouldSetCorrectSides() {
        // Arrange & Act
        Shield shield = new Shield(BACK_LEFT_SPECIAL_SIDES);

        // Assert
        assertEquals(2, shield.getCoveredSide1());
        assertEquals(3, shield.getCoveredSide2());
    }

    @Test
    void getCoveredSides_shouldReturnCorrectBooleanArray() {
        // Arrange
        Shield shield = new Shield(FRONT_RIGHT_SPECIAL_SIDES);

        // Act
        boolean[] coveredSides = shield.getCoveredSides();

        // Assert
        assertTrue(coveredSides[0]);  // front covered
        assertTrue(coveredSides[1]);  // right covered
        assertFalse(coveredSides[2]); // back not covered
        assertFalse(coveredSides[3]); // left not covered
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
        assertEquals(0, shield.getCoveredSide1());
        assertEquals(1, shield.getCoveredSide2());
        assertArrayEquals(FRONT_RIGHT_SPECIAL_SIDES, new SideType[]{
                shield.getFront(),
                shield.getRight(),
                shield.getBack(),
                shield.getLeft()
        });
    }
}
