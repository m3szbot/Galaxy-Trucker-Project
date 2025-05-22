package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.io.Serializable;
import java.util.List;

public class ShipBoard implements Serializable {
    // x: column
    // y: row
    private ShipBoardAttributes shipBoardAttributes;
    // Matrix representing the ship's component layout
    private Component[][] componentMatrix;
    // Boolean matrix indicating valid positions for components
    private boolean[][] validityMatrix;
    // Boolean matrix indicating components with errors
    private boolean[][] errorsMatrix;

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
        this.componentMatrix = new Component[12][12];
        this.validityMatrix = new boolean[12][12];
        this.errorsMatrix = new boolean[12][12];
        this.shipBoardAttributes = new ShipBoardAttributes();
        // Initialize all positions as valid
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                validityMatrix[i][j] = true;
            }
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                componentMatrix[i][j] = null;
            }
        }
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                errorsMatrix[i][j] = false;
            }
        }

        if (gameType.equals(GameType.TESTGAME)) {
        } else {
            // Set forbidden zones in the structure
            for (int i = 0; i < 12; i++) {
                validityMatrix[i][0] = false;
                validityMatrix[i][1] = false;
                validityMatrix[i][2] = false;
                validityMatrix[i][10] = false;
                validityMatrix[i][11] = false;

            }
            for (int i = 0; i < 12; i++) {
                validityMatrix[0][i] = false;
                validityMatrix[1][i] = false;
                validityMatrix[2][i] = false;
                validityMatrix[3][i] = false;
                validityMatrix[9][i] = false;
                validityMatrix[10][i] = false;
                validityMatrix[11][i] = false;
            }

            validityMatrix[4][3] = false;
            validityMatrix[4][4] = false;
            validityMatrix[5][3] = false;
            validityMatrix[4][6] = false;
            validityMatrix[8][6] = false;
            validityMatrix[4][8] = false;
            validityMatrix[4][9] = false;
            validityMatrix[5][9] = false;
        }

        try {
            addComponent(new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}), 7, 7);
        } catch (NotPermittedPlacementException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Adds a component to the specified position in the structure matrix.
     *
     * @param component The component to add.
     * @param col       The x-coordinate of the component.
     * @param row       The y-coordinate of the component.
     * @author Giacomo
     */
    public void addComponent(Component component, int col, int row) throws NotPermittedPlacementException {
        col = col - 1;
        row = row - 1;
        Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
        if (validityMatrix[row][col]) {
            componentMatrix[row][col] = component;
            List<Object> list = component.accept(visitor);
            if ((Integer) list.get(0) == 1) {
                shipBoardAttributes.updateDrivingPower((Integer) list.get(0));
            } else if ((Integer) list.get(0) == 2) {
                shipBoardAttributes.updateNumberDoubleEngines(1);
            }
            if ((Float) list.get(1) == 1) {
                shipBoardAttributes.updateFirePower((Float) list.get(1));
            } else if ((Float) list.get(1) == 2) {
                if (component.getFront().equals(SideType.Special)) {
                    shipBoardAttributes.updateNumberForwardDoubleCannons(1);
                } else {
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
        } else {
            throw new NotPermittedPlacementException();
        }
        //qua devo fare l'aggiunta degli indici con un metodo add che aggiorni tutti gli indici
    }

    public Component[][] getComponentMatrix() {
        return componentMatrix;
    }

    public boolean[][] getValidityMatrix() {
        return validityMatrix;
    }

    public boolean[][] getErrorsMatrix() {
        return errorsMatrix;
    }

    public Component getComponent(int col, int row) {
        return componentMatrix[row][col];
    }

    public int getMatrixRows() {
        return componentMatrix.length;
    }

    public int getMatrixCols() {
        return componentMatrix[0].length;
    }

    public ShipBoardAttributes getShipBoardAttributes() {
        return shipBoardAttributes;
    }

    /**
     * Checks if there are errors in the shipboard
     *
     * @return true if there are errors, false if correct
     * @author Boti
     */
    public boolean isErroneous() {
        if (this.checkErrors() > 0)
            return true;
        return false;
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
                if (componentMatrix[i][j] != null) {
                    if (!checkCorrectJunctions(i, j)) {
                        //System.out.println("Component " + (j + 1) + " " + (i + 1) + " is not well connected");
                        errorsMatrix[i][j] = true;
                        errors++;
                    }
                    if ((Integer) componentMatrix[i][j].accept(visitor).get(0) > 0) {
                        if (!componentMatrix[i][j].getBack().equals(SideType.Special)) {
                            errorsMatrix[i][j] = true;
                            errors++;
                        } else {
                            boolean check = false;
                            if (componentMatrix[i + 1][j] != null) {
                                check = true;
                                errors++;
                            }
                            /*
                            for (int k = i + 1; k < 12; k++) {
                                if (componentMatrix[k][j] != null) {
                                    check = true;
                                    errors++;
                                }
                            }
                            */
                            if (check) {
                                System.out.println("Error, in component" + i + ' ' + j);
                                errorsMatrix[i][j] = true;
                            }
                        }
                    }
                    if ((Float) componentMatrix[i][j].accept(visitor).get(1) > 0) {
                        boolean check = false;
                        if (componentMatrix[i][j].getLeft().equals(SideType.Special)) {

                            if (componentMatrix[i][j - 1] != null) {
                                check = true;
                                errorsMatrix[i][j] = true;
                                errors++;
                            }

                            if (check) {
                                System.out.println("Error, in component" + i + ' ' + j);
                            }
                        } else if (componentMatrix[i][j].getRight().equals(SideType.Special)) {

                            if (componentMatrix[i][j + 1] != null) {
                                check = true;
                                errorsMatrix[i][j] = true;
                                errors++;
                            }

                            if (check) {
                                System.out.println("Error, in component" + i + ' ' + j);
                            }
                        } else if (componentMatrix[i][j].getFront().equals(SideType.Special)) {
                            if (componentMatrix[i - 1][j] != null) {
                                check = true;
                                errorsMatrix[i][j] = true;
                                errors++;
                            }
                            if (check) {
                                System.out.println("Error, in component" + i + ' ' + j);
                            }
                        } else if (componentMatrix[i][j].getBack().equals(SideType.Special)) {
                            if (componentMatrix[i + 1][j] != null) {
                                check = true;
                                errorsMatrix[i][j] = true;
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
     * Checks if a component has correct junctions with its neighboring components.
     *
     * @param x The x-coordinate of the component.
     * @param y The y-coordinate of the component.
     * @return True if the junctions are correct, false otherwise.
     * @author Giacomo
     */
    private boolean checkCorrectJunctions(int x, int y) {
        if (componentMatrix[x][y] != null) {


            if ((componentMatrix[x][y].getLeft().equals(SideType.Single) && componentMatrix[x][y - 1] != null &&
                    (!componentMatrix[x][y - 1].getRight().equals(SideType.Single) && !componentMatrix[x][y - 1].getRight().equals(SideType.Universal))) ||

                    (componentMatrix[x][y].getLeft().equals(SideType.Double) && componentMatrix[x][y - 1] != null &&
                            (!componentMatrix[x][y - 1].getRight().equals(SideType.Double) && !componentMatrix[x][y - 1].getRight().equals(SideType.Universal))) ||


                    (componentMatrix[x][y].getFront().equals(SideType.Double) && componentMatrix[x - 1][y] != null &&
                            (!componentMatrix[x - 1][y].getBack().equals(SideType.Double) && !componentMatrix[x - 1][y].getBack().equals(SideType.Universal))) ||

                    (componentMatrix[x][y].getFront().equals(SideType.Single) && componentMatrix[x - 1][y] != null &&
                            (!componentMatrix[x - 1][y].getBack().equals(SideType.Single) && !componentMatrix[x - 1][y].getBack().equals(SideType.Universal))) ||


                    (componentMatrix[x][y].getRight().equals(SideType.Single) && componentMatrix[x][y + 1] != null &&
                            (!componentMatrix[x][y + 1].getLeft().equals(SideType.Single) && !componentMatrix[x][y + 1].getLeft().equals(SideType.Universal))) ||

                    (componentMatrix[x][y].getRight().equals(SideType.Double) && componentMatrix[x][y + 1] != null &&
                            (!componentMatrix[x][y + 1].getLeft().equals(SideType.Double) && !componentMatrix[x][y + 1].getLeft().equals(SideType.Universal))) ||


                    (componentMatrix[x][y].getBack().equals(SideType.Single) && componentMatrix[x + 1][y] != null &&
                            (!componentMatrix[x + 1][y].getFront().equals(SideType.Single) && !componentMatrix[x + 1][y].getFront().equals(SideType.Universal))) ||

                    (componentMatrix[x][y].getBack().equals(SideType.Double) && componentMatrix[x + 1][y] != null &&
                            (!componentMatrix[x + 1][y].getFront().equals(SideType.Double) && !componentMatrix[x + 1][y].getFront().equals(SideType.Universal)))
            ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Removes a component from the specified position.
     * Updates the shipBoard to reflect the destroyed components.
     *
     * @param col The x-coordinate of the component.
     * @param row The y-coordinate of the component.
     * @author Giacomo
     */
    public void removeComponent(int col, int row, boolean checkTrigger) {
        boolean flag = true;
        col = col - 1;
        row = row - 1;

        if (validityMatrix[row][col] == true && componentMatrix[row][col] != null) {
            Component component = componentMatrix[row][col];
            Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
            List<Object> list = component.accept(visitor);
            shipBoardAttributes.updateDestroyedComponents(1);
            shipBoardAttributes.updateDrivingPower(-(Integer) list.get(0));
            if ((Float) list.get(1) == 1) {
                shipBoardAttributes.updateFirePower(-(Float) list.get(1));
            } else if ((Float) list.get(1) == 2) {
                if (component.getFront().equals(SideType.Special)) {
                    shipBoardAttributes.updateNumberForwardDoubleCannons(-1);
                } else {
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
            if ((Boolean) list.get(8) == true) {
                int[] update = ((Storage) component).getGoods();
                update[0] = -update[0];
                update[1] = -update[1];
                update[2] = -update[2];
                update[3] = -update[3];
                shipBoardAttributes.updateGoods(update);
            }
            componentMatrix[row][col] = null;
            if (checkTrigger) {
                while (checkNotReachable(this.shipBoardAttributes)) ;
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
                if (componentMatrix[i][j] != null) {
                    //va sistemato il fatto che qualora si volesse davvero usare un enum allora dovrebbe essere messo tipodiverso da vuoto e diverso da shield
                    if ((!componentMatrix[i][j].getLeft().equals(SideType.Smooth) && componentMatrix[i - 1][j] == null)) {
                        externalJunctions++;
                    }
                    if ((!componentMatrix[i][j].getRight().equals(SideType.Smooth) && componentMatrix[i + 1][j] == null)) {
                        externalJunctions++;
                    }
                    if ((!componentMatrix[i][j].getFront().equals(SideType.Smooth) && componentMatrix[i][j - 1] == null)) {
                        externalJunctions++;
                    }
                    if ((!componentMatrix[i][j].getBack().equals(SideType.Smooth) && componentMatrix[i][j + 1] == null)) {
                        externalJunctions++;
                    }
                }
            }
        }
        return externalJunctions;
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
                    if (componentMatrix[i][j] != null) {
                        if (!mat[i][j]) {
                            flag = 1;
                            result = true;
                            removeComponent(j + 1, i + 1, false);
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
        if (componentMatrix[x][y] == null || mat[x][y]) return;
        mat[x][y] = true;
        // Right
        if (y + 1 < 12 && componentMatrix[x][y + 1] != null &&
                isCompatible(componentMatrix[x][y].getRight(), componentMatrix[x][y + 1].getLeft())) {
            goDownChecking(x, y + 1, mat);
        }

        // Left
        if (y - 1 >= 0 && componentMatrix[x][y - 1] != null &&
                isCompatible(componentMatrix[x][y].getLeft(), componentMatrix[x][y - 1].getRight())) {
            goDownChecking(x, y - 1, mat);
        }

        // Down
        if (x + 1 < 12 && componentMatrix[x + 1][y] != null &&
                isCompatible(componentMatrix[x][y].getBack(), componentMatrix[x + 1][y].getFront())) {
            goDownChecking(x + 1, y, mat);
        }

        // Up
        if (x - 1 >= 0 && componentMatrix[x - 1][y] != null &&
                isCompatible(componentMatrix[x][y].getFront(), componentMatrix[x - 1][y].getBack())) {
            goDownChecking(x - 1, y, mat);
        }
    }

    private boolean isCompatible(SideType a, SideType b) {
        return (a == SideType.Single && (b == SideType.Single || b == SideType.Universal)) ||
                (a == SideType.Double && (b == SideType.Double || b == SideType.Universal)) ||
                (a == SideType.Universal && (b == SideType.Double || b == SideType.Universal || b == SideType.Single));
    }

    /**
     * Sets the crew type in a cabin component, ensuring compatibility with alien support components.
     *
     * @param crewType The type of crew to assign.
     * @param col      The x-coordinate of the cabin.
     * @param row      The y-coordinate of the cabin.
     * @author Giacomo
     */
    public void setCrewType(CrewType crewType, int col, int row) {
        col = col - 1;
        row = row - 1;
        Visitor<List<Object>> visitor = new VisitorAttributesUpdater();
        if (componentMatrix[row][col] != null && componentMatrix[row][col].getComponentName().equals("Cabin")) {
            if (crewType.equals(CrewType.Brown)) {
                if ((componentMatrix[row - 1][col] != null && (Boolean) componentMatrix[row - 1][col].accept(visitor).get(7) && !((AlienSupport) componentMatrix[row - 1][col]).isPurple()) ||
                        (componentMatrix[row + 1][col] != null && (Boolean) componentMatrix[row + 1][col].accept(visitor).get(7) && !((AlienSupport) componentMatrix[row + 1][col]).isPurple()) ||
                        (componentMatrix[row][col - 1] != null && (Boolean) componentMatrix[row][col - 1].accept(visitor).get(7) && !((AlienSupport) componentMatrix[row][col - 1]).isPurple()) ||
                        (componentMatrix[row][col + 1] != null && (Boolean) componentMatrix[row][col + 1].accept(visitor).get(7) && !((AlienSupport) componentMatrix[row][col + 1]).isPurple())) {
                    ((Cabin) componentMatrix[row][col]).setCrewType(crewType);
                    shipBoardAttributes.updateAlien(CrewType.Brown, false);
                    shipBoardAttributes.updateCrewMembers(-1);
                } else {
                    System.out.println("CrewType not permitted");
                }
            } else {
                if ((componentMatrix[row - 1][col] != null && (Boolean) componentMatrix[row - 1][col].accept(visitor).get(7) && ((AlienSupport) componentMatrix[row - 1][col]).isPurple()) ||
                        (componentMatrix[row + 1][col] != null && (Boolean) componentMatrix[row + 1][col].accept(visitor).get(7) && ((AlienSupport) componentMatrix[row + 1][col]).isPurple()) ||
                        (componentMatrix[row][col - 1] != null && (Boolean) componentMatrix[row][col - 1].accept(visitor).get(7) && ((AlienSupport) componentMatrix[row][col - 1]).isPurple()) ||
                        (componentMatrix[row][col + 1] != null && (Boolean) componentMatrix[row][col + 1].accept(visitor).get(7) && ((AlienSupport) componentMatrix[row][col + 1]).isPurple())) {
                    ((Cabin) componentMatrix[row][col]).setCrewType(crewType);
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
        if (componentMatrix[x][y] != null) {
            if (((Storage) componentMatrix[x][y]).isRed()) {
                if (goods[0] <= ((Storage) componentMatrix[x][y]).getNumberOfMaximumElements() - ((Storage) componentMatrix[x][y]).getGoods()[0]) {
                    return true;
                } else {
                    return false;
                }
            } else {
                if (goods[0] <= ((Storage) componentMatrix[x][y]).getNumberOfMaximumElements() - ((Storage) componentMatrix[x][y]).getGoods()[0] - ((Storage) componentMatrix[x][y]).getGoods()[3] - ((Storage) componentMatrix[x][y]).getGoods()[2]) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }


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
    private void solveError(int x, int y) {
        errorsMatrix[y][x] = false;
    }
}
