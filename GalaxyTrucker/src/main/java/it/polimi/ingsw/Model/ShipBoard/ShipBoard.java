package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.io.Serializable;
import java.util.*;

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
    // All attributes must be Serializable or transient
    // visible values
    public static final int SB_COLS = 12;
    public static final int SB_ROWS = 12;
    public static final int SB_CENTER_COL = 7;
    public static final int SB_CENTER_ROW = 7;
    // the first column and row that can contain components
    // (components in [FIRST_REAL...MAX - FIRST_REAL (included!)])
    public static final int SB_FIRST_REAL_COL = 3;
    public static final int SB_FIRST_REAL_ROW = 4;
    // last INCLUDED! column/row on the shipboard
    public static final int SB_LAST_REAL_COL = 9;
    public static final int SB_LAST_REAL_ROW = 8;

    // final Object: reference cannot be changed (but state/elements can change)
    private final GameType gameType;
    private final ShipBoardAttributes shipBoardAttributes;
    // Matrix representing the ship's component layout
    private final Component[][] componentMatrix;
    // Boolean matrix indicating valid positions for components
    private final boolean[][] validityMatrix;
    // Boolean matrix indicating components with errors (true if error)
    private final boolean[][] errorsMatrix;
    // Matrix: [cols][rows]

    // real coordinates of the current center cabin
    private int centerCabinCol;
    private int centerCabinRow;


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
        this.gameType = gameType;
        this.shipBoardAttributes = new ShipBoardAttributes(this);
        this.componentMatrix = new Component[SB_COLS][SB_ROWS];
        this.validityMatrix = new boolean[SB_COLS][SB_ROWS];
        this.errorsMatrix = new boolean[SB_COLS][SB_ROWS];


        // Initialize component matrix as empty
        for (int realCol = 0; realCol < SB_COLS; realCol++) {
            for (int realRow = 0; realRow < SB_ROWS; realRow++) {
                componentMatrix[realCol][realRow] = null;
            }
        }
        // Initialize all positions as valid
        for (int realCol = 0; realCol < SB_COLS; realCol++) {
            for (int realRow = 0; realRow < SB_ROWS; realRow++) {
                validityMatrix[realCol][realRow] = true;
            }
        }
        // Initialize error matrix as error free
        for (int realCol = 0; realCol < SB_COLS; realCol++) {
            for (int realRow = 0; realRow < SB_ROWS; realRow++) {
                errorsMatrix[realCol][realRow] = false;
            }
        }

        // Set forbidden zones for component placement
        // Set forbidden zones in the structure
        // forbidden rows
        for (int realCol = 0; realCol < SB_COLS; realCol++) {
            validityMatrix[realCol][0] = false;
            validityMatrix[realCol][1] = false;
            validityMatrix[realCol][2] = false;
            validityMatrix[realCol][3] = false;
            validityMatrix[realCol][9] = false;
            validityMatrix[realCol][10] = false;
            validityMatrix[realCol][11] = false;

        }
        // forbidden columns
        for (int realRow = 0; realRow < SB_ROWS; realRow++) {
            validityMatrix[0][realRow] = false;
            validityMatrix[1][realRow] = false;
            validityMatrix[2][realRow] = false;
            validityMatrix[10][realRow] = false;
            validityMatrix[11][realRow] = false;
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
            for (int realRow = 0; realRow < SB_ROWS; realRow++) {
                validityMatrix[3][realRow] = false;
                validityMatrix[9][realRow] = false;
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
        Component starterCabin = new Cabin(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal}, CrewType.Human, 2);
        try {
            addComponent(SB_CENTER_COL, SB_CENTER_ROW, starterCabin);
        } catch (NotPermittedPlacementException e) {
            System.out.println("Couldn't add starter cabin");
        }
        // set center
        centerCabinCol = getRealIndex(SB_CENTER_COL);
        centerCabinRow = getRealIndex(SB_CENTER_ROW);
        try {
            starterCabin.accept(new SBAttributesUpdaterVisitor(this));
        } catch (NoHumanCrewLeftException e) {
            throw new IllegalStateException("Error: no human crew after adding starter cabin.");
        }
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
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);

        if (!checkValidPlacement(col, row)) {
            throw new NotPermittedPlacementException("Cannot place component in the selected coordinates");
        } else {
            // add component to shipBoard
            componentMatrix[col][row] = component;
            // update shipboard attributes
            try {
                component.accept(new SBAttributesUpdaterVisitor(this));
            } catch (NoHumanCrewLeftException e) {
                throw new IllegalStateException("Error: no human crew after adding component.");
            }
            // add component to connected components list
        }
    }

    /**
     * @return the realIndex from the passed visibleIndex.
     */
    private int getRealIndex(int visibleIndex) {
        return visibleIndex - 1;
    }

    /**
     * Checks if the given visible coordinates respect the bounds of shipboard coordinates.
     *
     * @return true if coordinates are in bounds, false if out of bounds.
     * @author Boti
     */
    public static boolean checkCoordinatesInBounds(int visibleCol, int visibleRow) {
        return (visibleCol >= 0 && visibleCol < SB_COLS && visibleRow >= 0 && visibleRow < SB_ROWS);
    }

    /**
     * Check if the requested cell is valid to place a component in.
     * Call checkCoordinatesInBounds before.
     * Special case for first component.
     *
     * @return true if placement is valid, false if invalid.
     */
    private boolean checkValidPlacement(int realCol, int realRow) {
        // first component to add (to center)
        if ((getComponentCount() == 0) && (realCol == getRealIndex(SB_CENTER_COL))
                && (realRow == getRealIndex(SB_CENTER_ROW)))
            return true;

        // not first component to add
        return (validityMatrix[realCol][realRow] &&
                componentMatrix[realCol][realRow] == null &&
                checkNotEmptyNeighbors(realCol, realRow));
    }

    /**
     * @return the number of current components in the shipboard.
     * @author Boti
     */
    private int getComponentCount() {
        int count = 0;
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                if (componentMatrix[realCol][realRow] != null)
                    count++;
            }
        }
        return count;
    }

    /**
     * Check if adjacent cells contain components. Used in addComponent.
     *
     * @return true if at least 1 neighbor cell is not empty, false if all neighbor cells are empty.
     * @author Boti
     */
    private boolean checkNotEmptyNeighbors(int realCol, int realRow) {
        return (componentMatrix[realCol - 1][realRow] != null || componentMatrix[realCol + 1][realRow] != null ||
                componentMatrix[realCol][realRow - 1] != null || componentMatrix[realCol][realRow + 1] != null);
    }

    /**
     * Returns a shipboard prefilled with many components. Used for testing.
     *
     * @return new prefilled shipboard.
     * @author Boti
     */
    public static ShipBoard getNewPreFilledShipBoard() {
        ShipBoard tmpShipboard = new ShipBoard(GameType.NORMALGAME);
        SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
        SideType[] universalSidesSpecialFront = new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal};
        SideType[] universalSidesSpecialBack = new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal};

        // add components
        try {
            tmpShipboard.addComponent(new Cannon(universalSidesSpecialFront, true), 7, 6);
            tmpShipboard.addComponent(new Battery(universalSides, 3), 8, 6);
            tmpShipboard.addComponent(new Engine(universalSidesSpecialBack, true), 7, 8);
            tmpShipboard.addComponent(new Storage(universalSidesSpecialBack, true, 4), 6, 8);
            tmpShipboard.addComponent(new AlienSupport(universalSides, true), 6, 7);
            tmpShipboard.addComponent(new Cabin(universalSides, CrewType.Purple, 1), 5, 7);
            tmpShipboard.addComponent(new Shield(universalSides, 0, 3), 6, 6);
            tmpShipboard.addComponent(new Component(universalSides), 8, 7);
            tmpShipboard.addComponent(new Storage(universalSides, false, 3), 9, 7);
            tmpShipboard.addComponent(new Engine(universalSidesSpecialBack, false), 8, 8);
            tmpShipboard.addComponent(new Cannon(universalSidesSpecialBack, false), 9, 8);
        } catch (NotPermittedPlacementException e) {
        }

        // modify attributes
        tmpShipboard.addGoods(9, 7, new int[]{0, 1, 1, 1});
        tmpShipboard.addGoods(6, 8, new int[]{2, 0, 1, 1});

        return tmpShipboard;
    }

    /**
     * Add goods to the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void addGoods(int visibleCol, int visibleRow, int[] goods) throws IllegalArgumentException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

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
        try {
            component.accept(new SBAttributesUpdaterVisitor(this));
        } catch (NoHumanCrewLeftException e) {
            throw new IllegalStateException("Error: no human crew left after adding goods.");
        }
    }

    public boolean[][] getValidityMatrix() {
        return validityMatrix;
    }

    public boolean[][] getErrorsMatrix() {
        return errorsMatrix;
    }

    /**
     * @return the component at the given visible coordinates.
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

    /**
     * Checks if there are erroneous components in the shipboard.
     * Special case if only 1 component is present (starter cabin etc.).
     *
     * @return true if there are errors in shipboard, false if shipboard correct.
     * @author Boti
     */
    public boolean isErroneous() {
        // 1 component only - cannot be connected
        if (getComponentCount() <= 1)
            return false;

        // more components
        checkErrors();

        return (getErrorCount() > 0);
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
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            // iterate rows
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                // default: no error in cell
                // override if cell erroneous
                errorsMatrix[realCol][realRow] = false;
                component = componentMatrix[realCol][realRow];


                // if component is present, check for errors
                if (component != null) {
                    // check if connected to a neighbor
                    if (!checkConnectedToNeighbor(realCol, realRow)) {
                        errorsMatrix[realCol][realRow] = true;
                    }

                    // check junctions
                    else if (!checkCorrectJunctions(realCol, realRow)) {
                        errorsMatrix[realCol][realRow] = true;
                    }

                    // check if Engine
                    else if (component instanceof Engine) {
                        if (checkEngineErrors(realCol, realRow)) {
                            errorsMatrix[realCol][realRow] = true;
                        }
                    }
                    // check if Cannon
                    else if (component instanceof Cannon) {
                        if (checkCannonErrors(realCol, realRow)) {
                            errorsMatrix[realCol][realRow] = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * @return the number of errors in the shipboard.
     * @author Boti
     */
    public int getErrorCount() {
        checkErrors();
        int count = 0;
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                if (errorsMatrix[realCol][realRow])
                    count++;
            }
        }
        return count;
    }

    /**
     * Check if component is connected through a connector to any adjacent cell.
     * Empty cells are not connected, return false.
     * Used in checkErrors.
     *
     * @return true if component is connected to at least 1 neighbor, false if not.
     * @author Boti
     */
    private boolean checkConnectedToNeighbor(int realCol, int realRow) {
        Component current = componentMatrix[realCol][realRow];
        Component temp;

        // starter cabin cannot cause errors
        if (getComponentCount() == 1 && realCol == getRealIndex(SB_CENTER_COL) && realRow == getRealIndex(SB_CENTER_ROW))
            return true;

        // empty cells are not connected
        if (current == null)
            return false;

        // front
        temp = componentMatrix[realCol][realRow - 1];
        if (temp != null && isConnector(current.getFront()) && checkCompatibleJunction(current.getFront(), temp.getBack()))
            return true;

        // back
        temp = componentMatrix[realCol][realRow + 1];
        if (temp != null && isConnector(current.getBack()) && checkCompatibleJunction(current.getBack(), temp.getFront()))
            return true;

        // left
        temp = componentMatrix[realCol - 1][realRow];
        if (temp != null && isConnector(current.getLeft()) && checkCompatibleJunction(current.getLeft(), temp.getRight()))
            return true;

        // right
        temp = componentMatrix[realCol + 1][realRow];
        if (temp != null && isConnector(current.getRight()) && checkCompatibleJunction(current.getRight(), temp.getLeft()))
            return true;

        // no connections found
        return false;
    }

    /**
     * Checks if a component has correct junctions with its neighboring components.
     * Connection with empty neighbor is always correct.
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
        if (component.getFront().equals(SideType.Special) && componentMatrix[realCol][realRow - 1] != null)
            return true;
            // back cannon
        else if (component.getBack().equals(SideType.Special) && componentMatrix[realCol][realRow + 1] != null)
            return true;
            // right cannon
        else if (component.getRight().equals(SideType.Special) && componentMatrix[realCol + 1][realRow] != null)
            return true;
            // left cannon
        else if (component.getLeft().equals(SideType.Special) && componentMatrix[realCol - 1][realRow] != null)
            return true;

        // no errors found
        return false;
    }

    /**
     * Removes a component from the specified position.
     * Updates shipBoard, shipBoardAttributes, destroyedComponents counter.
     *
     * @throws IllegalArgumentException    if operation not possible.
     * @throws NoHumanCrewLeftException    if no human crew left and player forced to give up.
     * @throws FracturedShipBoardException if shipboard is fractured into multiple pieces.
     * @author Giacomo, Boti
     */
    public void removeComponent(int visibleCol, int visibleRow, boolean checkFracture)
            throws IllegalArgumentException, NoHumanCrewLeftException, FracturedShipBoardException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

        int realCol = getRealIndex(visibleCol);
        int realRow = getRealIndex(visibleRow);
        Component component = componentMatrix[realCol][realRow];

        if (component == null) {
            throw new IllegalArgumentException("No component present at the given coordinates.");
        }
        // present component to be removed

        // remove given component
        componentMatrix[realCol][realRow] = null;
        shipBoardAttributes.destroyComponents(1);
        // update shipboard attributes
        component.accept(new SBAttributesUpdaterVisitor(this));

        // check for fracture, throw exceptions if needed
        // components with 1 neighbor cannot fracture shipboard
        if (checkFracture && getNumberOfAllNeighbours(realCol, realRow) > 1) {
            checkFracturedShipBoard();
        }


    }

    /**
     * Check if the shipboard is fractured after removing components.
     *
     * @throws FracturedShipBoardException
     * @throws NoHumanCrewLeftException
     * @author Boti
     */
    private void checkFracturedShipBoard() throws FracturedShipBoardException, NoHumanCrewLeftException {
        // map possible valid shipboards
        List<ShipBoard> validShipBoardsList = possibleShipBoardsMapper();

        // if >1 possible shipboards: fracture
        if (validShipBoardsList.size() > 1)
            throw new FracturedShipBoardException(validShipBoardsList);

        // if 0 possible shipboards: no human crew left
        if (validShipBoardsList.isEmpty())
            throw new NoHumanCrewLeftException();

        // if 1 possible shipboard, ok
    }

    /**
     * Map all the different connected parts of the shipboard and return them as a list of the valid shipboards to choose from.
     * Invalid (no human crew) shipboards are automatically erased from the real shipboard.
     * Used when shipboard is fractured.
     *
     * @return list of possible valid shipboards to choose from.
     * @author Boti
     */
    private List<ShipBoard> possibleShipBoardsMapper() {
        // list of possible valid shipboards (only those with human crew)
        List<ShipBoard> validShipboardsList = new ArrayList<>();
        ShipBoard tmp;

        // add shipboard reachable from current center cabin
        tmp = bfsShipBoardMapper(centerCabinCol, centerCabinRow);
        if (tmp != null) {
            validShipboardsList.add(tmp);
        } else {
            // TODO new center should be chosen
        }

        // indicates if the component has already been mapped
        boolean mapped;

        // check remaining components
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                // if component of real shipboard not null
                if (componentMatrix[realCol][realRow] != null) {
                    mapped = false;
                    // check if component is mapped already
                    for (ShipBoard shipBoard : validShipboardsList) {
                        if (shipBoard.getRealComponent(realCol, realRow) != null) {
                            mapped = true;
                            break;
                        }
                    }
                    // component not yet mapped, map it
                    if (!mapped) {
                        tmp = bfsShipBoardMapper(realCol, realRow);
                        // only add if it is a valid possible shipboard
                        if (tmp != null) {
                            validShipboardsList.add(tmp);
                        }
                    }
                    // component mapped
                }
            }
        }
        // all components mapped

        // return possible valid shipboards to choose from
        return validShipboardsList;
    }

    /**
     * Create a new shipboard from the components reachable from the given starting coordinates and return it,
     * if it has human crew (possible choice).
     * Return null and erase the mapped shipboard from the real shipboard if it has no human crew.
     *
     * @return the shipboard of reachable components, or null if mapped shipboard invalid (no human crew).
     * @author Boti
     */
    private ShipBoard bfsShipBoardMapper(int realCol, int realRow) {
        if (componentMatrix[realCol][realRow] == null)
            throw new IllegalArgumentException("bfsShipBoardMapper cannot start from empty cell.");

        // create new shipboard
        // center cabin is automatically added - to remove!
        ShipBoard tmpShipboard = new ShipBoard(gameType);

        // remove center cabin added by shipboard constructor, if current mapping is not starting from center cabin
        if (realCol != getRealIndex(SB_CENTER_COL) || realRow != getRealIndex(SB_CENTER_ROW)) {
            tmpShipboard.componentMatrix[getRealIndex(SB_CENTER_COL)][getRealIndex(SB_CENTER_ROW)] = null;
        }

        // store coordinates, not components - search based on coordinates
        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();

        // contains doesn't work with List<Integer[]> array entries
        Coordinate currentCoord, neighborCoord;
        Component currentComp, neighborComp;

        // add starting coordinates, component
        currentCoord = new Coordinate(realCol, realRow);
        tmpShipboard.componentMatrix[currentCoord.getCol()][currentCoord.getRow()] = getRealComponent(currentCoord.getCol(), currentCoord.getRow());
        visited.add(currentCoord);
        queue.offer(currentCoord);

        // empty queue
        while (!queue.isEmpty()) {
            // get current node
            currentCoord = queue.poll();
            currentComp = getRealComponent(currentCoord.getCol(), currentCoord.getRow());

            // if current component a cabin with human inhabitants, set as new center for the tmp shipboard
            if (currentComp instanceof Cabin && currentComp.getCrewMembers() > 0 &&
                    ((Cabin) currentComp).getCrewType().equals(CrewType.Human)) {
                tmpShipboard.centerCabinCol = currentCoord.getCol();
                tmpShipboard.centerCabinRow = currentCoord.getRow();
            }

            // check neighbors (bfs)

            // front
            neighborCoord = new Coordinate(currentCoord.getCol(), currentCoord.getRow() - 1);
            neighborComp = getRealComponent(neighborCoord.getCol(), neighborCoord.getRow());
            // check not yet visited connected neighbor
            if (!visited.contains(neighborCoord) && neighborComp != null &&
                    checkCompatibleJunction(currentComp.getFront(), neighborComp.getBack())) {
                tmpShipboard.componentMatrix[neighborCoord.getCol()][neighborCoord.getRow()] = neighborComp;
                visited.add(neighborCoord);
                queue.offer(neighborCoord);
            }

            // back
            neighborCoord = new Coordinate(currentCoord.getCol(), currentCoord.getRow() + 1);
            neighborComp = getRealComponent(neighborCoord.getCol(), neighborCoord.getRow());
            // check not yet visited connected neighbor
            if (!visited.contains(neighborCoord) && neighborComp != null &&
                    checkCompatibleJunction(currentComp.getBack(), neighborComp.getFront())) {
                tmpShipboard.componentMatrix[neighborCoord.getCol()][neighborCoord.getRow()] = neighborComp;
                visited.add(neighborCoord);
                queue.offer(neighborCoord);
            }

            // left
            neighborCoord = new Coordinate(currentCoord.getCol() - 1, currentCoord.getRow());
            neighborComp = getRealComponent(neighborCoord.getCol(), neighborCoord.getRow());
            // check not yet visited connected neighbor
            if (!visited.contains(neighborCoord) && neighborComp != null &&
                    checkCompatibleJunction(currentComp.getLeft(), neighborComp.getRight())) {
                tmpShipboard.componentMatrix[neighborCoord.getCol()][neighborCoord.getRow()] = neighborComp;
                visited.add(neighborCoord);
                queue.offer(neighborCoord);
            }

            // right
            neighborCoord = new Coordinate(currentCoord.getCol() + 1, currentCoord.getRow());
            neighborComp = getRealComponent(neighborCoord.getCol(), neighborCoord.getRow());
            // check not yet visited connected neighbor
            if (!visited.contains(neighborCoord) && neighborComp != null &&
                    checkCompatibleJunction(currentComp.getRight(), neighborComp.getLeft())) {
                tmpShipboard.componentMatrix[neighborCoord.getCol()][neighborCoord.getRow()] = neighborComp;
                visited.add(neighborCoord);
                queue.offer(neighborCoord);
            }
        }
        // all reachable components visited

        // update mapped shipboard attributes (for human crew and others)
        // (not using addComponent to avoid placement conflicts, attributes must be updated manually)
        try {
            tmpShipboard.getShipBoardAttributes().updateShipBoardAttributes(tmpShipboard);

        } catch (NoHumanCrewLeftException e) {
            // tmp shipboard has no crew left:
            // fall off: erase from real shipboard and return null
            try {
                eraseShipboardFromRealShipboard(tmpShipboard);
            } catch (NoHumanCrewLeftException ex) {
                // real shipboard has no crew left
                // shouldn't be thrown: only parts without crew are automatically erased from the real shipboard
                throw new IllegalStateException("Error: automatically erased last part with human crew from shipboard.");
            }
            // mapped shipboard discarded
            return null;
        }

        // crew present on mapped shipboard, valid shipboard option
        return tmpShipboard;
    }

    /**
     * @return the component at the given real coordinates.
     */
    public Component getRealComponent(int realCol, int realRow) {
        return componentMatrix[realCol][realRow];
    }

    public ShipBoardAttributes getShipBoardAttributes() {
        return shipBoardAttributes;
    }

    /**
     * Remove the passed shipboard's components from the real shipboard.
     * Updates the real shipBoard, shipBoardAttributes and destroyComponent counter.
     * Used to remove parts fallen off from the real shipboard.
     *
     * @author Boti
     */
    public void eraseShipboardFromRealShipboard(ShipBoard toErase) throws NoHumanCrewLeftException {
        // find components of the shipboard to erase
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                if (toErase.getRealComponent(realCol, realRow) != null) {
                    // remove element without checking for fracture (to avoid infinite loop)
                    // remove updates attributes
                    try {
                        this.removeComponent(getVisibleIndex(realCol), getVisibleIndex(realRow), false);
                    } catch (FracturedShipBoardException e) {
                        throw new IllegalStateException("Error: entering infinite checkFracture loop.");
                    }
                }
            }
        }
    }

    /**
     * Counts the number of neighbours (unconnected or connected) for the given cell. Works with empty center cells too.
     *
     * @return the number of neighbours (connected or unconnected).
     * @author Boti
     */
    private int getNumberOfAllNeighbours(int realCol, int realRow) {
        int count = 0;

        // front
        if (componentMatrix[realCol][realRow - 1] != null)
            count++;
        // back
        if (componentMatrix[realCol][realRow + 1] != null)
            count++;
        // left
        if (componentMatrix[realCol - 1][realRow] != null)
            count++;
        // right
        if (componentMatrix[realCol + 1][realRow] != null)
            count++;

        return count;
    }

    // TODO
    public void findNewCenterCabin() {
        return;
    }

    public Component[][] getComponentMatrix() {
        return componentMatrix;
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
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                Component component = componentMatrix[realCol][realRow];
                if (component != null) {
                    // check front
                    if (isConnector(component.getFront()) && componentMatrix[realCol][realRow - 1] == null)
                        externalJunctions++;
                    // check back
                    if (isConnector(component.getBack()) && componentMatrix[realCol][realRow + 1] == null)
                        externalJunctions++;
                    // check left
                    if (isConnector(component.getLeft()) && componentMatrix[realCol - 1][realRow] == null)
                        externalJunctions++;
                    // check right
                    if (isConnector(component.getRight()) && componentMatrix[realCol + 1][realRow] == null)
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
    public void removeBattery(int visibleCol, int visibleRow) throws IllegalArgumentException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Battery))
            throw new IllegalArgumentException("The selected component is not a battery");

        if (component.getBatteryPower() - 1 >= 0) {
            ((Battery) component).removeBattery();
            // update shipboard attributes
            try {
                component.accept(new SBAttributesUpdaterVisitor(this));
            } catch (NoHumanCrewLeftException e) {
                throw new IllegalStateException("Error: no human crew left after removing battery.");
            }
        } else
            throw new IllegalArgumentException("Not enough batteries at the selected component.");
    }

    /**
     * Remove a crew member from the cabin at the given coordinates, if possible.
     * Accounts for humans and aliens.
     * Updates shipBoardAttributes.
     *
     * @throws NoHumanCrewLeftException if no human crew left after removal and player forced to give up.
     * @throws IllegalArgumentException if removal operation not possible.
     * @author Boti
     */
    public void removeCrewMember(int visibleCol, int visibleRow) throws IllegalArgumentException, NoHumanCrewLeftException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalArgumentException("The selected component is not a cabin");

        // crew >=1 on cabin
        // throw NoHumanCrewLeftException if no crew remains on shipboard after removal
        if (component.getCrewMembers() >= 1) {
            ((Cabin) component).removeInhabitant();
            // update shipboard attributes
            component.accept(new SBAttributesUpdaterVisitor(this));
        }
        // 0 crew on cabin
        else {
            throw new IllegalArgumentException("Not enough crew members at the selected component.");
        }
    }

    /**
     * Sets the crewType of the cabin at the selected coordinates, if possible.
     * Checks for alien supports.
     * Denies change to alien crew if no human crew would be left.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void setCrewType(int visibleCol, int visibleRow, CrewType crewType) throws IllegalArgumentException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalArgumentException("The selected component is not a cabin");

        // save original data if component is a cabin
        Cabin cabin = (Cabin) component;
        int originalCrewCount = cabin.getCrewMembers();
        CrewType originalCrewType = cabin.getCrewType();

        // no crew in cabin
        if (originalCrewCount <= 0)
            throw new IllegalArgumentException("The selected cabin has no crew members.");

        // no crew would be left after changing crewType from Human
        if (cabin.getCrewType().equals(CrewType.Human) && !crewType.equals(CrewType.Human) &&
                (shipBoardAttributes.getHumanCrewMembers() - cabin.getCrewMembers() <= 0)) {
            throw new IllegalArgumentException("Cannot change crew type from human: no humans would be left.");
        }

        // change only if needed
        if (!originalCrewType.equals(crewType)) {
            // alien checks
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Purple) || crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Brown)) {
                if (shipBoardAttributes.getAlien(crewType))
                    throw new IllegalArgumentException("Cannot change crew to alien: the alien is already present elsewhere.");

                if (!checkForAlienSupport(col, row, crewType))
                    throw new IllegalArgumentException("Cannot change crew to alien: no alien support nearby.");
            }

            // all conditions met to change crew type
            ((Cabin) component).setCrewType(crewType);
            try {
                component.accept(new SBAttributesUpdaterVisitor(this));
            } catch (NoHumanCrewLeftException e) {
                throw new IllegalStateException("Error: no human crew left after setting crew type.");
            }

        }
    }

    /**
     * Check if component at given coordinates is connected to an alien support of given type.
     *
     * @return true if connected to given alien support, false if not connected.
     * @author Boti
     */
    public boolean checkForAlienSupport(int realCol, int realRow, CrewType crewType) {
        Component component = componentMatrix[realCol][realRow];
        Component temp;
        // check front
        temp = componentMatrix[realCol][realRow - 1];
        if (temp != null && checkCompatibleJunction(component.getFront(), temp.getBack()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check back
        temp = componentMatrix[realCol][realRow + 1];
        if (temp != null && checkCompatibleJunction(component.getBack(), temp.getFront()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check left
        temp = componentMatrix[realCol - 1][realRow];
        if (temp != null && checkCompatibleJunction(component.getLeft(), temp.getRight()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // check right
        temp = componentMatrix[realCol + 1][realRow];
        if (temp != null && checkCompatibleJunction(component.getRight(), temp.getLeft()) && temp instanceof AlienSupport) {
            // check for purple
            if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Purple) && ((AlienSupport) temp).isPurple())
                return true;
                // check for brown
            else if (crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Brown) && !((AlienSupport) temp).isPurple())
                return true;
        }
        // no matching support found
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
     * Moves goods from the storage at the starting coordinates to the storage at the final coordinates, if possible.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void moveGoods(int visibleColStarter, int visibleRowStarter, int visibleColFinal, int visibleRowFinal, int[] goods)
            throws IllegalArgumentException {
        // removeGoods throws IllegalArgumentException if not possible
        removeGoods(visibleColStarter, visibleRowStarter, goods);

        // try adding goods, revert if not possible
        try {
            addGoods(visibleColFinal, visibleRowFinal, goods);
        } catch (Exception e) {
            // revert changes
            addGoods(visibleColStarter, visibleRowStarter, goods);
            throw e;
        }
    }

    /**
     * Remove goods from the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if operation not possible.
     * @author Boti
     */
    public void removeGoods(int visibleCol, int visibleRow, int[] goods) throws IllegalArgumentException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalArgumentException("Coordinates out of bounds.");

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
        try {
            component.accept(new SBAttributesUpdaterVisitor(this));
        } catch (NoHumanCrewLeftException e) {
            throw new IllegalStateException("Error: no human crew left after removing goods.");
        }
    }

    /**
     * Scan the shipboard and return the list of the visible coordinates of joined cabins with inhabitants.
     * Used by the Epidemic card.
     *
     * @return the list of the visible coordinates of joined cabins with inhabitants.
     * @author Ludo, Boti
     */
    public List<int[]> getJoinedCabinsVisibleCoordinates() {
        List<int[]> coordinatesList = new ArrayList<>();
        Component current, temp;

        // find cabins
        for (int realCol = SB_FIRST_REAL_COL; realCol <= SB_LAST_REAL_COL; realCol++) {
            for (int realRow = SB_FIRST_REAL_ROW; realRow <= SB_LAST_REAL_ROW; realRow++) {
                current = componentMatrix[realCol][realRow];
                if (current instanceof Cabin && current.getCrewMembers() > 0) {
                    // check if current cabin is connected to a neighbor cabin

                    // front
                    temp = componentMatrix[realCol][realRow - 1];
                    if (temp instanceof Cabin && temp.getCrewMembers() > 0 && isConnector(current.getFront())
                            && checkCompatibleJunction(current.getFront(), temp.getBack())) {
                        coordinatesList.add(new int[]{getVisibleIndex(realCol), getVisibleIndex(realRow)});
                        // jump to next cell
                        continue;
                    }
                    // back
                    temp = componentMatrix[realCol][realRow + 1];
                    if (temp instanceof Cabin && temp.getCrewMembers() > 0 && isConnector(current.getBack())
                            && checkCompatibleJunction(current.getBack(), temp.getFront())) {
                        coordinatesList.add(new int[]{getVisibleIndex(realCol), getVisibleIndex(realRow)});
                        // jump to next cell
                        continue;
                    }
                    // left
                    temp = componentMatrix[realCol - 1][realRow];
                    if (temp instanceof Cabin && temp.getCrewMembers() > 0 && isConnector(current.getLeft())
                            && checkCompatibleJunction(current.getLeft(), temp.getRight())) {
                        coordinatesList.add(new int[]{getVisibleIndex(realCol), getVisibleIndex(realRow)});
                        // jump to next cell
                        continue;
                    }
                    // right
                    temp = componentMatrix[realCol + 1][realRow];
                    if (temp instanceof Cabin && temp.getCrewMembers() > 0 && isConnector(current.getRight())
                            && checkCompatibleJunction(current.getRight(), temp.getLeft())) {
                        coordinatesList.add(new int[]{getVisibleIndex(realCol), getVisibleIndex(realRow)});
                        // jump to next cell
                        continue;
                    }
                }
            }
        }
        // finished scanning
        return coordinatesList;
    }

    /**
     * @return the visibleIndex from the passed realIndex.
     */
    private int getVisibleIndex(int realIndex) {
        return realIndex + 1;
    }

}
