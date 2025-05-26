package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShipBoardAttributesTest {
    GameInformation gameInformation = new GameInformation();
    ShipBoard shipBoard;
    ShipBoardAttributes shipBoardAttributes;


    @BeforeEach
    void setUp() {
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        shipBoard = new ShipBoard(gameInformation.getGameType());
        shipBoardAttributes = shipBoard.getShipBoardAttributes();
    }

    @Test
    void testSetup() {
        assertEquals(2, shipBoard.getComponent(ShipBoard.SB_CENTER_COL, ShipBoard.SB_CENTER_ROW).getCrewMembers());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
        assertFalse(shipBoardAttributes.getCoveredSides()[0]);
        assertFalse(shipBoardAttributes.getCoveredSides()[1]);
        assertFalse(shipBoardAttributes.getCoveredSides()[2]);
        assertFalse(shipBoardAttributes.getCoveredSides()[3]);
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
        assertEquals(0, shipBoardAttributes.getRemainingBatteries());
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(0, shipBoardAttributes.getGoods()[0]);
        assertEquals(0, shipBoardAttributes.getGoods()[1]);
        assertEquals(0, shipBoardAttributes.getGoods()[2]);
        assertEquals(0, shipBoardAttributes.getGoods()[3]);
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
