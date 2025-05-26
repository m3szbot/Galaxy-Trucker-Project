package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShipBoardAttributesTest {
    GameInformation gameInformation;
    ShipBoard shipBoard;
    ShipBoardAttributes shipBoardAttributes;

    @BeforeEach
    void setUp() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        shipBoard = new ShipBoard(gameInformation.getGameType());
        shipBoardAttributes = new ShipBoardAttributes(shipBoard);
    }

    @Test
    void testSetup() {
        assertEquals(2, shipBoardAttributes.getCrewMembers());
        assertEquals(new boolean[]{false, false, false, false}, shipBoardAttributes.getCoveredSides());
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
        assertEquals(0, shipBoardAttributes.getRemainingBatteries());
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getDoubleCannonPower());
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(new int[]{0, 0, 0, 0}, shipBoardAttributes.getGoods());
        assertEquals(0, shipBoardAttributes.getRemainingRedSlots());
        assertEquals(0, shipBoardAttributes.getRemainingBlueSlots());
        assertEquals(0, shipBoardAttributes.getDestroyedComponents());
        assertEquals(0, shipBoardAttributes.getCredits());
    }

    @Test
    void destroyComponent() {
        shipBoardAttributes.destroyComponents(2);
        assertEquals(2, shipBoardAttributes.getDestroyedComponents());
        assertThrows(IllegalStateException.class, () -> {
            shipBoardAttributes.destroyComponents(-1);
        });
    }

    @Test
    void addRemoveCredits() {
        shipBoardAttributes.addCredits(2);
        assertEquals(2, shipBoardAttributes.getCredits());
        shipBoardAttributes.removeCredits(1);
        assertEquals(1, shipBoardAttributes.getCredits());
        shipBoardAttributes.removeCredits(10);
        assertEquals(0, shipBoardAttributes.getCredits());

        assertThrows(IllegalStateException.class, () -> {
            shipBoardAttributes.addCredits(-1);
        });
        assertThrows(IllegalStateException.class, () -> {
            shipBoardAttributes.removeCredits(-1);
        });
    }

}
