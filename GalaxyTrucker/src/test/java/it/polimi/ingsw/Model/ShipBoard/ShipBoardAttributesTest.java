package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ShipBoardAttributesTest {
    GameInformation gameInformation = new GameInformation();
    ShipBoard shipBoard;
    ShipBoardAttributes shipBoardAttributes;

    SideType[] singleSides = new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single};
    SideType[] singleSidesSpecialFront = new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single};
    SideType[] singleSidesSpecialBack = new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single};

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
        assertThrows(IllegalArgumentException.class, () -> {
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

        assertThrows(IllegalArgumentException.class, () -> {
            shipBoardAttributes.addCredits(-1);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            shipBoardAttributes.removeCredits(-1);
        });
    }

    // Test addComponent attribute changes
    @Test
    void addRemovePurpleAlienCabin() throws NotPermittedPlacementException {
        // 1 cabin(2humans) 1 Cabin(purple alien) 1 Alien support(purple)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, true));
        shipBoard.setCrewType(7, 7, CrewType.Purple);

        assertThrows(IllegalArgumentException.class, () -> {
            shipBoard.setCrewType(7, 7, CrewType.Brown);
        });
        assertTrue(shipBoardAttributes.getPurpleAlien());
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(3, shipBoardAttributes.getCrewMembers());
        assertEquals(2, shipBoardAttributes.getHumanCrewMembers());

        // remove cabin
        try {
            shipBoard.removeComponent(7, 7, false);
        } catch (NoHumanCrewLeftException e) {

        }
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemovePurpleAlienSupport() throws NotPermittedPlacementException {
        // 1 Cabin(2humans) 1 Cabin(purple) 1 Alien support(purple)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, true));
        shipBoard.setCrewType(7, 7, CrewType.Purple);

        assertThrows(IllegalArgumentException.class, () -> {
            shipBoard.setCrewType(7, 7, CrewType.Brown);
        });
        assertTrue(shipBoardAttributes.getPurpleAlien());
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(3, shipBoardAttributes.getCrewMembers());
        assertEquals(2, shipBoardAttributes.getHumanCrewMembers());

        // remove alien support
        shipBoard.removeComponent(7, 8, false);
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemoveBrownAlienCabin() throws NotPermittedPlacementException {
        // 1 Cabin(2 humans) 1 Cabin(brown) 1 Alien support(brown)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, false));
        shipBoard.setCrewType(7, 7, CrewType.Brown);

        assertThrows(IllegalArgumentException.class, () -> {
            shipBoard.setCrewType(7, 7, CrewType.Purple);
        });
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertTrue(shipBoardAttributes.getBrownAlien());
        assertEquals(3, shipBoardAttributes.getCrewMembers());
        assertEquals(2, shipBoardAttributes.getHumanCrewMembers());

        // remove cabin
        shipBoard.removeComponent(7, 7, false);
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemoveBrownAlienSupport() throws NotPermittedPlacementException {
        // 1 Cabin(2 humans) 1 Cabin(Brown alien) 1 Alien support (brown)
        // must add cabin so there are enough humans
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));

        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, false));
        shipBoard.setCrewType(7, 7, CrewType.Brown);
        assertThrows(IllegalArgumentException.class, () -> {
            shipBoard.setCrewType(7, 7, CrewType.Purple);
        });
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertTrue(shipBoardAttributes.getBrownAlien());
        assertEquals(3, shipBoardAttributes.getCrewMembers());
        assertEquals(2, shipBoardAttributes.getHumanCrewMembers());

        // remove cabin
        shipBoard.removeComponent(7, 8, false);
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemoveShields() throws NotPermittedPlacementException {
        // side protected only if batteries are available
        // add shield
        shipBoard.addComponent(8, 7, new Shield(singleSidesSpecialFront));
        assertFalse(shipBoardAttributes.checkSideShieldProtected(0));
        // add batteries
        shipBoard.addComponent(7, 8, new Battery(singleSides, 2));
        // check protection
        assertTrue(shipBoardAttributes.checkSideShieldProtected(0));
        assertFalse(shipBoardAttributes.checkSideShieldProtected(1));
        assertThrows(IllegalArgumentException.class, () -> {
            shipBoardAttributes.checkSideShieldProtected(10);
        });

        // remove shield
        shipBoard.removeComponent(8, 7, false);
        assertFalse(shipBoardAttributes.checkSideShieldProtected(0));
    }

    @Test
    void addRemoveSingleEngine() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Engine(singleSidesSpecialBack, true));
        assertEquals(1, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
    }

    @Test
    void addRemoveDoubleEngine() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Engine(singleSidesSpecialBack, false));
        assertEquals(2, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
    }

    @Test
    void addRemoveSingleForwardCannon() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialFront, true));
        assertEquals(1, shipBoardAttributes.getSingleCannonPower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
    }

    @Test
    void addRemoveSingleLateralCannon() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialBack, true));
        assertEquals(0.5, shipBoardAttributes.getSingleCannonPower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
    }

    @Test
    void addRemoveDoubleForwardCannon() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialFront, false));
        assertEquals(1, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
    }

    @Test
    void addRemoveDoubleLateralCannon() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialBack, false));
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(1, shipBoardAttributes.getNumberLateralDoubleCannons());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());
    }

    @Test
    void addRemoveBatteries() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Battery(singleSides, 3));
        assertEquals(3, shipBoardAttributes.getRemainingBatteries());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingBatteries());
    }

    @Test
    void addRemoveHumanCrew() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Cabin(singleSides, CrewType.Human, 2));
        assertEquals(4, shipBoardAttributes.getCrewMembers());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemoveBlueStorage() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Storage(singleSides, false, 1));
        assertEquals(1, shipBoardAttributes.getRemainingBlueSlots());
        assertEquals(0, shipBoardAttributes.getRemainingRedSlots());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingBlueSlots());
    }

    @Test
    void addRemoveRedStorage() throws NotPermittedPlacementException {
        // add
        shipBoard.addComponent(8, 7, new Storage(singleSides, true, 1));
        assertEquals(0, shipBoardAttributes.getRemainingBlueSlots());
        assertEquals(1, shipBoardAttributes.getRemainingRedSlots());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingRedSlots());
    }


}
