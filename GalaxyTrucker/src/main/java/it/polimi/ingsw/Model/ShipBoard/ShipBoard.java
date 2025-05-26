package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Shipboard used to create and store the player's ship and it's attributes.
 * <p>
 * Indexes:
 * Player uses visible indexes [1...12] (center: 7-7)
 * Shipboard uses real indexes shifted by -1 [0...11] (center: 6-6)
 * <p>
 * RealIndex = VisibleIndex - 1
 * <p>
 * Matrix structure:
 * Shipboard[cols][rows]
 *
 * @author Giacomo, Boti
 */
public class ShipBoard implements Serializable {
    // visible values
    public static final int SB_COLS = 12;
    public static final int SB_ROWS = 12;
    public static final int SB_CENTER_COL = 7;
    public static final int SB_CENTER_ROW = 7;
    // the first column and row that can contain components
    // (components in [FIRST_REAL...MAX - FIRST_REAL (included!)])
    public static final int SB_FIRST_REAL_COL = 3;
    public static final int SB_FIRST_REAL_ROW = 4;

    // final Object: reference cannot be changed (but state/elements can change)
    private final ComponentVisitor<Void> sbAttributesUpdaterVisitor;
    private final ShipBoardAttributes shipBoardAttributes;
    // Matrix representing the ship's component layout
    private final Component[][] componentMatrix;
    // Boolean matrix indicating valid positions for components
    private final boolean[][] validityMatrix;
    // Boolean matrix indicating components with errors (true if error)
    private final boolean[][] errorsMatrix;
    // Matrix: [cols][rows]

    // keeps track of the lists of connected components
    // if shipboard is fractured, different connected parts are inserted as separate lists into the list
    private final List<List<Component>> connectedComponentsList;
    // TODO
    // 1 element is okay, 0 crew is removed


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
        this.shipBoardAttributes = new ShipBoardAttributes(this);
        this.sbAttributesUpdaterVisitor = new SBAttributesUpdaterVisitor(shipBoardAttributes);
        this.componentMatrix = new Component[SB_COLS][SB_ROWS];
        this.validityMatrix = new boolean[SB_COLS][SB_ROWS];
        this.errorsMatrix = new boolean[SB_COLS][SB_ROWS];
        this.connectedComponentsList = new ArrayList<>();


        // Initialize component matrix as empty
        for (int i = 0; i < SB_COLS; i++) {
            for (int j = 0; j < SB_ROWS; j++) {
                componentMatrix[i][j] = null;
            }
        }
        // Initialize all positions as valid
        for (int i = 0; i < SB_COLS; i++) {
            for (int j = 0; j < SB_ROWS; j++) {
                validityMatrix[i][j] = true;
            }
        }
        // Initialize error matrix as error free
        for (int i = 0; i < SB_COLS; i++) {
            for (int j = 0; j < SB_ROWS; j++) {
                errorsMatrix[i][j] = false;
            }
        }

        // Set forbidden zones for component placement
        // Set forbidden zones in the structure
        // forbidden rows
        for (int i = 0; i < SB_COLS; i++) {
            validityMatrix[i][0] = false;
            validityMatrix[i][1] = false;
            validityMatrix[i][2] = false;
            validityMatrix[i][3] = false;
            validityMatrix[i][9] = false;
            validityMatrix[i][10] = false;
            validityMatrix[i][11] = false;

        }
        // forbidden columns
        for (int i = 0; i < SB_ROWS; i++) {
            validityMatrix[0][i] = false;
            validityMatrix[1][i] = false;
            validityMatrix[2][i] = false;
            validityMatrix[10][i] = false;
            validityMatrix[11][i] = false;
        }
        // inside cells
        // NormalGame
        if (gameType.equals(GameType.NORMALGAME)) {
            validityMatrix[3][4] = false;
            validityMatrix[3][5] = false;
            validityMatrix[4][4] = false;
            validityMatrix[6][4] = false;
            validityMatrix[6][8] = false;
            validityMatrix[8][4] = false;
            validityMatrix[9][4] = false;
            validityMatrix[9][5] = false;
        }
        // TestGame
        else {
            for (int i = 0; i < SB_ROWS; i++) {
                validityMatrix[3][i] = false;
                validityMatrix[9][i] = false;
            }
            validityMatrix[4][4] = false;
            validityMatrix[4][5] = false;
            validityMatrix[5][4] = false;
            validityMatrix[6][8] = false;
            validityMatrix[7][4] = false;
            validityMatrix[8][4] = false;
            validityMatrix[8][5] = false;
        }

        // add center cabin
        addStarterCabin();

    }

    private void addStarterCabin() {
        // TODO: colored starter cabin, to get from the componentList in gameInformation
        Component starterCabin = new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal});
        componentMatrix[getRealIndex(SB_CENTER_COL)][getRealIndex(SB_CENTER_ROW)] = starterCabin;
        connectedComponentsList.add(new ArrayList<>());
        connectedComponentsList.getFirst().add(starterCabin);
        starterCabin.accept(sbAttributesUpdaterVisitor);
    }

    private int getRealIndex(int visibleIndex) {
        return visibleIndex - 1;
    }

    /**
     * Alternative parameter order to improve readability during testing.
     */
    public void addComponent(int visibleCol, int visibleRow, Component component) throws NotPermittedPlacementException, IllegalArgumentException {
        addComponent(component, visibleCol, visibleRow);
    }

    /**
     * Adds a component to the specified position in the structure matrix,
     * and updates Shipboard Attributes.
     *
     * @param component  The component to add.
     * @param visibleCol Visible column.
     * @param visibleRow Visible row.
     * @author Giacomo, Boti
     */
    public void addComponent(Component component, int visibleCol, int visibleRow) throws NotPermittedPlacementException, IllegalArgumentException {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);

        if (!checkValidPlacement(col, row)) {
            throw new NotPermittedPlacementException();
        } else {
            // add component to shipBoard
            componentMatrix[col][row] = component;
            // update shipboard attributes
            component.accept(sbAttributesUpdaterVisitor);
            // add component to connected components list
            connectedComponentsList.getFirst().add(component);
        }
    }

    /**
     * Throws IllegalArgumentException if entered coordinates are out of bounds of the shipBoard.
     * To call at the start of every method that takes coordinates as input.
     *
     * @throws IllegalArgumentException if coordinates out of bounds.
     * @author Boti
     */
    private void checkIndexInBounds(int visibleCol, int visibleRow) throws IllegalArgumentException {
        if (visibleCol < 0 || visibleCol > SB_COLS || visibleRow < 0 || visibleRow > SB_ROWS)
            throw new IllegalArgumentException("The entered coordinates are out of bounds");
    }

    /**
     * Check if the requested cell is valid to place a component in.
     * Call checkIndexInBounds before.
     *
     * @return true if placement is valid, false if invalid.
     */
    private boolean checkValidPlacement(int realCol, int realRow) {
        return (validityMatrix[realCol][realRow] &&
                componentMatrix[realCol][realRow] == null &&
                checkAdjacency(realCol, realRow));
    }

    /**
     * Check if adjacent cells contain components.
     *
     * @return true if there is at least one adjacent component, false if all adjacent cells are empty.
     */
    private boolean checkAdjacency(int realCol, int realRow) {
        return (componentMatrix[realCol - 1][realRow] != null || componentMatrix[realCol + 1][realRow] != null ||
                componentMatrix[realCol][realRow - 1] != null || componentMatrix[realCol][realRow + 1] != null);
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

    /**
     * Return the component at the given visible coordinates.
     */
    public Component getComponent(int visibleCol, int visibleRow) {
        return componentMatrix[getRealIndex(visibleCol)][getRealIndex(visibleRow)];
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
     * Checks if there are erroneous components in the shipboard.
     *
     * @return true if there are errors in shipboard, false if shipboard correct.
     * @author Boti
     */
    public boolean isErroneous() {
        checkErrors();
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_COLS - SB_FIRST_REAL_COL; realCol++) {
            // iterate rows
            for (int realRow = SB_FIRST_REAL_ROW; realRow < SB_ROWS - SB_FIRST_REAL_ROW; realRow++) {
                if (errorsMatrix[realCol][realRow])
                    return true;
            }
        }
        // no errors found
        return false;
    }


    /**
     * Scans the ship structure to identify errors.
     * Updates errorMatrix with erroneous components
     * (true for error, false for correct).
     * <p>
     * Verify connection to ship, correct junctions, engine and cannon rules.
     *
     * @author Giacomo, Boti
     */
    public void checkErrors() {
        Component component;

        // max-real included!
        // iterate columns
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_COLS - SB_FIRST_REAL_COL; realCol++) {
            // iterate rows
            for (int realRow = SB_FIRST_REAL_ROW; realRow < SB_ROWS - SB_FIRST_REAL_ROW; realRow++) {
                // default: no error in cell
                // override if cell erroneous
                errorsMatrix[realCol][realRow] = false;
                component = componentMatrix[realCol][realRow];

                // if component is present, check for errors
                if (component != null) {

                    // check if adjacent
                    if (!checkAdjacency(realCol, realRow)) {
                        errorsMatrix[realCol][realRow] = true;
                        break;
                    }

                    // check junctions
                    if (!checkCorrectJunctions(realCol, realRow)) {
                        errorsMatrix[realCol][realRow] = true;
                        break;
                    }

                    // check if Engine
                    if (component instanceof Engine) {
                        if (checkEngineErrors(realCol, realRow)) {
                            errorsMatrix[realCol][realRow] = true;
                            break;
                        }
                    }
                    // check if Cannon
                    else if (component instanceof Cannon) {
                        if (checkCannonErrors(realCol, realRow)) {
                            errorsMatrix[realCol][realRow] = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * Checks if a component has correct junctions with its neighboring components.
     *
     * @return True if the junctions are correct, false otherwise.
     * @author Giacomo, Boti
     */
    private boolean checkCorrectJunctions(int realCol, int realRow) {
        Component currentComponent = componentMatrix[realCol][realRow];
        // empty cell has correct junctions
        if (currentComponent == null)
            return true;

            // component present
        else {
            // return false if incompatible junction
            // left neighbour
            if ((componentMatrix[realCol - 1][realRow] != null) &&
                    !checkCompatibleJunction(currentComponent.getLeft(), componentMatrix[realCol - 1][realRow].getRight()))
                return false;
            // right neighbour
            if ((componentMatrix[realCol + 1][realRow] != null) &&
                    !checkCompatibleJunction(currentComponent.getRight(), componentMatrix[realCol + 1][realRow].getLeft()))
                return false;
            // front neighbour
            if ((componentMatrix[realCol][realRow - 1] != null) &&
                    (!checkCompatibleJunction(currentComponent.getFront(), componentMatrix[realCol][realRow - 1].getBack())))
                return false;
            // back neighbour
            if ((componentMatrix[realCol][realRow + 1] != null) &&
                    (!checkCompatibleJunction(currentComponent.getBack(), componentMatrix[realCol][realRow + 1].getFront())))
                return false;
            // no incorrect junctions
            return true;
        }
    }

    /**
     * Check for engine errors.
     *
     * @return true if there are errors, false if correct.
     */
    private boolean checkEngineErrors(int realCol, int realRow) {
        Engine component = (Engine) componentMatrix[realCol][realRow];
        // engine not facing backwards
        if (!component.getBack().equals(SideType.Special))
            return true;

            // engine obstructed
        else if (componentMatrix[realCol][realRow + 1] != null)
            return true;
        // no errors found
        return false;
    }

    /**
     * Check for cannon errors.
     *
     * @return true if there are errors, false if correct.
     */
    private boolean checkCannonErrors(int realCol, int realRow) {
        Cannon component = (Cannon) componentMatrix[realCol][realRow];
        // cannon obstructed
        // front cannon obstructed
        if (component.getFront().equals(SideType.Special)) {
            if (componentMatrix[realCol][realRow - 1] != null)
                return true;
        }
        // right cannon
        else if (component.getRight().equals(SideType.Special)) {
            if (componentMatrix[realCol + 1][realRow] != null)
                return true;
        }
        // left cannon
        else if (component.getLeft().equals(SideType.Special)) {
            if (componentMatrix[realCol - 1][realRow] != null)
                return true;
        }
        // back cannon
        else {
            if (componentMatrix[realCol][realRow + 1] != null)
                return true;
        }
        // no errors found
        return false;
    }

    /**
     * Check if the 2 provided junctions are compatible.
     *
     * @return true if junctions are compatible, false if incompatible.
     * @author Boti
     */
    private boolean checkCompatibleJunction(SideType sideA, SideType sideB) {
        if (sideA.equals(SideType.Smooth) || sideA.equals(SideType.Special)) {
            return (sideB.equals(SideType.Smooth) || sideB.equals(SideType.Special));
        } else if (sideA.equals(SideType.Single)) {
            return (sideB.equals(SideType.Single) || sideB.equals(SideType.Universal));
        } else if (sideA.equals(SideType.Double)) {
            return (sideB.equals(SideType.Double) || sideB.equals(SideType.Universal));
        } else if (sideA.equals(SideType.Universal)) {
            return (sideB.equals(SideType.Single) || sideB.equals(SideType.Double) || sideB.equals(SideType.Universal));
        }
        return false;
    }


    /**
     * Removes a component from the specified position.
     * Updates the shipBoard and shipBoardAttributes
     * TODO fracture
     *
     * @author Giacomo, Boti
     */
    public void removeComponent(int visibleCol, int visibleRow, boolean checkDisconnectionTrigger) throws IllegalArgumentException {
        checkIndexInBounds(visibleCol, visibleRow);
        int realCol = getRealIndex(visibleCol);
        int realRow = getRealIndex(visibleRow);
        Component component = componentMatrix[realCol][realRow];

        // if a present element is to be removed
        if (component != null) {
            checkFracturedShipBoard();
            shipBoardAttributes.destroyComponents(1);
            // update shipboard attributes
            component.accept(sbAttributesUpdaterVisitor);

        }

    }

    private void checkFracturedShipBoard() throws FracturedShipBoardException {

    }

    /**
     * Counts the number of external junctions in the ship structure.
     * External junctions occur when a component has a connection point
     * facing an empty space.
     *
     * @return The total number of external junctions.
     * @author Giacomo, Boti
     */
    public int countExternalJunctions() {
        int externalJunctions = 0;
        for (int i = SB_FIRST_REAL_COL; i <= SB_COLS - SB_FIRST_REAL_COL; i++) {
            for (int j = SB_FIRST_REAL_ROW; j <= SB_ROWS - SB_FIRST_REAL_ROW; j++) {
                Component component = componentMatrix[i][j];
                if (component != null) {
                    // check front
                    if (isConnector(component.getFront()) && componentMatrix[i][j - 1] == null)
                        externalJunctions++;
                    // check back
                    if (isConnector(component.getBack()) && componentMatrix[i][j + 1] == null)
                        externalJunctions++;
                    // check left
                    if (isConnector(component.getLeft()) && componentMatrix[i - 1][j] == null)
                        externalJunctions++;
                    // check right
                    if (isConnector(component.getRight()) && componentMatrix[i + 1][j] == null)
                        externalJunctions++;
                }
            }
        }
        return externalJunctions;
    }

    /**
     * @return True if given sideType is a connector, false if not a connector.
     */
    private boolean isConnector(SideType sideType) {
        return (sideType.equals(SideType.Single) || sideType.equals(SideType.Double) || sideType.equals(SideType.Universal));
    }


    /**
     * Remove a battery from the battery storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void removeBattery(int visibleCol, int visibleRow) {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Battery))
            throw new IllegalArgumentException("The selected component is not a battery");

        if (component.getBatteryPower() - 1 >= 0) {
            ((Battery) component).removeBattery();
            // update shipboard attributes
            component.accept(sbAttributesUpdaterVisitor);
        } else
            throw new IllegalArgumentException("Not enough batteries at the selected component.");
    }

    /**
     * Remove a crew member from the cabin at the given coordinates, if possible.
     * Accounts for humans and aliens.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void removeCrewMember(int visibleCol, int visibleRow) {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalArgumentException("The selected component is not a cabin");

        if (component.getCrewMembers() - 1 >= 0) {
            ((Cabin) component).removeInhabitant();
            // update shipboard attributes
            component.accept(sbAttributesUpdaterVisitor);
        } else {
            throw new IllegalArgumentException("Not enough crew members at the selected component.");
        }
    }

    /**
     * Sets the crewType of the cabin at the selected coordinates, if possible.
     * Checks for alien supports.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void setCrewType(int visibleCol, int visibleRow, CrewType crewType) {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalArgumentException("The selected component is not a cabin");

        // cabin with crew
        if (component.getCrewMembers() > 0) {
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Human))
                return;
                // purple alien (1 per shipboard)
            else if (crewType.equals(CrewType.Purple) && !shipBoardAttributes.getPurpleAlien() &&
                    checkForAlienSupport(col, row, crewType)) {
                ((Cabin) component).setCrewType(crewType);
                // update shipboard attributes
                component.accept(sbAttributesUpdaterVisitor);
            }
            // brown alien (1 per shipboard)
            else if (crewType.equals(CrewType.Brown) && !shipBoardAttributes.getBrownAlien() &&
                    checkForAlienSupport(col, row, crewType)) {
                ((Cabin) component).setCrewType(crewType);
                // update shipboard attributes
                component.accept(sbAttributesUpdaterVisitor);
            } else
                throw new IllegalArgumentException("Crew type couldn't be set for the selected component.");

        } else
            throw new IllegalArgumentException("Crew type couldn't be set for the selected component.");
    }

    /**
     * Check if component at given coordinates is connected to an alien support of given type.
     *
     * @return true if connected to given alien support, false if not connected.
     * @author Boti
     */
    private boolean checkForAlienSupport(int realCol, int realRow, CrewType crewType) {
        Component component = componentMatrix[realCol][realRow];
        Component temp;
        // check front
        temp = componentMatrix[realCol][realRow - 1];
        if (temp != null && checkCompatibleJunction(component.getFront(), temp.getBack()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check back
        temp = componentMatrix[realCol][realRow + 1];
        if (temp != null && checkCompatibleJunction(component.getBack(), temp.getFront()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check left
        temp = componentMatrix[realCol - 1][realRow];
        if (temp != null && checkCompatibleJunction(component.getLeft(), temp.getRight()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check right
        temp = componentMatrix[realCol + 1][realRow];
        if (temp != null && checkCompatibleJunction(component.getRight(), temp.getLeft()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // no matching support found
        return false;
    }

    /**
     * Add goods to the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void addGoods(int visibleCol, int visibleRow, int[] goods) {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Storage))
            throw new IllegalArgumentException("The selected component is not a storage");

        // negative goods - malicious intent
        if (goods[0] < 0 || goods[1] < 0 || goods[2] < 0 || goods[3] < 0)
            throw new IllegalArgumentException("Cannot add negative number of goods");

        // check red goods slots
        if (goods[0] > component.getAvailableRedSlots())
            throw new IllegalArgumentException("Not enough red goods storage available");

        // check red + normal goods slots
        int totalGoods = 0;
        for (int good : goods)
            totalGoods += good;
        if (totalGoods > (component.getAvailableRedSlots() + component.getAvailableBlueSlots()))
            throw new IllegalArgumentException("Not enough goods storage available");

        // no problems, add goods to component
        ((Storage) component).addGoods(goods);
        // update shipboard attributes
        component.accept(sbAttributesUpdaterVisitor);
    }

    /**
     * Remove goods from the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void removeGoods(int visibleCol, int visibleRow, int[] goods) {
        checkIndexInBounds(visibleCol, visibleRow);
        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Storage))
            throw new IllegalArgumentException("The selected component is not a storage");

        // negative goods - malicious intent
        if (goods[0] < 0 || goods[1] < 0 || goods[2] < 0 || goods[3] < 0)
            throw new IllegalArgumentException("Cannot add negative number of goods");

        // check available goods
        int[] componentGoods = ((Storage) component).getGoods();
        for (int i = 0; i < componentGoods.length; i++) {
            if (componentGoods[i] < goods[i])
                throw new IllegalArgumentException("Not enough goods storage available");
        }

        // no problems, remove goods from component
        ((Storage) component).removeGoods(goods);
        // update shipboard attributes
        component.accept(sbAttributesUpdaterVisitor);
    }


    /**
     * Checks if any components are not connected to the main structure.
     * If a component is unreachable, it is removed.
     *
     * @param shipBoardAttributes The ShipBoard object that tracks destroyed components.
     * @return True if any unreachable components were found, false otherwise.
     * @author Giacomo
     */
    /*
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
    */

    /**
     * Recursively marks reachable components.
     *
     * @param x   The x-coordinate.
     * @param y   The y-coordinate.
     * @param mat The boolean matrix tracking visited positions.
     * @author Giacomo
     */
    /*
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
    */

}
