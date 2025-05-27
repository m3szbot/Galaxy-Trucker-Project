package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.GeneralView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//already tested: addComponent, removeComponent, goDownChecking, isCompatible, countExternalJunctions, setCrewType, checkNotReachable
//remaining methods: checkErrors, addGoods, checkSlots

// use Shipboard graphic to construct test shipboards!

public class ShipBoardTest {
    GameInformation gameInformation;
    ShipBoard shipBoard;

    // view to print shipboard to debug
    GeneralView generalViewTUI = new GeneralView();

    // components sides
    SideType[] smoothSidesUniversalRight = new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth};
    SideType[] singleSides = new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single};
    SideType[] doubleSides = new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double};
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] specialSidesUniversalRight = new SideType[]{SideType.Special, SideType.Universal, SideType.Special, SideType.Special};
    SideType[] singleSidesSpecialFront = new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single};
    SideType[] singleSidesSpecialBack = new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single};

    // connector components
    Component smoothRightUniversal = new Component(smoothSidesUniversalRight);
    Component singleConnector = new Component(singleSides);
    Component doubleConnector = new Component(doubleSides);
    Component universalConnector = new Component(universalSides);
    Component specialRightUniversal = new Component(specialSidesUniversalRight);


    @BeforeEach
    void setUp() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        shipBoard = new ShipBoard(gameInformation.getGameType());
    }

    // TESTS OF BOTI

    @Test
    void TestSetup() {
        // check starter cabin
        assertNotNull(shipBoard.getComponent(ShipBoard.SB_CENTER_COL, ShipBoard.SB_CENTER_ROW));
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void disconnectedPlacementException() {
        assertThrows(NotPermittedPlacementException.class, () -> {
            shipBoard.addComponent(singleConnector, 9, 9);
        });
    }

    // check connectors (Single, Double, Universal)
    @Test
    void checkConnectorCompatibleJunctions() throws NotPermittedPlacementException {
        // 3 1 1
        // 3 2 2
        shipBoard.addComponent(universalConnector, 7, 8);
        shipBoard.addComponent(singleConnector, 8, 7);
        shipBoard.addComponent(singleConnector, 9, 7);
        shipBoard.addComponent(doubleConnector, 8, 8);
        shipBoard.addComponent(doubleConnector, 9, 8);
        generalViewTUI.printShipboard(shipBoard);
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void checkSmoothCompatibleJunctions() throws NotPermittedPlacementException {
        // 2/1 3
        // 0 3
        shipBoard.addComponent(smoothRightUniversal, 6, 7);
        assertFalse(shipBoard.isErroneous());
        shipBoard.addComponent(universalConnector, 7, 6);
        shipBoard.addComponent(singleConnector, 6, 6);
        generalViewTUI.printShipboard(shipBoard);
        assertTrue(shipBoard.isErroneous());

        shipBoard.removeComponent(6, 6, false);
        assertFalse(shipBoard.isErroneous());

        shipBoard.addComponent(doubleConnector, 6, 6);
        generalViewTUI.printShipboard(shipBoard);
        assertTrue(shipBoard.isErroneous());
    }

    @Test
    void checkSpecialCompatibleJunctions() throws NotPermittedPlacementException {
        // 2/1 3
        // S 3
        shipBoard.addComponent(specialRightUniversal, 6, 7);
        assertFalse(shipBoard.isErroneous());
        shipBoard.addComponent(universalConnector, 7, 6);
        shipBoard.addComponent(singleConnector, 6, 6);
        generalViewTUI.printShipboard(shipBoard);
        assertTrue(shipBoard.isErroneous());

        shipBoard.removeComponent(6, 6, false);
        assertFalse(shipBoard.isErroneous());

        shipBoard.addComponent(doubleConnector, 6, 6);
        generalViewTUI.printShipboard(shipBoard);
        assertTrue(shipBoard.isErroneous());
    }

    // TESTS OF GIACOMO:
    // (testing mainly ShipboardAttributes)
    @Test
    void addComponent() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleEnginePower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleEnginePower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }


    @Test
    void addComponent2() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleCannonPower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleCannonPower(), 0);
    }

    @Test
    void addComponent3() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Shield(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(1), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(2), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(3), false);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(1), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(2), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(3), false);
    }

    @Test
    void addComponent4() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 2);
    }

    @Test
    void addComponent5() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBatteries(), 2);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBatteries(), 0);
    }

    @Test
    void addComponent6() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 0);
    }

    @Test
    void addComponent7() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, false, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 0);
    }

    @Test
    void addComponent8() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.addComponent(new AlienSupport(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true), 8, 8);
        shipBoard.setCrewType(7, 8, CrewType.Purple);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 3);
        shipBoard.removeComponent(7, 8, true);
        shipBoard.removeComponent(8, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 0);
    }

    @Test
    void CountExternalJunctions() throws NotPermittedPlacementException {
        assertEquals(shipBoard.countExternalJunctions(), 4);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.countExternalJunctions(), 6);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.countExternalJunctions(), 4);
    }

    @Test
    void testError() throws NotPermittedPlacementException { //Correct junctions
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        assertTrue(shipBoard.isErroneous());

        assertTrue(shipBoard.getErrorsMatrix()[6][7]);
        assertTrue(shipBoard.getErrorsMatrix()[5][7]);
        assertTrue(shipBoard.getErrorsMatrix()[7][7]);
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 4), 8, 7);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 4);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 0);
        shipBoard.removeComponent(8, 7, false);
        shipBoard.removeComponent(8, 8, false);
        assertTrue(shipBoard.isErroneous());
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 2);
    }


}
