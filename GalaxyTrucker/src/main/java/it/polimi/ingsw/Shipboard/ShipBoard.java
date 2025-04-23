package it.polimi.ingsw.Shipboard;

import it.polimi.ingsw.Components.*;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Application.*;

import java.util.List;

public class ShipBoard {
    // Matrix representing the ship's component layout
    private Component[][] structureMatrix;
    // Boolean matrix indicating valid positions for components
    private boolean[][] matr;
    private ShipBoardAttributes shipBoardAttributes;
    private boolean[][] matrErrors;

    public Component[][] getStructureMatrix() {
        return structureMatrix;
    }
    public boolean[][] getMatr() {
        return matr;
    }
    public boolean[][] getMatrErrors() {
        return matrErrors;
    }


    /**
     * Constructs a ShipStructure instance.
     * Initializes the ship's structure matrix and determines valid component placement
     * based on the specified game type.
     * <p>
     * - Initializes a 12x12 matrix for ship components.
     * - Marks all positions as valid initially.
     * - If the game type is not "TestGame", restricts specific areas of the matrix
     * by marking them as forbidden zones.
     *
     * @param gameType The type of game being played.
     * @author Giacomo
     */
    public ShipBoard(GameType gameType) {
        this.structureMatrix = new Component[12][12];
        this.matr = new boolean[12][12];
        this.matrErrors = new boolean[12][12];
        this.shipBoardAttributes = new ShipBoardAttributes();
        // Initialize all positions as valid
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matr[i][j] = true;
            }
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                structureMatrix[i][j] = null;
            }
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                matrErrors[i][j] = false;
            }
        }

        if (gameType.equals(GameType.TestGame)) {
        } else {
            // Set forbidden zones in the structure
            for (int i = 0; i < 12; i++) {
                matr[i][0] = false;
                matr[i][1] = false;
                matr[i][2] = false;
                matr[i][10] = false;
                matr[i][11] = false;

            }
            for (int i = 0; i < 12; i++) {
                matr[0][i] = false;
                matr[1][i] = false;
                matr[2][i] = false;
                matr[3][i] = false;
                matr[9][i] = false;
                matr[10][i] = false;
                matr[11][i] = false;
            }

            matr[4][3] = false;
            matr[4][4] = false;
            matr[5][3] = false;
            matr[4][6] = false;
            matr[8][6] = false;
            matr[4][8] = false;
            matr[4][9] = false;
            matr[5][9] = false;
        }

        addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 7);
    }

    public Component getComponent(int x, int y){
        return structureMatrix[x][y];
    }

    public int getMatrixRows(){
       return structureMatrix.length;
    }

    public int getMatrixCols(){
        return structureMatrix[0].length;
    }

    public ShipBoardAttributes getShipBoardAttributes() {
        return shipBoardAttributes;
    }

    /**
     * Adds a component to the specified position in the structure matrix.
     *
     * @param component The component to add.
     * @param x         The x-coordinate of the component.
     * @param y         The y-coordinate of the component.
     * @author Giacomo
     */
    public void addComponent(Component component, int x, int y) {
        x = x - 1;
        y = y - 1;
        Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
        if (matr[y][x] == true) {
            structureMatrix[y][x] = component;
        }
        //qua devo fare l'aggiunta degli indici con un metodo add che aggiorni tutti gli indici
        List<Object> list = component.accept(visitor);
        if ((Integer) list.get(0) == 1){
            shipBoardAttributes.updateDrivingPower((Integer) list.get(0));
        }else if ((Integer) list.get(0) == 2){
            shipBoardAttributes.updateNumberDoubleEngines(1);
        }
        if ((Float) list.get(1) == 1) {
            shipBoardAttributes.updateFirePower((Float) list.get(1));
        }
        else if ((Float) list.get(1) == 2) {
            if(component.getFront().equals(SideType.Special)){
                shipBoardAttributes.updateNumberForwardDoubleCannons(1);
            }
            else{
                shipBoardAttributes.updateNumberNotForwardDoubleCannons(1);
            }
        }
        shipBoardAttributes.updateCrewMembers((Integer) list.get(2));
        shipBoardAttributes.updateBatteryPower((Integer) list.get(3));
        boolean[] sides = (boolean[]) list.get(4);
        for (int i = 0; i < 4; i++) {
            shipBoardAttributes.updateCoveredSides(i, sides[i], true);
        }
        shipBoardAttributes.updateAvailableSlots(1, (Integer) list.get(5));
        shipBoardAttributes.updateAvailableSlots(2, (Integer) list.get(6));
    }

    /**
     * Removes a component from the specified position.
     * Updates the shipBoard to reflect the destroyed components.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @author Giacomo
     */
    public void removeComponent(int x, int y, boolean checkTrigger) {
        boolean flag = true;
        x = x - 1;
        y = y - 1;

        if (matr[y][x] == true && structureMatrix[y][x] != null) {
            Component component = structureMatrix[y][x];
            Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
            List<Object> list = component.accept(visitor);
            shipBoardAttributes.updateDestroyedComponents(1);
            shipBoardAttributes.updateDrivingPower(-(Integer) list.get(0));
            if ((Float) list.get(1) == 1) {
                shipBoardAttributes.updateFirePower(-(Float) list.get(1));
            } else if ((Float) list.get(1) == 2) {
                if(component.getFront().equals(SideType.Special)){
                    shipBoardAttributes.updateNumberForwardDoubleCannons(-1);
                }
                else{
                    shipBoardAttributes.updateNumberNotForwardDoubleCannons(-1);
                }
            }
            shipBoardAttributes.updateCrewMembers(-(Integer) list.get(2));
            shipBoardAttributes.updateBatteryPower(-(Integer) list.get(3));
            boolean[] sides = (boolean[]) list.get(4);
            for (int i = 0; i < 4; i++) {
                shipBoardAttributes.updateCoveredSides(i, sides[i], false);
            }
            shipBoardAttributes.updateAvailableSlots(1, -(Integer) list.get(5));
            shipBoardAttributes.updateAvailableSlots(2, -(Integer) list.get(6));
            if((Boolean) list.get(8) == true){
                int[] update = ((Storage) component).getGoods();
                update[0] = -update[0];
                update[1] = -update[1];
                update[2] = -update[2];
                update[3] = -update[3];
                shipBoardAttributes.updateGoods(update);
            }
            structureMatrix[y][x] = null;
            if (checkTrigger) {
                while (checkNotReachable(this.shipBoardAttributes));
            }
        }
    }

    /**
     * Counts the number of external junctions in the ship structure.
     * External junctions occur when a component has a connection point
     * facing an empty space.
     *
     * @return The number of external junctions.
     * @author Giacomo
     */
    public int countExternalJunctions() {
        int externalJunctions = 0;
        for (int i = 1; i < 11; i++) {
            for (int j = 1; j < 11; j++) {
                if (structureMatrix[i][j] != null) {
                    //va sistemato il fatto che qualora si volesse davvero usare un enum allora dovrebbe essere messo tipodiverso da vuoto e diverso da shield
                    if ((!structureMatrix[i][j].getLeft().equals(SideType.Smooth) && structureMatrix[i - 1][j] == null)) {
                        externalJunctions++;
                    }
                    if ((!structureMatrix[i][j].getRight().equals(SideType.Smooth) && structureMatrix[i + 1][j] == null)) {
                        externalJunctions++;
                    }
                    if ((!structureMatrix[i][j].getFront().equals(SideType.Smooth) && structureMatrix[i][j - 1] == null)) {
                        externalJunctions++;
                    }
                    if ((!structureMatrix[i][j].getBack().equals(SideType.Smooth) && structureMatrix[i][j + 1] == null)) {
                        externalJunctions++;
                    }
                }
            }
        }
        return externalJunctions;
    }

    /**
     * Scans the ship structure to identify errors related to incorrect junctions.
     * Errors are detected and counted but not automatically corrected.
     * <p>
     * The function iterates through the structure matrix and:
     * 1. Verifies if components are correctly connected.
     * 2. Checks if the "Engine" component is incorrectly placed.
     * 3. Ensures "Cannon" components follow specific placement rules.
     *
     * @return The total number of detected errors.
     * @author Giacomo
     */
    public int checkErrors() {
        boolean flag = true;
        int errors = 0;
        Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
            for (int i = 1; i < 12; i++) {
                for (int j = 1; j < 12; j++) {
                    if (structureMatrix[i][j] != null) {
                        if (!checkCorrectJunctions(i, j)) {
                            System.out.println("Component" + (j+1) + " " + (i+1) + " is not well connected");
                            matrErrors[i][j] = true;
                            errors++;
                        }
                        if ((Integer) structureMatrix[i][j].accept(visitor).get(0) > 0) {
                            if (!structureMatrix[i][j].getBack().equals(SideType.Special)) {
                                matrErrors[i][j] = true;
                                errors++;
                            } else {
                                boolean check = false;
                                for (int k = i + 1; k < 12; k++) {
                                    if (structureMatrix[k][j] != null) {
                                        check = true;
                                        errors++;
                                    }
                                }
                                if (check) {
                                    System.out.println("Error, in component" + i + ' ' + j);
                                    matrErrors[i][j] = true;
                                }
                            }
                        }
                        if ((Float) structureMatrix[i][j].accept(visitor).get(1) > 0) {
                            boolean check = false;
                            if (structureMatrix[i][j].getLeft().equals(SideType.Special)) {

                                if (structureMatrix[i][j-1] != null) {
                                    check = true;
                                    matrErrors[i][j] = true;
                                    errors++;
                                }

                                if (check) {
                                    System.out.println("Error, in component" + i + ' ' + j);
                                }
                            } else if (structureMatrix[i][j].getRight().equals(SideType.Special)) {

                                if (structureMatrix[i][j+1] != null) {
                                    check = true;
                                    matrErrors[i][j] = true;
                                    errors++;
                                }

                                if (check) {
                                    System.out.println("Error, in component" + i + ' ' + j);
                                }
                            } else if (structureMatrix[i][j].getFront().equals(SideType.Special)) {
                                if (structureMatrix[i-1][j] != null) {
                                    check = true;
                                    matrErrors[i][j] = true;
                                    errors++;
                                }
                                if (check) {
                                    System.out.println("Error, in component" + i + ' ' + j);
                                }
                            } else if (structureMatrix[i][j].getBack().equals(SideType.Special)) {
                                if (structureMatrix[i+1][j] != null) {
                                    check = true;
                                    matrErrors[i][j] = true;
                                    errors++;
                                }
                                if (check) {
                                    System.out.println("Error, in component" + i + ' ' + j);
                                }
                            }
                        }
                    }
                }
            }
        return errors;
    }

    /**
     * Checks if any components are not connected to the main structure.
     * If a component is unreachable, it is removed.
     *
     * @param shipBoardAttributes The ShipBoard object that tracks destroyed components.
     * @return True if any unreachable components were found, false otherwise.
     * @author Giacomo
     */
    public boolean checkNotReachable(ShipBoardAttributes shipBoardAttributes) {
        boolean result = false;
        int flag = 1;
        boolean[][] mat = new boolean[12][12];
        while (flag == 1) {
            flag = 0;
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    mat[i][j] = false;
                }
            }

            goDownChecking(6, 6, mat);
            for (int i = 0; i < 12; i++) {
                for (int j = 0; j < 12; j++) {
                    if (structureMatrix[i][j] != null) {
                        if (!mat[i][j]) {
                            flag = 1;
                            result = true;
                            removeComponent(i+1, j+1, true);
                            shipBoardAttributes.updateDestroyedComponents(1);
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Recursively marks reachable components.
     *
     * @param x   The x-coordinate.
     * @param y   The y-coordinate.
     * @param mat The boolean matrix tracking visited positions.
     * @author Giacomo
     */
    private void goDownChecking(int x, int y, boolean[][] mat) {
        if (x < 0 || x >= 12 || y < 0 || y >= 12) return;
        if (structureMatrix[x][y] == null || mat[x][y]) return;
        mat[x][y] = true;
        // Down
        if (y + 1 < 12 && structureMatrix[x][y+1] != null &&
                isCompatible(structureMatrix[x][y].getRight(), structureMatrix[x][y+1].getLeft())) {
            goDownChecking(x, y+1, mat);
        }

        // Up
        if (y - 1 >= 0 && structureMatrix[x][y-1] != null &&
                isCompatible(structureMatrix[x][y].getLeft(), structureMatrix[x][y-1].getRight())) {
            goDownChecking(x , y-1, mat);
        }

        // Right
        if (x + 1 < 12 && structureMatrix[x+1][y] != null &&
                isCompatible(structureMatrix[x][y].getBack(), structureMatrix[x+1][y].getFront())) {
            goDownChecking(x+1, y , mat);
        }

        // Left
        if (x - 1 >= 0 && structureMatrix[x-1][y] != null &&
                isCompatible(structureMatrix[x][y].getFront(), structureMatrix[x-1][y].getBack())) {
            goDownChecking(x-1, y, mat);
        }
    }

    private boolean isCompatible(SideType a, SideType b) {
        return (a == SideType.Single && (b == SideType.Single || b == SideType.Universal)) ||
                (a == SideType.Double && (b == SideType.Double || b == SideType.Universal));
    }

    /**
     * Checks if a component has correct junctions with its neighboring components.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @return True if the junctions are correct, false otherwise.
     * @author Giacomo
     */
    private boolean checkCorrectJunctions(int x, int y) {
        System.out.println("Checking: " + (y+1)   + ", " + (x+1));
        if (structureMatrix[x][y] != null) {


            if ((structureMatrix[x][y].getLeft().equals(SideType.Single) && structureMatrix[x][y-1] != null &&
                    (!structureMatrix[x][y-1].getRight().equals(SideType.Single) && !structureMatrix[x][y-1].getRight().equals(SideType.Universal))) ||

                    (structureMatrix[x][y].getLeft().equals(SideType.Double) && structureMatrix[x][y-1] != null &&
                            (!structureMatrix[x][y-1].getRight().equals(SideType.Double) && !structureMatrix[x][y-1].getRight().equals(SideType.Universal))) ||


                    (structureMatrix[x][y].getFront().equals(SideType.Double) && structureMatrix[x-1][y] != null &&
                            (!structureMatrix[x-1][y].getBack().equals(SideType.Double) && !structureMatrix[x-1][y].getBack().equals(SideType.Universal))) ||

                    (structureMatrix[x][y].getFront().equals(SideType.Single) && structureMatrix[x-1][y] != null &&
                            (!structureMatrix[x-1][y].getBack().equals(SideType.Single) && !structureMatrix[x-1][y].getBack().equals(SideType.Universal))) ||


                    (structureMatrix[x][y].getRight().equals(SideType.Single) && structureMatrix[x][y + 1] != null &&
                            (!structureMatrix[x][y + 1].getLeft().equals(SideType.Single) && !structureMatrix[x][y + 1].getLeft().equals(SideType.Universal))) ||

                    (structureMatrix[x][y].getRight().equals(SideType.Double) && structureMatrix[x][y + 1] != null &&
                            (!structureMatrix[x][y + 1].getLeft().equals(SideType.Double) && !structureMatrix[x][y + 1].getLeft().equals(SideType.Universal))) ||


                    (structureMatrix[x][y].getBack().equals(SideType.Single) && structureMatrix[x + 1][y] != null &&
                            (!structureMatrix[x + 1][y].getFront().equals(SideType.Single) && !structureMatrix[x + 1][y].getFront().equals(SideType.Universal))) ||

                    (structureMatrix[x][y].getBack().equals(SideType.Double) && structureMatrix[x + 1][y] != null &&
                            (!structureMatrix[x + 1][y].getFront().equals(SideType.Double) && !structureMatrix[x + 1][y].getFront().equals(SideType.Universal)))
            ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets the crew type in a cabin component, ensuring compatibility with alien support components.
     *
     * @param crewType The type of crew to assign.
     * @param x        The x-coordinate of the cabin.
     * @param y        The y-coordinate of the cabin.
     * @author Giacomo
     */
    public void setCrewType(CrewType crewType, int x, int y) {
        x = x - 1;
        y = y - 1;
        Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
        if (structureMatrix[y][x] != null && structureMatrix[y][x].getComponentName().equals("Cabin")) {
            if (crewType.equals(CrewType.Brown)) {
                if (( structureMatrix[y-1][x] != null && (Boolean) structureMatrix[y - 1][x].accept(visitor).get(7) && !((AlienSupport) structureMatrix[y - 1][x]).isPurple()) ||
                        (structureMatrix[y+1][x] != null && (Boolean) structureMatrix[y + 1][x].accept(visitor).get(7) && !((AlienSupport) structureMatrix[y + 1][x]).isPurple()) ||
                        (structureMatrix[y][x-1] != null && (Boolean) structureMatrix[y][x - 1].accept(visitor).get(7) && !((AlienSupport) structureMatrix[y][x - 1]).isPurple()) ||
                        (structureMatrix[y][x+1] != null && (Boolean) structureMatrix[y][x + 1].accept(visitor).get(7) && !((AlienSupport) structureMatrix[y][x + 1]).isPurple())) {
                    ((Cabin) structureMatrix[y][x]).setCrewType(crewType);
                    shipBoardAttributes.updateAlien(CrewType.Brown, false);
                    shipBoardAttributes.updateCrewMembers(-1);
                } else {
                    System.out.println("CrewType not permitted");
                }
            } else {
                if ((structureMatrix[y-1][x] != null && (Boolean) structureMatrix[y - 1][x].accept(visitor).get(7) && ((AlienSupport) structureMatrix[y - 1][x]).isPurple()) ||
                        (structureMatrix[y+1][x] != null && (Boolean) structureMatrix[y + 1][x].accept(visitor).get(7) && ((AlienSupport) structureMatrix[y + 1][x]).isPurple()) ||
                        (structureMatrix[y][x-1] != null && (Boolean) structureMatrix[y][x-1].accept(visitor).get(7) && ((AlienSupport) structureMatrix[y][x - 1]).isPurple()) ||
                        (structureMatrix[y][x+1] != null && (Boolean) structureMatrix[y][x+1].accept(visitor).get(7) && ((AlienSupport) structureMatrix[y][x + 1]).isPurple())) {
                    ((Cabin) structureMatrix[y][x]).setCrewType(crewType);
                    shipBoardAttributes.updateAlien(CrewType.Purple, false);
                    shipBoardAttributes.updateCrewMembers(-1);
                } else {
                    System.out.println("CrewType not permitted");
                }
            }
        } else {
            System.out.println("ERROR, this component is not a cabin");
        }
    }

    /**
     * Checks if the storage component at a given position has enough space for the goods.
     *
     * @param goods The array representing the goods to be checked.
     * @param x     The x-coordinate of the storage component.
     * @param y     The y-coordinate of the storage component.
     * @return True if the goods fit, false otherwise.
     * @author Giacomo
     */
    private boolean checkSlots(int[] goods, int x, int y) {
        if (structureMatrix[x][y] != null) {
            if (((Storage) structureMatrix[x][y]).isRed()) {
                if (goods[0] <= ((Storage) structureMatrix[x][y]).getNumberOfMaximumElements() - ((Storage) structureMatrix[x][y]).getGoods()[0]) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (goods[0] <= ((Storage) structureMatrix[x][y]).getNumberOfMaximumElements() - ((Storage) structureMatrix[x][y]).getGoods()[0] - ((Storage) structureMatrix[x][y]).getGoods()[3] - ((Storage) structureMatrix[x][y]).getGoods()[2]) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    //Is this useful?

    private int getDrivingPower(VisitorAttributesUpdater visitor, Component component) {
        return (Integer) component.accept(visitor).get(0);
    }

    private float getFirePower(VisitorAttributesUpdater visitor, Component component) {
        return (Float) component.accept(visitor).get(1);
    }

    private int getCrewMembers(VisitorAttributesUpdater visitor, Component component) {
        return (Integer) component.accept(visitor).get(2);
    }

    private int getBatteryPower(VisitorAttributesUpdater visitor, Component component) {
        return (Integer) component.accept(visitor).get(3);
    }

    private boolean[] getCoveredSides(VisitorAttributesUpdater visitor, Component component) {
        return (boolean[]) component.accept(visitor).get(4);
    }

    private int getAvailableRedSlots(VisitorAttributesUpdater visitor, Component component) {
        return (Integer) component.accept(visitor).get(5);
    }

    private int getAvailableBlueSlots(VisitorAttributesUpdater visitor, Component component) {
        return (Integer) component.accept(visitor).get(6);
    }

    private boolean getAmIASupport(VisitorAttributesUpdater visitor, Component component) {
        return (Boolean) component.accept(visitor).get(7);
    }

    //this function might change completly since i'm still not sure where the error checking will be
    private void solveError(int x, int y){
        matrErrors[y][x]= false;
    }
}
