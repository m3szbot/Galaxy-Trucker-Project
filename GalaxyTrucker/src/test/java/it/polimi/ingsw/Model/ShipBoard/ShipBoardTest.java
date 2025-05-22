package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.GeneralView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//already tested: addComponent, removeComponent, goDownChecking, isCompatible, countExternalJunctions, setCrewType, checkNotReachable
//remaining methods: checkErrors, addGoods, checkSlotsú

// use Shipboard graphic to construct test shipboards!

public class ShipBoardTest {
    ShipBoard shipBoard;

    // view to print shipboard to debug
    GeneralView generalViewTUI = new GeneralView();

    // components
    SideType[] smoothSidesUniversalRight = new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth};
    SideType[] singleSides = new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single};
    SideType[] doubleSides = new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double};
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] specialSidesUniversalRight = new SideType[]{SideType.Special, SideType.Universal, SideType.Special, SideType.Special};
    Component smoothRightUniversal = new Component(smoothSidesUniversalRight);
    Component singleConnector = new Component(singleSides);
    Component doubleConnector = new Component(doubleSides);
    Component universalConnector = new Component(universalSides);
    Component specialRightUniversal = new Component(specialSidesUniversalRight);


    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(GameType.NORMALGAME);
    }

    @Test
    void TestSetup() {
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

    @Test
    void addComponent() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }


    @Test
    void addComponent2() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 0);
    }

    @Test
    void addComponent3() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Shield(new SideType[]{SideType.Universal, SideType.Special, SideType.Special, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(0), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(1), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(2), false);
        assertEquals(shipBoard.getShipBoardAttributes().checkSide(3), false);
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
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(), 2);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(), 0);
    }

    @Test
    void addComponent6() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 0);
    }

    @Test
    void addComponent7() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, false, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
    }

    @Test
    void addComponent8() throws NotPermittedPlacementException {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.addComponent(new AlienSupport(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true), 8, 8);
        shipBoard.setCrewType(CrewType.Purple, 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 3);
        assertEquals(shipBoard.getShipBoardAttributes().getAlienType(), 2);
        shipBoard.removeComponent(7, 8, true);
        shipBoard.removeComponent(8, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
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
        int errors;
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        assertTrue(shipBoard.isErroneous());

        assertTrue(shipBoard.getErrorsMatrix()[7][5]);
        assertTrue(shipBoard.getErrorsMatrix()[7][6]);
        assertTrue(shipBoard.getErrorsMatrix()[7][6]);
        shipBoard.addComponent(new Storage(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, true, 4), 8, 7);
        printAsciiBoard(shipBoard);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 4);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
        shipBoard.removeComponent(8, 7, false);
        shipBoard.removeComponent(8, 8, false);
        assertTrue(shipBoard.isErroneous());
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 2);
    }


    /**
     * Stampa la griglia 12×12 con bordi, nome del componente (3 car.)
     * al centro e valori numerici dei lati.
     * <p>
     * Layout di ogni cella (7×5):
     * +-------+
     * |   F   |
     * |L NNN R|
     * |   B   |
     * +-------+
     * <p>
     * Forbidden  = "XXXXXXX"
     * Empty cell = spazi vuoti
     */
    public void printAsciiBoard(ShipBoard shipBoard) {

        final int SIZE = 12;
        final String HEDGE = "+-------";    // bordo orizzontale di una cella
        final String HRULE = HEDGE.repeat(SIZE) + "+";
        System.out.print("    ");            // spazio per intestazione righe / colonne
        for (int c = 1; c <= SIZE; c++) {
            System.out.printf("   %2d   ", c);
        }
        System.out.println();

        for (int r = 0; r < SIZE; r++) {

            // ---------- bordo superiore della riga ----------------------------
            System.out.print("    ");
            System.out.println(HRULE);

            // ***** riga “front” ***********************************************
            System.out.printf("%2d  ", r + 1);
            for (int c = 0; c < SIZE; c++) {
                if (!shipBoard.getValidityMatrix()[r][c]) {                    // forbidden 
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getComponentMatrix()[r][c];
                String front = comp != null ? num(side(comp.getFront())) : " ";
                System.out.print("|   " + front + "   ");
            }
            System.out.println("|");

            // ***** riga centrale (left, name, right) ***************************
            System.out.print("    ");                 // niente indice riga ora
            for (int c = 0; c < SIZE; c++) {
                if (!shipBoard.getValidityMatrix()[r][c]) {
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getComponentMatrix()[r][c];
                if (comp == null) {
                    System.out.print("|       ");
                } else {
                    String name = pad(comp.getComponentName(), 3);
                    String left = num(side(comp.getLeft()));
                    String right = num(side(comp.getRight()));
                    System.out.print("|" + left + " " + name + " " + right);
                }
            }
            System.out.println("|");

            // ***** riga “back” *************************************************
            System.out.print("    ");
            for (int c = 0; c < SIZE; c++) {
                if (!shipBoard.getValidityMatrix()[r][c]) {
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getComponentMatrix()[r][c];
                String back = comp != null ? num(side(comp.getBack())) : " ";
                System.out.print("|   " + back + "   ");
            }
            System.out.println("|");
        }

        // ---------- bordo inferiore finale ------------------------------------
        System.out.print("    ");
        System.out.println(HRULE);
    }

    /* -------------------------------------------------------------------------
     * Helpers
     * ---------------------------------------------------------------------- */

    /**
     * Converte il numero in stringa a 1 char, spazio se 0 o -1.
     */
    private String num(int n) {
        return (n <= 0) ? " " : Integer.toString(n);
    }

    /**
     * Mappa SideType in numero intero: Single=1, Double=2, Universal=3,
     * Special=9, Smooth=0, null=- 
     */
    private int side(SideType s) {
        if (s == null) return -1;
        switch (s) {
            case Single:
                return 1;
            case Double:
                return 2;
            case Universal:
                return 3;
            case Special:
                return 9;
            case Smooth:
                return 0;
            default:
                return -1;
        }
    }

    /**
     * Rende una stringa lunga esattamente len, tagliando o padding con spazi.
     */
    private String pad(String s, int len) {
        if (s == null) s = "";
        return s.length() >= len ? s.substring(0, len)
                : String.format("%-" + len + "s", s);
    }

}
