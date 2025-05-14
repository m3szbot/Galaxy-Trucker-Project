package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


//already tested: addComponent, removeComponent, goDownChecking, isCompatible, countExternalJunctions, setCrewType, checkNotReachable
//remaining methods: checkErrors, addGoods, checkSlots

public class ShipBoardTest {
    ShipBoard shipBoard;

    @BeforeEach
    void setUp() {
        shipBoard = new ShipBoard(GameType.NORMALGAME);
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(shipBoard.getMatr()[i][j] + "");
            }
            System.out.println();
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                System.out.print(shipBoard.getStructureMatrix()[i][j] + "");
            }
            System.out.println();
        }
    }

    @Test
    void addComponent() {
        shipBoard.addComponent(new Engine(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.SPECIAL, SideType.UNIVERSAL}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getDrivingPower(), 0);
        assertEquals(shipBoard.getShipBoardAttributes().getDestroyedComponents(), 1);

    }


    @Test
    void addComponent2() {
        shipBoard.addComponent(new Cannon(new SideType[]{SideType.SPECIAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, true), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 1);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getFirePower(), 0);
    }

    @Test
    void addComponent3() {
        shipBoard.addComponent(new Shield(new SideType[]{SideType.UNIVERSAL, SideType.SPECIAL, SideType.SPECIAL, SideType.UNIVERSAL}), 7, 8);
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
    void addComponent4() {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.UNIVERSAL, SideType.SPECIAL, SideType.SPECIAL, SideType.UNIVERSAL}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 2);
    }

    @Test
    void addComponent5() {
        shipBoard.addComponent(new Battery(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, 2), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(), 2);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getBatteryPower(), 0);
    }

    @Test
    void addComponent6() {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, true, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 0);
    }

    @Test
    void addComponent7() {
        shipBoard.addComponent(new Storage(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, false, 20), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 20);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
    }

    @Test
    void addComponent8() {
        shipBoard.addComponent(new Cabin(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}), 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 4);
        shipBoard.addComponent(new AlienSupport(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, true), 8, 8);
        shipBoard.setCrewType(CrewType.PURPLE, 7, 8);
        assertEquals(shipBoard.getShipBoardAttributes().getCrewMembers(), 3);
        assertEquals(shipBoard.getShipBoardAttributes().getAlienType(), 2);
        shipBoard.removeComponent(7, 8, true);
        shipBoard.removeComponent(8, 8, true);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
    }

    @Test
    void CountExternalJunctions() {
        assertEquals(shipBoard.countExternalJunctions(), 4);
        shipBoard.addComponent(new Component(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}), 7, 8);
        assertEquals(shipBoard.countExternalJunctions(), 6);
        shipBoard.removeComponent(7, 8, true);
        assertEquals(shipBoard.countExternalJunctions(), 4);
    }

    @Test
    void testError() { //Correct junctions
        int errors;
        shipBoard.addComponent(new Component(new SideType[]{SideType.UNIVERSAL, SideType.SINGLE, SideType.SMOOTH, SideType.DOUBLE}), 7, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.UNIVERSAL, SideType.SINGLE, SideType.SMOOTH, SideType.DOUBLE}), 6, 8);
        shipBoard.addComponent(new Component(new SideType[]{SideType.UNIVERSAL, SideType.SINGLE, SideType.SMOOTH, SideType.DOUBLE}), 8, 8);
        errors = shipBoard.checkErrors();
        assertEquals(errors, 3);

        assertTrue(shipBoard.getMatrErrors()[7][5]);
        assertTrue(shipBoard.getMatrErrors()[7][6]);
        assertTrue(shipBoard.getMatrErrors()[7][6]);
        shipBoard.addComponent(new Storage(new SideType[]{SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL, SideType.UNIVERSAL}, true, 4), 8, 7);
        printAsciiBoard(shipBoard);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableRedSlots(), 4);
        assertEquals(shipBoard.getShipBoardAttributes().getAvailableBlueSlots(), 0);
        shipBoard.removeComponent(8, 7, false);
        shipBoard.removeComponent(8, 8, false);
        errors = shipBoard.checkErrors();
        assertEquals(errors, 2);
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
                if (!shipBoard.getMatr()[r][c]) {                    // forbidden 
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getStructureMatrix()[r][c];
                String front = comp != null ? num(side(comp.getFront())) : " ";
                System.out.print("|   " + front + "   ");
            }
            System.out.println("|");

            // ***** riga centrale (left, name, right) ***************************
            System.out.print("    ");                 // niente indice riga ora
            for (int c = 0; c < SIZE; c++) {
                if (!shipBoard.getMatr()[r][c]) {
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getStructureMatrix()[r][c];
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
                if (!shipBoard.getMatr()[r][c]) {
                    System.out.print("|XXXXXXX");
                    continue;
                }
                Component comp = shipBoard.getStructureMatrix()[r][c];
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
     * Mappa SideType in numero intero: SINGLE=1, DOUBLE=2, UNIVERSAL=3,
     * SPECIAL=9, SMOOTH=0, null=- 
     */
    private int side(SideType s) {
        if (s == null) return -1;
        switch (s) {
            case SINGLE:
                return 1;
            case DOUBLE:
                return 2;
            case UNIVERSAL:
                return 3;
            case SPECIAL:
                return 9;
            case SMOOTH:
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
