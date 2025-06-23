package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TUIView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ShipBoardTest {
    ShipBoard shipBoard;

    // view to print shipboard to debug
    GeneralView generalViewTUI = new TUIView();

    // components sides
    SideType[] smoothSides = new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth};
    SideType[] smoothSidesUniversalRight = new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth};
    SideType[] singleSides = new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single};
    SideType[] doubleSides = new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double};
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] specialSides = new SideType[]{SideType.Special, SideType.Special, SideType.Special, SideType.Special};
    SideType[] specialSidesUniversalRight = new SideType[]{SideType.Special, SideType.Universal, SideType.Special, SideType.Special};
    SideType[] singleSidesSpecialFront = new SideType[]{SideType.Special, SideType.Single, SideType.Single, SideType.Single};
    SideType[] singleSidesSpecialBack = new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single};
    SideType[] singleSidesSpecialRight = new SideType[]{SideType.Single, SideType.Special, SideType.Single, SideType.Single};
    SideType[] singleSidesSpecialLeft = new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Special};

    // connector components
    Component smoothRightUniversal = new Component(smoothSidesUniversalRight);
    Component smoothSidesConnector = new Component(smoothSides);
    Component singleConnector = new Component(singleSides);
    Component doubleConnector = new Component(doubleSides);
    Component universalConnector = new Component(universalSides);
    Component specialRightUniversal = new Component(specialSidesUniversalRight);
    Component specialConnector = new Component(specialSides);


    // TESTS OF BOTI

    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(GameType.NORMALGAME);
    }

    @Test
    void TestSetupTestGameShipboard() {
        shipBoard = new ShipBoard(GameType.TESTGAME);
        assertNotNull(shipBoard.getComponent(ShipBoard.CENTER_COL, ShipBoard.CENTER_ROW));
        assertFalse(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void TestSetupNormalGameShipboard() {
        // check starter cabin
        assertNotNull(shipBoard.getComponent(ShipBoard.CENTER_COL, ShipBoard.CENTER_ROW));
        assertFalse(shipBoard.isErroneous());
        generalViewTUI.printFullShipboard(shipBoard);
    }

    @Test
    void checkFloatingBottomRightCorner() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // create bridge to bottom right corner, then remove connection one by one
        shipBoard.addComponent(universalConnector, 7, 8);
        shipBoard.addComponent(universalConnector, 8, 8);
        shipBoard.addComponent(universalConnector, 9, 8);
        shipBoard.addComponent(universalConnector, 10, 8);
        shipBoard.addComponent(universalConnector, 10, 9);
        assertFalse(shipBoard.isErroneous());

        // removals
        // error caused by floating cabin
        shipBoard.removeComponent(7, 8, false);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(8, 8, false);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(9, 8, false);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(10, 8, false);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(10, 9, false);
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void testFractureConnectorFallsOffNoException() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(9, 7, new Component(universalSides));

        // no exception should be thrown
        shipBoard.removeComponent(8, 7, true);
        assertNull(shipBoard.getComponent(8, 7));
        assertNull(shipBoard.getComponent(9, 7));
        assertEquals(2, shipBoard.getShipBoardAttributes().getDestroyedComponents());
    }

    @Test
    void testFractureAlienFallsOffNoException() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // add alien cabin then remove connector so only center cabin is kept
        // no exception launched, alien automatically erased

        // 77Cabin 87connector 97Cabin 107AlienSupport
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(10, 7, new AlienSupport(universalSides, true));
        shipBoard.setCrewType(9, 7, CrewType.Purple);

        generalViewTUI.printShipboard(shipBoard);

        assertEquals(3, shipBoard.getShipBoardAttributes().getCrewMembers());
        // shouldn't launch fracture
        shipBoard.removeComponent(8, 7, true);

        // fall off successful
        assertEquals(2, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(3, shipBoard.getShipBoardAttributes().getDestroyedComponents());
        generalViewTUI.printShipboard(shipBoard);

    }

    @Test
    void testFractureTwoCabins() throws NotPermittedPlacementException, IllegalSelectionException {
        // FracturedException thrown
        // 77Cabin 78connector 79Cabin
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        assertEquals(4, shipBoard.getShipBoardAttributes().getCrewMembers());

        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(8, 7, true);
        });
    }

    @Test
    void testFractureThreeCabinsRemoveCenter() throws NotPermittedPlacementException, IllegalSelectionException {
        // checks if center is updated after removal

        // FracturedException thrown
        // 67Cabin 77Cabin 87Cabin
        shipBoard.addComponent(6, 7, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(8, 7, new Cabin(universalSides, CrewType.Human, 2));
        assertEquals(6, shipBoard.getShipBoardAttributes().getCrewMembers());

        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(7, 7, true);
        });
    }

    @Test
    void testCenterCabinRepeatedlyRemoved() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        /*
        set up shipboard and always remove center cabin to see if it gets relocated automatically
        1: add new connector and cabin
        2: remove connector and catch FracturedException
        3: remove old center cabin
        4: repeat
         */
        // block to repeat:
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(8, 7, true);
        });
        shipBoard.removeComponent(7, 7, true);
        assertEquals(2, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(2, shipBoard.getShipBoardAttributes().getDestroyedComponents());
        // cabin: 8 6

        // block to repeat:
        shipBoard.addComponent(7, 6, new Component(universalSides));
        shipBoard.addComponent(7, 7, new Cabin(universalSides, CrewType.Human, 2));
        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(7, 6, true);
        });
        shipBoard.removeComponent(8, 6, true);
        assertEquals(2, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(4, shipBoard.getShipBoardAttributes().getDestroyedComponents());
        // cabin: 7 7

        // block to repeat:
        shipBoard.addComponent(6, 7, new Component(universalSides));
        shipBoard.addComponent(6, 8, new Cabin(universalSides, CrewType.Human, 2));
        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(6, 7, true);
        });
        shipBoard.removeComponent(7, 7, true);
        assertEquals(2, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(6, shipBoard.getShipBoardAttributes().getDestroyedComponents());
        // cabin: 6 8

        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void testCenterCabinRemovedBeforeFracture() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // remove center cabin, and then fracture - check if new center cabin is elected before fracture checking (mapping)
        shipBoard.addComponent(6, 7, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.removeComponent(7, 7, true);
        shipBoard.addComponent(5, 7, new Component(universalSides));
        shipBoard.addComponent(5, 8, new Cabin(universalSides, CrewType.Human, 2));
        // setup finished

        // fracture
        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(5, 7, true);
        });
        assertEquals(2, shipBoard.getShipBoardAttributes().getDestroyedComponents());
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void testFractureComponentsFallOff() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // tests if parts without crew fall off
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        // add connectors that fall off
        shipBoard.addComponent(6, 7, new Component(universalSides));
        shipBoard.addComponent(6, 6, new Component(universalSides));
        shipBoard.addComponent(6, 5, new Component(universalSides));
        shipBoard.addComponent(6, 8, new Component(universalSides));
        shipBoard.addComponent(6, 9, new Component(universalSides));
        shipBoard.addComponent(7, 6, new Component(universalSides));
        shipBoard.addComponent(7, 8, new Component(universalSides));

        assertThrows(FracturedShipBoardException.class, () -> {
            shipBoard.removeComponent(8, 7, true);
        });
        shipBoard.removeComponent(7, 7, true);
        assertEquals(9, shipBoard.getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void testCenterCabinSurroundedNoExceptionThrown() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // remove center cabin - find new center cabin
        /*
                    Connector   Cabin
        Connector   Cabin       Connector
                    Connector
         */
        // add components
        shipBoard.addComponent(6, 7, new Component(universalSides));
        shipBoard.addComponent(7, 6, new Component(universalSides));
        shipBoard.addComponent(7, 8, new Component(universalSides));
        shipBoard.addComponent(8, 7, new Component(universalSides));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));

        assertEquals(4, shipBoard.getShipBoardAttributes().getHumanCrewMembers());
        // no exception thrown
        shipBoard.removeComponent(7, 7, true);
        assertEquals(2, shipBoard.getShipBoardAttributes().getHumanCrewMembers());
        assertEquals(3, shipBoard.getShipBoardAttributes().getDestroyedComponents());

    }

    @Test
    void disconnectedPlacementException() {
        assertThrows(NotPermittedPlacementException.class, () -> {
            shipBoard.addComponent(singleConnector, 9, 9);
        });
    }

    @Test
    void testErrorCount1SmoothConnector() throws NotPermittedPlacementException, IllegalSelectionException {
        // add smooth connectors to test error count of unconnected sides
        assertEquals(0, shipBoard.getErrorCount());
        // smooth connectors + 1 cabin
        shipBoard.addComponent(smoothSidesConnector, 7, 8);
        generalViewTUI.printShipboard(shipBoard);
        assertEquals(2, shipBoard.getErrorCount());
    }

    @Test
    void testRemoveCenterCabinFromEmptyShipboard() {
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeComponent(7, 7, true);
        });
    }

    @Test
    void testCoordinatesOutOfBounds() {
        // illegal coordinates
        assertFalse(ShipBoard.checkCoordinatesInBounds(0, 1));
        assertFalse(ShipBoard.checkCoordinatesInBounds(1, 0));
        assertFalse(ShipBoard.checkCoordinatesInBounds(0, 0));
        assertFalse(ShipBoard.checkCoordinatesInBounds(-1, -1));
        assertFalse(ShipBoard.checkCoordinatesInBounds(1, -1));
        assertFalse(ShipBoard.checkCoordinatesInBounds(-1, 1));
        assertFalse(ShipBoard.checkCoordinatesInBounds(13, 1));
        assertFalse(ShipBoard.checkCoordinatesInBounds(1, 13));
        assertFalse(ShipBoard.checkCoordinatesInBounds(1, 130));
        assertFalse(ShipBoard.checkCoordinatesInBounds(100, 1));

        // legal coordinates
        assertTrue(ShipBoard.checkCoordinatesInBounds(1, 1));
        for (int i = 1; i <= shipBoard.COLS; i++) {
            for (int j = 1; j <= shipBoard.ROWS; j++) {
                assertTrue(ShipBoard.checkCoordinatesInBounds(i, j));
            }
        }
    }


    @Test
    void testRemoveCrewFromCenterCabinEmptyShipboard() throws NoHumanCrewLeftException, IllegalSelectionException {
        shipBoard.removeCrewMember(7, 7);
        assertEquals(1, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeCrewMember(7, 7);
        });
    }

    @Test
    void testEdgeIndexesWithAddComponent() {
        // visible indexes: [1, 12]
        // real indexes: [0, 11]

        // OUT OF BOUNDS: ILLEGALSELECTION

        // visible col -1, 0
        for (int i = -1; i <= 0; i++) {
            for (int j = -1; j <= 13; j++) {
                int col = i;
                int row = j;
                assertThrows(IllegalSelectionException.class, () -> {
                    shipBoard.addComponent(col, row, universalConnector);
                });
            }
        }

        // visible col 13
        for (int j = -1; j <= 13; j++) {
            int col = 13;
            int row = j;
            assertThrows(IllegalSelectionException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

        // visible row -1, 0
        for (int i = -1; i <= 0; i++) {
            for (int j = -1; j <= 13; j++) {
                int col = j;
                int row = i;
                assertThrows(IllegalSelectionException.class, () -> {
                    shipBoard.addComponent(col, row, universalConnector);
                });
            }
        }

        // visible row 13
        for (int j = -1; j <= 13; j++) {
            int col = j;
            int row = 13;
            assertThrows(IllegalSelectionException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

        // IN BOUNDS, INVALID PLACEMENTS: NOTPERMITTEDPLACEMENT
        // [1, 12]

        // visible col 1
        for (int j = 1; j <= 12; j++) {
            int col = 1;
            int row = j;
            assertThrows(NotPermittedPlacementException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

        // visible col 12
        for (int j = 1; j <= 12; j++) {
            int col = 12;
            int row = j;
            assertThrows(NotPermittedPlacementException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

        // visible row 1
        for (int j = 1; j <= 12; j++) {
            int col = j;
            int row = 1;
            assertThrows(NotPermittedPlacementException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

        // visible col 12
        for (int j = 1; j <= 12; j++) {
            int col = j;
            int row = 12;
            assertThrows(NotPermittedPlacementException.class, () -> {
                shipBoard.addComponent(col, row, universalConnector);
            });
        }

    }

    // TESTS OF GIACOMO:
    // (testing mainly ShipboardAttributes)
    @Test
    void addComponent() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleEnginePower(), 1);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleEnginePower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }

    @Test
    void testErrorCount5SmoothConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // add smooth connectors to test error count of unconnected sides
        assertEquals(0, shipBoard.getErrorCount());
        // smooth connectors + 1 cabin
        shipBoard.addComponent(smoothSidesConnector, 7, 8);
        assertEquals(2, shipBoard.getErrorCount());
        shipBoard.addComponent(smoothSidesConnector, 8, 8);
        assertEquals(3, shipBoard.getErrorCount());
        shipBoard.addComponent(smoothSidesConnector, 8, 9);
        assertEquals(4, shipBoard.getErrorCount());
        shipBoard.addComponent(smoothSidesConnector, 9, 8);
        assertEquals(5, shipBoard.getErrorCount());
        shipBoard.addComponent(smoothSidesConnector, 9, 9);
        generalViewTUI.printShipboard(shipBoard);
        assertEquals(6, shipBoard.getErrorCount());
    }

    // check connectors (Single, Double, Universal)
    @Test
    void checkConnectorCompatibleJunctions() throws NotPermittedPlacementException, IllegalSelectionException {
        // test universal, 1-1, 2-2, 3-3
        // 1 3 2
        // 1 3 2
        shipBoard.addComponent(universalConnector, 7, 8);
        shipBoard.addComponent(singleConnector, 6, 7);
        shipBoard.addComponent(singleConnector, 6, 8);
        shipBoard.addComponent(doubleConnector, 8, 7);
        shipBoard.addComponent(doubleConnector, 8, 8);
        assertFalse(shipBoard.isErroneous());

        // test single - double
        shipBoard.addComponent(doubleConnector, 5, 7);
        assertTrue(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void checkSmoothCompatibleJunctions() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
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
    void checkSpecialCompatibleJunctions() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
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

    @Test
    void checkFloatingComponent() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(universalConnector, 6, 7);
        shipBoard.addComponent(universalConnector, 8, 7);
        shipBoard.addComponent(universalConnector, 9, 7);
        assertFalse(shipBoard.isErroneous());

        shipBoard.removeComponent(8, 7, false);
        assertTrue(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void checkFloatingCabin() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(universalConnector, 6, 7);
        shipBoard.addComponent(universalConnector, 8, 7);
        shipBoard.addComponent(new Cabin(universalSides, CrewType.Human, 2), 8, 6);
        assertFalse(shipBoard.isErroneous());

        // float
        shipBoard.removeComponent(8, 7, false);
        assertTrue(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);

    }

    @Test
    void getJoinedCabinsVisibleCoordinates() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        List<int[]> coordinatesList = new ArrayList<>();
        // no connected cabins
        assertTrue(shipBoard.getJoinedCabinsVisibleCoordinates().isEmpty());

        // no connected cabins:
        // Cabin
        // Component Cabin
        shipBoard.addComponent(7, 8, new Component(universalSides));
        shipBoard.addComponent(8, 8, new Cabin(universalSides, CrewType.Human, 2));
        assertTrue(shipBoard.getJoinedCabinsVisibleCoordinates().isEmpty());

        // 3 connected cabins:
        // Ca(77) Ca(87)
        // Co     Ca(88)
        shipBoard.addComponent(8, 7, new Cabin(universalSides, CrewType.Human, 2));
        coordinatesList.add(new int[]{7, 7});
        coordinatesList.add(new int[]{8, 7});
        coordinatesList.add(new int[]{8, 8});

        for (int i = 0; i < coordinatesList.size(); i++) {
            assertArrayEquals(coordinatesList.get(i), shipBoard.getJoinedCabinsVisibleCoordinates().get(i));
        }

        // 5 connected cabins
        //        Ca
        // Ca(77) Ca(87) Ca
        // Co     Ca(88)
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        coordinatesList = new ArrayList<>();
        coordinatesList.add(new int[]{7, 7});
        coordinatesList.add(new int[]{8, 6});
        coordinatesList.add(new int[]{8, 7});
        coordinatesList.add(new int[]{8, 8});
        coordinatesList.add(new int[]{9, 7});

        for (int i = 0; i < coordinatesList.size(); i++) {
            assertArrayEquals(coordinatesList.get(i), shipBoard.getJoinedCabinsVisibleCoordinates().get(i));
        }

        //remove inhabitants from connecting cabin
        shipBoard.removeCrewMember(8, 7);
        shipBoard.removeCrewMember(8, 7);

        // remove center connecting cabin
        shipBoard.removeComponent(8, 7, false);
        assertTrue(shipBoard.getJoinedCabinsVisibleCoordinates().isEmpty());
        assertTrue(shipBoard.getJoinedCabinsVisibleCoordinates().isEmpty());

        // add connector instead
        shipBoard.addComponent(8, 7, new Component(universalSides));
        assertTrue(shipBoard.getJoinedCabinsVisibleCoordinates().isEmpty());

    }

    @Test
    void preBuildShipBoardNormalGame() {
        shipBoard.preBuildShipBoard();
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void preBuildShipBoardTestGame() {
        shipBoard = new ShipBoard(GameType.TESTGAME);

        shipBoard.preBuildShipBoard();
        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    void testAliensInTestGame() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard = new ShipBoard(GameType.TESTGAME);
        shipBoard.addComponent(7, 8, new AlienSupport(universalSides, true));
        assertThrows(IllegalStateException.class, () -> {
            shipBoard.addComponent(8, 8, new Cabin(universalSides, CrewType.Purple, 1));
        });
    }

    @Test
    void fillShipBoardWithSingleConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // check if shipboard borders are set correctly

        // col 7
        shipBoard.addComponent(singleConnector, 7, 6);
        shipBoard.addComponent(singleConnector, 7, 8);
        // col 8
        shipBoard.addComponent(singleConnector, 8, 8);
        shipBoard.addComponent(singleConnector, 8, 9);
        shipBoard.addComponent(singleConnector, 8, 7);
        shipBoard.addComponent(singleConnector, 8, 6);
        shipBoard.addComponent(singleConnector, 8, 5);
        // col 9
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(9, i, singleConnector);
        // col 10
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(10, i, singleConnector);
        // col 6
        shipBoard.addComponent(singleConnector, 6, 6);
        shipBoard.addComponent(singleConnector, 6, 5);
        shipBoard.addComponent(singleConnector, 6, 7);
        shipBoard.addComponent(singleConnector, 6, 8);
        shipBoard.addComponent(singleConnector, 6, 9);
        // col 5
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(5, i, singleConnector);
        // col 4
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(4, i, singleConnector);

        generalViewTUI.printShipboard(shipBoard);
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void fillShipBoardWithDoubleConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // check if shipboard borders are set correctly

        // col 7
        shipBoard.addComponent(doubleConnector, 7, 6);
        shipBoard.addComponent(doubleConnector, 7, 8);
        // col 8
        shipBoard.addComponent(doubleConnector, 8, 8);
        shipBoard.addComponent(doubleConnector, 8, 9);
        shipBoard.addComponent(doubleConnector, 8, 7);
        shipBoard.addComponent(doubleConnector, 8, 6);
        shipBoard.addComponent(doubleConnector, 8, 5);
        // col 9
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(9, i, doubleConnector);
        // col 10
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(10, i, doubleConnector);
        // col 6
        shipBoard.addComponent(doubleConnector, 6, 6);
        shipBoard.addComponent(doubleConnector, 6, 5);
        shipBoard.addComponent(doubleConnector, 6, 7);
        shipBoard.addComponent(doubleConnector, 6, 8);
        shipBoard.addComponent(doubleConnector, 6, 9);
        // col 5
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(5, i, doubleConnector);
        // col 4
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(4, i, doubleConnector);

        generalViewTUI.printShipboard(shipBoard);
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void fillShipBoardWithUniversalConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // check if shipboard borders are set correctly

        // col 7
        shipBoard.addComponent(universalConnector, 7, 6);
        shipBoard.addComponent(universalConnector, 7, 8);
        // col 8
        shipBoard.addComponent(universalConnector, 8, 8);
        shipBoard.addComponent(universalConnector, 8, 9);
        shipBoard.addComponent(universalConnector, 8, 7);
        shipBoard.addComponent(universalConnector, 8, 6);
        shipBoard.addComponent(universalConnector, 8, 5);
        // col 9
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(9, i, universalConnector);
        // col 10
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(10, i, universalConnector);
        // col 6
        shipBoard.addComponent(universalConnector, 6, 6);
        shipBoard.addComponent(universalConnector, 6, 5);
        shipBoard.addComponent(universalConnector, 6, 7);
        shipBoard.addComponent(universalConnector, 6, 8);
        shipBoard.addComponent(universalConnector, 6, 9);
        // col 5
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(5, i, universalConnector);
        // col 4
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(4, i, universalConnector);

        generalViewTUI.printShipboard(shipBoard);
        assertFalse(shipBoard.isErroneous());
    }

    @Test
    void fillShipBoardWithSmoothConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // check if shipboard borders are set correctly
        // check if error correction covers whole shipboard
        assertFalse(shipBoard.isErroneous());

        // col 7
        shipBoard.addComponent(smoothSidesConnector, 7, 6);
        shipBoard.addComponent(smoothSidesConnector, 7, 8);
        // col 8
        shipBoard.addComponent(smoothSidesConnector, 8, 8);
        shipBoard.addComponent(smoothSidesConnector, 8, 9);
        shipBoard.addComponent(smoothSidesConnector, 8, 7);
        shipBoard.addComponent(smoothSidesConnector, 8, 6);
        shipBoard.addComponent(smoothSidesConnector, 8, 5);
        // col 9
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(9, i, smoothSidesConnector);
        // col 10
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(10, i, smoothSidesConnector);
        // col 6
        shipBoard.addComponent(smoothSidesConnector, 6, 6);
        shipBoard.addComponent(smoothSidesConnector, 6, 5);
        shipBoard.addComponent(smoothSidesConnector, 6, 7);
        shipBoard.addComponent(smoothSidesConnector, 6, 8);
        shipBoard.addComponent(smoothSidesConnector, 6, 9);
        // col 5
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(5, i, smoothSidesConnector);
        // col 4
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(4, i, smoothSidesConnector);

        generalViewTUI.printShipboard(shipBoard);
        // check correct error count
        assertTrue(shipBoard.isErroneous());
        assertEquals(27, shipBoard.getErrorCount());
    }

    @Test
    void fillShipBoardWithSpecialConnectors() throws NotPermittedPlacementException, IllegalSelectionException {
        // check if shipboard borders are set correctly
        // check if error correction covers whole shipboard
        assertFalse(shipBoard.isErroneous());

        // col 7
        shipBoard.addComponent(specialConnector, 7, 6);
        shipBoard.addComponent(specialConnector, 7, 8);
        // col 8
        shipBoard.addComponent(specialConnector, 8, 8);
        shipBoard.addComponent(specialConnector, 8, 9);
        shipBoard.addComponent(specialConnector, 8, 7);
        shipBoard.addComponent(specialConnector, 8, 6);
        shipBoard.addComponent(specialConnector, 8, 5);
        // col 9
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(9, i, specialConnector);
        // col 10
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(10, i, specialConnector);
        // col 6
        shipBoard.addComponent(specialConnector, 6, 6);
        shipBoard.addComponent(specialConnector, 6, 5);
        shipBoard.addComponent(specialConnector, 6, 7);
        shipBoard.addComponent(specialConnector, 6, 8);
        shipBoard.addComponent(specialConnector, 6, 9);
        // col 5
        for (int i = 6; i <= 9; i++)
            shipBoard.addComponent(5, i, specialConnector);
        // col 4
        for (int i = 7; i <= 9; i++)
            shipBoard.addComponent(4, i, specialConnector);

        generalViewTUI.printShipboard(shipBoard);
        // check correct error count
        assertTrue(shipBoard.isErroneous());
        assertEquals(27, shipBoard.getErrorCount());
    }

    @Test
    void floatingObstructedCannonError() throws NotPermittedPlacementException, IllegalSelectionException {
        // Cabin Cannon(left)
        assertFalse(shipBoard.isErroneous());
        shipBoard.addComponent(8, 7, new Cannon(singleSidesSpecialLeft, true));
        assertTrue(shipBoard.isErroneous());

    }

    @Test
    void removeBattery() throws NotPermittedPlacementException, IllegalSelectionException {
        // 0Batteries Cabin 2Batteries
        // add empty battery storage
        shipBoard.addComponent(new Battery(universalSides, 0), 6, 7);
        // add battery storage with 2 batteries
        shipBoard.addComponent(new Battery(universalSides, 2), 8, 7);
        // test
        assertEquals(2, shipBoard.getShipBoardAttributes().getRemainingBatteries());
        // no batteries
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeBattery(6, 7);
        });
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeBattery(7, 7);
        });
        // 2 batteries
        shipBoard.removeBattery(8, 7);
        assertEquals(1, shipBoard.getShipBoardAttributes().getRemainingBatteries());
        shipBoard.removeBattery(8, 7);
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingBatteries());
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeBattery(8, 7);
        });
    }

    @Test
    void setCrewRemoveCrewMember() throws NotPermittedPlacementException, NoHumanCrewLeftException, IllegalSelectionException {
        // Cabin(2 humans) Cabin(1 alien) AlienSupport
        shipBoard.addComponent(new Cabin(universalSides, CrewType.Human, 2), 6, 7);
        shipBoard.addComponent(new AlienSupport(universalSides, true), 8, 7);
        shipBoard.setCrewType(7, 7, CrewType.Purple);
        assertEquals(3, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(2, shipBoard.getShipBoardAttributes().getHumanCrewMembers());
        assertTrue(shipBoard.getShipBoardAttributes().getPurpleAlien());

        // removals
        shipBoard.removeCrewMember(7, 7);
        assertFalse(shipBoard.getShipBoardAttributes().getPurpleAlien());
        shipBoard.removeCrewMember(6, 7);
        // 1 human 0 alien
        assertEquals(1, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(1, shipBoard.getShipBoardAttributes().getHumanCrewMembers());

        // exceptions:
        // not cabin selected
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeCrewMember(8, 7);
        });

        // remove last human: NoHumanCrewLeft
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeCrewMember(6, 7);
        });
        assertEquals(0, shipBoard.getShipBoardAttributes().getCrewMembers());
        assertEquals(0, shipBoard.getShipBoardAttributes().getHumanCrewMembers());

        // remove from empty alien cabin: IllegalArgument
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeCrewMember(7, 7);
        });

        // remove from empty human cabin: IllegalArgument
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeCrewMember(6, 7);
        });
    }

    @Test
    void addComponentIllegalPlacements() throws NotPermittedPlacementException, IllegalSelectionException {
        // out of bounds coordinates
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addComponent(new Component(universalSides), -1, -1);
        });
        // on existing component
        assertThrows(NotPermittedPlacementException.class, () -> {
            shipBoard.addComponent(new Component(universalSides), 7, 7);
        });
        // on invalid but connected tile 7 9
        shipBoard.addComponent(new Component(universalSides), 7, 8);
        assertThrows(NotPermittedPlacementException.class, () -> {
            shipBoard.addComponent(new Component(universalSides), 7, 9);
        });
        // on unconnected tile 5 7
        assertThrows(NotPermittedPlacementException.class, () -> {
            shipBoard.addComponent(new Component(universalSides), 5, 7);
        });
    }

    @Test
    void AddRemoveGoods() throws NotPermittedPlacementException, IllegalSelectionException {
        // 4 capacity each
        // redStorage Cabin BlueStorage
        shipBoard.addComponent(new Storage(universalSides, true, 4), 6, 7);
        shipBoard.addComponent(new Storage(universalSides, false, 4), 8, 7);
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        // adds
        shipBoard.addGoods(6, 7, new int[]{1, 1, 1, 1});
        // add red to blue
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addGoods(8, 7, new int[]{1, 0, 0, 0});
        });
        shipBoard.addGoods(8, 7, new int[]{0, 1, 1, 1});

        // add to not storage
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addGoods(7, 7, new int[]{1, 0, 0, 0});
        });
        // exceed capacity
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addGoods(6, 7, new int[]{0, 0, 0, 1});
        });
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addGoods(8, 7, new int[]{0, 0, 0, 2});
        });
        // add negative
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.addGoods(8, 7, new int[]{0, 0, 0, -1});
        });
        // red full blue 1 left
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(1, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());


        // removes
        shipBoard.removeGoods(6, 7, new int[]{1, 1, 1, 1});
        shipBoard.removeGoods(8, 7, new int[]{0, 1, 1, 1});
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());

        // exceed removes
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeGoods(6, 7, new int[]{0, 0, 0, 1});
        });
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeGoods(8, 7, new int[]{1, 0, 0, 0});
        });

        // remove negative
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.removeGoods(8, 7, new int[]{0, 0, 0, -1});
        });
    }

    @Test
    void moveGoods() throws NotPermittedPlacementException, IllegalSelectionException {
        // 4 capacity each
        // redStorage Cabin BlueStorage
        shipBoard.addComponent(new Storage(universalSides, true, 4), 6, 7);
        shipBoard.addComponent(new Storage(universalSides, false, 4), 8, 7);
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        // add blues
        shipBoard.addGoods(8, 7, new int[]{0, 1, 1, 2});
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        // fail move - not enough goods at start
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.moveGoods(8, 7, 6, 7, new int[]{0, 1, 1, 3});
        });
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        // move all blues to red
        shipBoard.moveGoods(8, 7, 6, 7, new int[]{0, 1, 1, 2});
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingRedSlots());

        // move red from red to blue
        shipBoard.removeGoods(6, 7, new int[]{0, 1, 1, 2});
        shipBoard.addGoods(6, 7, new int[]{1, 1, 1, 1});
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        // fail move
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.moveGoods(6, 7, 8, 7, new int[]{1, 1, 1, 1});
        });
        assertEquals(4, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        assertEquals(0, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
        // move blues
        shipBoard.moveGoods(6, 7, 8, 7, new int[]{0, 1, 1, 1});
        assertEquals(1, shipBoard.getShipBoardAttributes().getRemainingBlueSlots());
        assertEquals(3, shipBoard.getShipBoardAttributes().getRemainingRedSlots());
    }

    @Test
    void checkEngineErrors() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // Connector Engine (front, back, both erroneous)
        // Cabin Engine (back)

        // valid engine (backwards unobstructed)
        assertFalse(shipBoard.isErroneous());
        shipBoard.addComponent(new Component(universalSides), 7, 6);
        shipBoard.addComponent(new Engine(singleSidesSpecialBack, false), 8, 7);
        assertFalse(shipBoard.isErroneous());
        // wrong engine (obstructed)
        shipBoard.addComponent(new Engine(singleSidesSpecialBack, false), 8, 6);
        assertTrue(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);
        // remove wrong engine
        shipBoard.removeComponent(8, 6, false);
        assertFalse(shipBoard.isErroneous());
        // wrong engine (not backwards)
        shipBoard.addComponent(new Engine(singleSidesSpecialFront, false), 8, 6);
        assertTrue(shipBoard.isErroneous());

    }

    @Test
    void checkCannonError() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        // obstructed cannons
        //           Connector Cannon (left, back)
        // Connector Cabin     Connector
        // Cannon    Connector
        // (front, right)

        // add connectors
        shipBoard.addComponent(universalConnector, 7, 6);
        shipBoard.addComponent(universalConnector, 6, 7);
        shipBoard.addComponent(universalConnector, 8, 7);
        shipBoard.addComponent(universalConnector, 7, 8);
        assertFalse(shipBoard.isErroneous());
        // add top right
        // back
        shipBoard.addComponent(new Cannon(singleSidesSpecialBack, true), 8, 6);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(8, 6, false);
        assertFalse(shipBoard.isErroneous());
        // left
        shipBoard.addComponent(new Cannon(singleSidesSpecialLeft, true), 8, 6);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(8, 6, false);
        assertFalse(shipBoard.isErroneous());
        // front
        shipBoard.addComponent(new Cannon(singleSidesSpecialFront, true), 8, 6);
        assertFalse(shipBoard.isErroneous());
        shipBoard.removeComponent(8, 6, false);
        // right
        shipBoard.addComponent(new Cannon(singleSidesSpecialRight, true), 8, 6);
        assertFalse(shipBoard.isErroneous());
        // add bottom left
        // front
        shipBoard.addComponent(new Cannon(singleSidesSpecialFront, true), 6, 8);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(6, 8, false);
        assertFalse(shipBoard.isErroneous());
        // right
        shipBoard.addComponent(new Cannon(singleSidesSpecialRight, true), 6, 8);
        assertTrue(shipBoard.isErroneous());
        shipBoard.removeComponent(6, 8, false);
        assertFalse(shipBoard.isErroneous());
        // back
        shipBoard.addComponent(new Cannon(singleSidesSpecialBack, true), 6, 8);
        assertFalse(shipBoard.isErroneous());
        shipBoard.removeComponent(6, 8, false);
        // right
        shipBoard.addComponent(new Cannon(singleSidesSpecialLeft, true), 6, 8);
        assertFalse(shipBoard.isErroneous());
        generalViewTUI.printShipboard(shipBoard);

        assertEquals(1, shipBoard.getShipBoardAttributes().getSingleCannonPower());

    }

    @Test
    void noHumanCrewLeftException() throws NotPermittedPlacementException, NoHumanCrewLeftException, IllegalSelectionException {
        shipBoard.addComponent(new AlienSupport(universalSides, true), 7, 8);
        assertThrows(IllegalSelectionException.class, () -> {
            shipBoard.setCrewType(7, 7, CrewType.Purple);
        });
        shipBoard.removeCrewMember(7, 7);
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeCrewMember(7, 7);
        });
        assertThrows(NoHumanCrewLeftException.class, () -> {
            shipBoard.removeComponent(7, 7, false);
        });
    }

    @Test
    void addComponent2() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleCannonPower(), 1);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getSingleCannonPower(), 0);
    }

    @Test
    void addComponent3() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Shield(singleSides, 1, 2), 7, 8);
        shipBoard.addComponent(8, 7, new Battery(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single}, 2));
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(1), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(2), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(3), false);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(1), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(2), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSideShieldProtected(3), false);
    }

    @Test
    void addComponent4() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}, CrewType.Human, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 2);
    }

    @Test
    void addComponent5() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBatteries(), 2);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBatteries(), 0);
    }

    @Test
    void addComponent6() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 20);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingRedSlots(), 0);
    }

    @Test
    void addComponent7() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, false, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 20);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 0);
    }

    @Test
    void addComponent8() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, CrewType.Human, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.addComponent(new AlienSupport(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true), 8, 8);
        shipBoard.setCrewType(7, 8, CrewType.Purple);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 3);
        shipBoard.removeComponent(7, 8, false);
        shipBoard.removeComponent(8, 8, false);
        assertEquals(shipBoard.getShipBoardAttributes().getRemainingBlueSlots(), 0);
    }

    @Test
    void CountExternalJunctions() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException {
        assertEquals(shipBoard.countExternalJunctions(), 4);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.countExternalJunctions(), 6);
        shipBoard.removeComponent(7, 8, false);
        assertEquals(shipBoard.countExternalJunctions(), 4);
    }

    @Test
    void testError() throws NotPermittedPlacementException, NoHumanCrewLeftException, FracturedShipBoardException, IllegalSelectionException { //Correct junctions
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
