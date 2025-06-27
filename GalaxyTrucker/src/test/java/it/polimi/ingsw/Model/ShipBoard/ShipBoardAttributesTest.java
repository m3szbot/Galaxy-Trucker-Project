package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
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
    SideType[] universalSidesSpecialFront = new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] universalSidesSpecialRight = new SideType[]{SideType.Universal, SideType.Special, SideType.Universal, SideType.Universal};
    SideType[] universalSidesSpecialBack = new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal};
    SideType[] universalSidesSpecialLeft = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Special};
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @BeforeEach
    void setUp() {
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        shipBoard = new ShipBoard(gameInformation.getGameType(), Color.RED);
        shipBoardAttributes = shipBoard.getShipBoardAttributes();
    }

    @Test
    void testSetup() {
        assertEquals(2, shipBoard.getComponent(ShipBoard.CENTER_COL, ShipBoard.CENTER_ROW).getCrewMembers());
        assertEquals(2, shipBoardAttributes.getCrewMembers());
        assertArrayEquals(new boolean[]{false, false, false, false}, shipBoardAttributes.getCoveredSides());
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
        assertEquals(0, shipBoardAttributes.getRemainingBatteries());
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());
        assertFalse(shipBoardAttributes.getPurpleAlien());
        assertFalse(shipBoardAttributes.getBrownAlien());
        assertArrayEquals(new int[]{0, 0, 0, 0}, shipBoardAttributes.getGoods());
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

    @Test
    void testReturnCreditsInBankNotes() {
        assertArrayEquals(new int[]{0, 0, 0, 0, 0}, shipBoardAttributes.getCreditsInBankNotes());

        shipBoardAttributes.addCredits(128);
        assertArrayEquals(new int[]{2, 2, 1, 1, 1}, shipBoardAttributes.getCreditsInBankNotes());

        shipBoardAttributes.addCredits(2);
        assertArrayEquals(new int[]{2, 3, 0, 0, 0}, shipBoardAttributes.getCreditsInBankNotes());

    }

    // Test addComponent attribute changes
    @Test
    void addRemovePurpleAlienCabin() throws NotPermittedPlacementException, FracturedShipBoardException, IllegalSelectionException {
        // 1 cabin(2humans) 1 Cabin(purple alien) 1 Alien support(purple)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, true));
        shipBoard.setCrewType(7, 7, CrewType.Purple);

        assertThrows(IllegalSelectionException.class, () -> {
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
    void addRemovePurpleAlienSupport() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // 1 Cabin(2humans) 1 Cabin(purple) 1 Alien support(purple)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, true));
        shipBoard.setCrewType(7, 7, CrewType.Purple);

        assertThrows(IllegalSelectionException.class, () -> {
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
    void addRemoveBrownAlienCabin() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // 1 Cabin(2 humans) 1 Cabin(brown) 1 Alien support(brown)
        // add
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));
        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, false));
        shipBoard.setCrewType(7, 7, CrewType.Brown);

        assertThrows(IllegalSelectionException.class, () -> {
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
    void addRemoveBrownAlienSupport() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // 1 Cabin(2 humans) 1 Cabin(Brown alien) 1 Alien support (brown)
        // must add cabin so there are enough humans
        shipBoard.addComponent(6, 7, new Cabin(singleSides, CrewType.Human, 2));

        shipBoard.addComponent(7, 8, new AlienSupport(singleSides, false));
        shipBoard.setCrewType(7, 7, CrewType.Brown);
        assertThrows(IllegalSelectionException.class, () -> {
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
    void addRemoveShields() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // side protected only if batteries are available
        // add shield
        shipBoard.addComponent(8, 7, new Shield(singleSides, 0, 0));
        assertFalse(shipBoardAttributes.checkSideShieldProtected(0));
        // add batteries
        shipBoard.addComponent(7, 8, new Battery(singleSides, 2));
        // check protection
        assertTrue(shipBoardAttributes.checkSideShieldProtected(0));
        assertFalse(shipBoardAttributes.checkSideShieldProtected(1));
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoardAttributes.checkSideShieldProtected(10);
        });

        // remove shield
        shipBoard.removeComponent(8, 7, false);
        assertFalse(shipBoardAttributes.checkSideShieldProtected(0));
    }

    @Test
    void coverAllSidesWithShields() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        assertArrayEquals(new boolean[]{false, false, false, false}, shipBoardAttributes.getCoveredSides());
        // add shields
        shipBoard.addComponent(7, 6, new Shield(singleSides, 2, 3));
        assertArrayEquals(new boolean[]{false, false, true, true}, shipBoardAttributes.getCoveredSides());

        shipBoard.addComponent(7, 8, new Shield(singleSides, 0, 1));
        assertArrayEquals(new boolean[]{true, true, true, true}, shipBoardAttributes.getCoveredSides());

        // remove shields
        shipBoard.removeComponent(7, 6, false);
        assertArrayEquals(new boolean[]{true, true, false, false}, shipBoardAttributes.getCoveredSides());

        shipBoard.removeComponent(7, 8, false);
        assertArrayEquals(new boolean[]{false, false, false, false}, shipBoardAttributes.getCoveredSides());
    }

    @Test
    void addRemoveSingleEngine() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Engine(singleSidesSpecialBack, true));
        assertEquals(1, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getDestroyedComponents());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
        assertEquals(1, shipBoardAttributes.getDestroyedComponents());
    }

    @Test
    void addRemoveDoubleEngine() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Engine(singleSidesSpecialBack, false));
        assertEquals(2, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(0, shipBoardAttributes.getSingleEnginePower());
        assertEquals(0, shipBoardAttributes.getDestroyedComponents());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getDoubleEnginePower());
        assertEquals(1, shipBoardAttributes.getDestroyedComponents());
    }

    @Test
    void addRemoveSingleForwardCannon() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialFront, true));
        assertEquals(1, shipBoardAttributes.getSingleCannonPower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
    }

    @Test
    void addRemoveSingleLateralCannon() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialBack, true));
        assertEquals(0.5, shipBoardAttributes.getSingleCannonPower());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getSingleCannonPower());
    }

    @Test
    void addRemoveDoubleForwardCannon() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialFront, false));
        assertEquals(1, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
    }

    @Test
    void addRemoveDoubleLateralCannon() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialBack, false));
        assertEquals(0, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(1, shipBoardAttributes.getNumberLateralDoubleCannons());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getNumberLateralDoubleCannons());
    }

    @Test
    void singleCannonAllDirections() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(7, 6, new Cannon(universalSidesSpecialFront, true));
        shipBoard.addComponent(6, 7, new Cannon(universalSidesSpecialLeft, true));
        shipBoard.addComponent(8, 7, new Cannon(universalSidesSpecialRight, true));
        shipBoard.addComponent(7, 8, new Cannon(universalSidesSpecialBack, true));

        assertEquals(2.5, shipBoardAttributes.getSingleCannonPower());
    }

    @Test
    void doubleCannonAllDirections() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(7, 6, new Cannon(universalSidesSpecialFront, false));
        shipBoard.addComponent(6, 7, new Cannon(universalSidesSpecialLeft, false));
        shipBoard.addComponent(8, 7, new Cannon(universalSidesSpecialRight, false));
        shipBoard.addComponent(7, 8, new Cannon(universalSidesSpecialBack, false));

        assertEquals(1, shipBoardAttributes.getNumberForwardDoubleCannons());
        assertEquals(3, shipBoardAttributes.getNumberLateralDoubleCannons());
    }

    @Test
    void addRemoveBatteries() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Battery(singleSides, 3));
        assertEquals(3, shipBoardAttributes.getRemainingBatteries());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingBatteries());
    }

    @Test
    void addRemoveHumanCrew() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Cabin(singleSides, CrewType.Human, 2));
        assertEquals(4, shipBoardAttributes.getCrewMembers());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(2, shipBoardAttributes.getCrewMembers());
    }

    @Test
    void addRemoveBlueStorage() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Storage(singleSides, false, 1));
        assertEquals(1, shipBoardAttributes.getRemainingBlueSlots());
        assertEquals(0, shipBoardAttributes.getRemainingRedSlots());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingBlueSlots());
    }

    @Test
    void addRemoveRedStorage() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add
        shipBoard.addComponent(8, 7, new Storage(singleSides, true, 1));
        assertEquals(0, shipBoardAttributes.getRemainingBlueSlots());
        assertEquals(1, shipBoardAttributes.getRemainingRedSlots());

        // remove
        shipBoard.removeComponent(8, 7, false);
        assertEquals(0, shipBoardAttributes.getRemainingRedSlots());
    }

    @Test
    void setDestroyedComponents() {
        assertThrows(IllegalArgumentException.class, () -> {
            shipBoardAttributes.setDestroyedComponents(-1);
        });
        shipBoardAttributes.setDestroyedComponents(10);
        assertEquals(10, shipBoardAttributes.getDestroyedComponents());
    }

    @Test
    void testGameAliens() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard = new ShipBoard(GameType.TESTGAME, Color.RED);
        shipBoardAttributes = shipBoard.getShipBoardAttributes();

        shipBoard.addComponent(new Cabin(universalSides, CrewType.Human, 2), 7, 6);
        shipBoard.addComponent(new AlienSupport(universalSides, true), 6, 6);
        shipBoard.addComponent(new AlienSupport(universalSides, false), 8, 6);

        assertThrows(IllegalStateException.class, () -> {
            shipBoard.setCrewType(7, 6, CrewType.Purple);
        });
        assertThrows(IllegalStateException.class, () -> {
            shipBoard.setCrewType(7, 6, CrewType.Brown);
        });

    }

    @Test
    void testNoHumanCrewLeftException() throws NoHumanCrewLeftException, IllegalSelectionException {
        shipBoard.removeCrewMember(7, 7);
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeCrewMember(7, 7);
        });
    }

}
