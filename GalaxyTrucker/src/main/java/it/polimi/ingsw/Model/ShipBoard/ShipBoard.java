package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;

import java.io.Serializable;
import java.util.*;

/**
 * Shipboard used to create and store the player's ship and it's attributes.
 * <p>
 * Indexes:
 * Player uses visible indexes [1...12] (center: 7-7),
 * Shipboard uses real indexes shifted by -1 [0...11] (center: 6-6).
 * <p>
 * RealIndex = VisibleIndex - 1.
 * <p>
 * Matrix structure:
 * Shipboard[cols][rows].
 *
 * @author Giacomo, Boti
 */
public class ShipBoard implements Serializable {
    // All attributes must be Serializable or transient
    // visible values
    // visible coordinate bounds: [1, 12]
    // real coordinate bounds: [0, 12-1]
    public static final int COLS = 12;
    public static final int ROWS = 12;
    public static final int CENTER_COL = 7;
    public static final int CENTER_ROW = 7;

    // components in componentMatrix in [FIRST_REAL_COL/ROW, LAST_REAL_COL/ROW] (included)

    /*
    Cycle to iterate componentMatrix:
    for (int realCol = ShipBoard.FIRST_REAL_COL; realCol <= ShipBoard.LAST_REAL_COL; realCol++) {
            for (int realRow = ShipBoard.FIRST_REAL_ROW; realRow <= ShipBoard.LAST_REAL_ROW; realRow++) {
     */


    // the first real column and row that can contain components
    public static final int FIRST_REAL_COL = 3;
    public static final int FIRST_REAL_ROW = 4;

    // the last real column and row that can contain components (included!)
    public static final int LAST_REAL_COL = 9;
    public static final int LAST_REAL_ROW = 8;

    // final Object: reference cannot be changed (but state/elements can change)
    private final GameType gameType;
    private final Color color;
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
    private List<ShipBoard> possibleFracturedShipBoards;

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
    public ShipBoard(GameType gameType, Color color) {
        this.gameType = gameType;
        this.color = color;
        this.shipBoardAttributes = new ShipBoardAttributes(this);
        this.componentMatrix = new Component[COLS][ROWS];
        this.validityMatrix = new boolean[COLS][ROWS];
        this.errorsMatrix = new boolean[COLS][ROWS];

        // Initialize component matrix as empty
        for (int realCol = 0; realCol < COLS; realCol++) {
            for (int realRow = 0; realRow < ROWS; realRow++) {
                componentMatrix[realCol][realRow] = null;
            }
        }
        // Initialize all positions as valid
        for (int realCol = 0; realCol < COLS; realCol++) {
            for (int realRow = 0; realRow < ROWS; realRow++) {
                validityMatrix[realCol][realRow] = true;
            }
        }
        // Initialize error matrix as error free
        for (int realCol = 0; realCol < COLS; realCol++) {
            for (int realRow = 0; realRow < ROWS; realRow++) {
                errorsMatrix[realCol][realRow] = false;
            }
        }

        // Set forbidden zones for component placement
        // Set forbidden zones in the structure
        // forbidden rows
        for (int realCol = 0; realCol < COLS; realCol++) {
            validityMatrix[realCol][0] = false;
            validityMatrix[realCol][1] = false;
            validityMatrix[realCol][2] = false;
            validityMatrix[realCol][3] = false;
            validityMatrix[realCol][9] = false;
            validityMatrix[realCol][10] = false;
            validityMatrix[realCol][11] = false;

        }
        // forbidden columns
        for (int realRow = 0; realRow < ROWS; realRow++) {
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
            for (int realRow = 0; realRow < ROWS; realRow++) {
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

    /**
     * Add the starter cabin to a new shipboard.
     */
    private void addStarterCabin() {
        Component starterCabin = Cabin.getStarterCabin(color);
        try {
            addComponent(CENTER_COL, CENTER_ROW, starterCabin);
        } catch (NotPermittedPlacementException | IllegalSelectionException e) {
            System.out.println("Couldn't add starter cabin");
        }
        // set center
        centerCabinCol = getRealIndex(CENTER_COL);
        centerCabinRow = getRealIndex(CENTER_ROW);
        try {
            starterCabin.accept(new SBAttributesUpdaterVisitor(this));
        } catch (NoHumanCrewLeftException e) {
            throw new IllegalStateException("Error: no human crew after adding starter cabin.");
        }
    }

    /**
     * Alternative parameter order to improve readability during testing.
     */
    public void addComponent(int visibleCol, int visibleRow, Component component) throws NotPermittedPlacementException, IllegalSelectionException {
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
    public void addComponent(Component component, int visibleCol, int visibleRow) throws NotPermittedPlacementException, IllegalSelectionException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

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
    public static int getRealIndex(int visibleIndex) {
        return visibleIndex - 1;
    }

    /**
     * Checks if the given visible coordinates respect the bounds of shipboard coordinates.
     * Coordinates in bounds are [1, max] (included).
     * Real coordinates are visible - 1: [0, max-1]
     *
     * @return true if coordinates are in bounds, false if out of bounds.
     * @author Boti
     */
    public static boolean checkCoordinatesInBounds(int visibleCol, int visibleRow) {
        // coordinates must be between [1, max] (included)
        // 0, max+1 out of bounds! - error in arrays
        return (visibleCol >= 1 && visibleRow >= 1 && visibleCol <= COLS && visibleRow <= ROWS);
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
        if ((getComponentCount() == 0) && (realCol == getRealIndex(CENTER_COL))
                && (realRow == getRealIndex(CENTER_ROW)))
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
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
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
    public boolean checkNotEmptyNeighbors(int realCol, int realRow) {
        // should be a private method, shouldn't be used in Assembly
        if (!checkCoordinatesInBounds(getVisibleIndex(realCol), getVisibleIndex(realRow)))
            System.err.println("Error: coordinates out of bounds");

        return (componentMatrix[realCol - 1][realRow] != null || componentMatrix[realCol + 1][realRow] != null ||
                componentMatrix[realCol][realRow - 1] != null || componentMatrix[realCol][realRow + 1] != null);
    }

    /**
     * @return the visibleIndex from the passed realIndex.
     */
    public static int getVisibleIndex(int realIndex) {
        return realIndex + 1;
    }

    public List<ShipBoard> getPossibleFracturedShipBoards() {
        return possibleFracturedShipBoards;
    }

    public GameType getGameType() {
        return gameType;
    }

    /**
     * Fills the shipboard with all kinds of prewritten components.
     * Adds: engines, cabins, cannons, batteries, storages, shields,
     * Used for testing, to skip AssemblyPhase.
     *
     * @author Boti
     */
    public void preBuildShipBoard() {
        SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

        // NORMALGAME
        if (gameType.equals(GameType.NORMALGAME)) {
            try {
                /*
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
                this.addComponent(new AlienSupport(universalSides, false), 6, 8);


                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, false), 7, 6);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 8, 6);
                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, false), 8, 5);

                this.addComponent(new Cabin(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Universal}, CrewType.Human, 2), 6, 7);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 7);
                this.addComponent(new AlienSupport(universalSides, true), 4, 7);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 8);
                this.addComponent(new Cabin(universalSides, CrewType.Purple, 1), 4, 8);

                this.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Single, SideType.Universal}, 9), 8, 7);
                this.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Single, SideType.Universal}, 9), 9, 7);

                this.addComponent(new Storage(universalSides, false, 3), 8, 8);
                this.addComponent(new Storage(universalSides, false, 3), 9, 8);
                this.addComponent(new Storage(universalSides, true, 2), 10, 8);
                this.addComponent(new Storage(universalSides, true, 1), 8, 9);
                this.addComponent(new Storage(universalSides, true, 2), 9, 9);
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, false), 4, 9);
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 5, 9);
                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, false), 9, 6);
                this.addComponent(new Shield(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, 0, 1), 6, 6);
                this.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Single, SideType.Universal}, 9), 10, 7);

                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 6, 5);
                this.addComponent(new Shield(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Smooth}, 2, 3), 5, 6);
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, false), 6, 9);
                addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Special, SideType.Smooth, SideType.Universal}, false), 10, 9);
                */
                this.addComponent(new Shield("/Polytechnic/tiles/GT-new_tiles_16_for web154.jpg", 1, new SideType[]{SideType.Double, SideType.Single, SideType.Double, SideType.Single}, 2, 3), 6, 7);
                this.addComponent(new Shield("/Polytechnic/tiles/GT-new_tiles_16_for web153.jpg", 0, new SideType[]{SideType.Smooth, SideType.Double, SideType.Double, SideType.Double}, 0, 1), 7, 6);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web96.jpg", 0, new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single}, false), 7, 8);
                this.addComponent(new Component("/Polytechnic/tiles/GT-new_tiles_16_for web55.jpg", 1, new SideType[]{SideType.Universal, SideType.Single, SideType.Universal, SideType.Single}), 8, 7);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web4.jpg", 2, new SideType[]{SideType.Double, SideType.Single, SideType.Double, SideType.Single}, 2), 8, 8);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web76.jpg", 0, new SideType[]{SideType.Double, SideType.Smooth, SideType.Special, SideType.Smooth}, true), 8, 9);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web64.jpg", 1, new SideType[]{SideType.Universal, SideType.Single, SideType.Single, SideType.Single}, true, 1), 6, 8);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web66.jpg", 1, new SideType[]{SideType.Universal, SideType.Double, SideType.Double, SideType.Double}, true, 1), 8, 6);
                this.addComponent(new Cabin("/Polytechnic/tiles/GT-new_tiles_16_for web51.jpg", 0, new SideType[]{SideType.Double, SideType.Double, SideType.Smooth, SideType.Universal}, CrewType.Human, 2), 9, 7);
                this.addComponent(new Component("/Polytechnic/tiles/GT-new_tiles_16_for web53.jpg", 2, new SideType[]{SideType.Smooth, SideType.Single, SideType.Universal, SideType.Universal}), 9, 8);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web77.jpg", 0, new SideType[]{SideType.Double, SideType.Smooth, SideType.Special, SideType.Smooth}, true), 9, 9);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web2.jpg", 3, new SideType[]{SideType.Double, SideType.Smooth, SideType.Smooth, SideType.Universal}, 2), 10, 8);
                this.addComponent(new Cannon("/Polytechnic/tiles/GT-new_tiles_16_for web130.jpg", 1, new SideType[]{SideType.Smooth, SideType.Special, SideType.Universal, SideType.Double}, false), 10, 7);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web17.jpg", 1, new SideType[]{SideType.Single, SideType.Smooth, SideType.Smooth, SideType.Smooth}, 3), 6, 9);
                this.addComponent(new AlienSupport("/Polytechnic/tiles/GT-new_tiles_16_for web146.jpg", 0, new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Double, SideType.Universal}, true), 9, 6);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web3.jpg", 1, new SideType[]{SideType.Smooth, SideType.Universal, SideType.Double, SideType.Single}, 2), 6, 6);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web92.jpg", 0, new SideType[]{SideType.Single, SideType.Smooth, SideType.Special, SideType.Smooth}, false), 5, 9);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web6.jpg", 2, new SideType[]{SideType.Universal, SideType.Single, SideType.Single, SideType.Single}, 2), 5, 8);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web68.jpg", 3, new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Single, SideType.Smooth}, true, 2), 8, 5);
                this.addComponent(new Cannon("/Polytechnic/tiles/GT-new_tiles_16_for web113.jpg", 2, new SideType[]{SideType.Double, SideType.Single, SideType.Special, SideType.Smooth}, true), 4, 8);
                this.addComponent(new Cabin("/Polytechnic/tiles/GT-new_tiles_16_for web36.jpg", 2, new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Double}, CrewType.Human, 2), 5, 7);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web26.jpg", 2, new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Smooth}, false, 2), 4, 7);
                this.addComponent(new AlienSupport("/Polytechnic/tiles/GT-new_tiles_16_for web140.jpg", 3, new SideType[]{SideType.Smooth, SideType.Universal, SideType.Single, SideType.Smooth}, false), 5, 6);
            } catch (NotPermittedPlacementException | IllegalSelectionException e) {
                throw new IllegalStateException("The prebuilt shipboard has illegal placements.");
            }
        }

        // TESTGAME
        else {
            try {
                /*
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 7, 8);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 6, 8);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 7, 6);
                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, false), 7, 5);
                this.addComponent(new Cannon(new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal}, true), 8, 6);
                this.addComponent(new Cabin(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Universal}, CrewType.Human, 2), 6, 7);
                this.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 7);
                this.addComponent(new Cannon(new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Special}, true), 5, 8);
                this.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Single, SideType.Universal}, 9), 8, 7);
                this.addComponent(new Battery(new SideType[]{SideType.Universal, SideType.Universal, SideType.Single, SideType.Universal}, 9), 9, 7);
                this.addComponent(new Storage(universalSides, false, 3), 8, 8);
                this.addComponent(new Storage(universalSides, false, 3), 9, 8);
                this.addComponent(new Storage(universalSides, true, 1), 8, 9);
                this.addComponent(new Storage(universalSides, true, 2), 9, 9);
                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, true), 5, 9);

                this.addComponent(new Shield(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, 0, 1), 6, 6);

                this.addComponent(new Engine(new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal}, false), 6, 9);


                 */
                this.addComponent(new Shield("/Polytechnic/tiles/GT-new_tiles_16_for web154.jpg", 1, new SideType[]{SideType.Double, SideType.Single, SideType.Double, SideType.Single}, 2, 3), 6, 7);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web96.jpg", 0, new SideType[]{SideType.Single, SideType.Single, SideType.Special, SideType.Single}, false), 7, 8);
                this.addComponent(new Component("/Polytechnic/tiles/GT-new_tiles_16_for web55.jpg", 1, new SideType[]{SideType.Universal, SideType.Single, SideType.Universal, SideType.Single}), 8, 7);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web4.jpg", 2, new SideType[]{SideType.Double, SideType.Single, SideType.Double, SideType.Single}, 2), 8, 8);
                this.addComponent(new Engine("/Polytechnic/tiles/GT-new_tiles_16_for web76.jpg", 0, new SideType[]{SideType.Double, SideType.Smooth, SideType.Special, SideType.Smooth}, true), 8, 9);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web64.jpg", 1, new SideType[]{SideType.Universal, SideType.Single, SideType.Single, SideType.Single}, true, 1), 6, 8);
                this.addComponent(new Shield("/Polytechnic/tiles/GT-new_tiles_16_for web152.jpg", 0, new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Universal, SideType.Double}, 0, 1), 8, 6);
                this.addComponent(new Cabin("/Polytechnic/tiles/GT-new_tiles_16_for web44.jpg", 3, new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Double, SideType.Universal}, CrewType.Human, 2), 9, 7);
                this.addComponent(new Cannon("/Polytechnic/tiles/GT-new_tiles_16_for web122.jpg", 1, new SideType[]{SideType.Double, SideType.Special, SideType.Single, SideType.Universal}, true), 9, 8);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web21.jpg", 1, new SideType[]{SideType.Universal, SideType.Smooth, SideType.Smooth, SideType.Smooth}, false, 2), 9, 9);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web11.jpg", 2, new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Smooth}, 2), 5, 7);
                this.addComponent(new Cannon("/Polytechnic/tiles/GT-new_tiles_16_for web115.jpg", 3, new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Special}, true), 5, 8);
                this.addComponent(new Cabin("/Polytechnic/tiles/GT-new_tiles_16_for web35.jpg", 1, new SideType[]{SideType.Single, SideType.Smooth, SideType.Smooth, SideType.Universal}, CrewType.Human, 2), 6, 9);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web26.jpg", 1, new SideType[]{SideType.Universal, SideType.Universal, SideType.Smooth, SideType.Smooth}, false, 2), 5, 9);
                this.addComponent(new Battery("/Polytechnic/tiles/GT-new_tiles_16_for web6.jpg", 1, new SideType[]{SideType.Single, SideType.Universal, SideType.Single, SideType.Single}, 2), 7, 6);
                this.addComponent(new Cannon("/Polytechnic/tiles/GT-new_tiles_16_for web126.jpg", 0, new SideType[]{SideType.Special, SideType.Smooth, SideType.Single, SideType.Smooth}, false), 7, 5);
                this.addComponent(new Storage("/Polytechnic/tiles/GT-new_tiles_16_for web67.jpg", 2, new SideType[]{SideType.Smooth, SideType.Universal, SideType.Universal, SideType.Smooth}, true, 1), 6, 6);


            } catch (NotPermittedPlacementException | IllegalSelectionException e) {
                throw new IllegalStateException("The prebuilt shipboard has illegal placements.");
            }
        }

        if (this.isErroneous())
            throw new IllegalStateException("The prebuilt shipboard has errors.");
    }

    /**
     * @return the valid positions matrix.
     */
    public boolean[][] getValidityMatrix() {
        return validityMatrix;
    }

    /**
     * @return the erroneous components matrix.
     */
    public boolean[][] getErrorsMatrix() {
        return errorsMatrix;
    }

    /**
     * @return the component at the given visible coordinates.
     */
    public Component getComponent(int visibleCol, int visibleRow) {
        // do not throw exception (crash), but signal errors
        // SufferBlows methods should be implemented in ShipBoard
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            System.err.println("Error: coordinates out of bounds in getComponents()");

        return componentMatrix[getRealIndex(visibleCol)][getRealIndex(visibleRow)];
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
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            // iterate rows
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
                // default: no error in cell
                // override if cell erroneous
                errorsMatrix[realCol][realRow] = false;
                component = componentMatrix[realCol][realRow];


                // if component is present, check for errors
                if (component != null) {
                    // check if connected to a neighbor
                    if (!checkConnectedToAnyNeighbor(realCol, realRow)) {
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
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
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
    private boolean checkConnectedToAnyNeighbor(int realCol, int realRow) {
        Component current = componentMatrix[realCol][realRow];
        Component temp;

        // starter cabin cannot cause errors
        if (getComponentCount() == 1 && realCol == getRealIndex(CENTER_COL) && realRow == getRealIndex(CENTER_ROW))
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
     * Updates shipBoard, shipBoardAttributes, destroyedComponents, centerCabinCoordinates (if center cabin is removed).
     * Calls check for fracture if flag is set.
     *
     * @throws NoHumanCrewLeftException    if no human crew left and player forced to give up.
     * @throws FracturedShipBoardException if shipboard is fractured into multiple pieces.
     * @author Giacomo, Boti
     */
    public void removeComponent(int visibleCol, int visibleRow, boolean checkFracture)
            throws IllegalSelectionException, NoHumanCrewLeftException, FracturedShipBoardException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int realCol = getRealIndex(visibleCol);
        int realRow = getRealIndex(visibleRow);
        Component component = componentMatrix[realCol][realRow];

        if (component == null) {
            throw new IllegalSelectionException("No component present at the given coordinates.");
        }
        // present component to be removed

        // remove given component
        componentMatrix[realCol][realRow] = null;
        shipBoardAttributes.destroyComponents(1);

        // if center cabin is removed, find new center
        if (componentMatrix[centerCabinCol][centerCabinRow] == null) {
            findNewCenterCabin();
        }

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
     * The shipboard is fractured if there are >1 disconnected parts with human crew >0, so the part to keep must be chosen
     * using the ExceptionsHandler utility class.
     *
     * @throws FracturedShipBoardException if the shipboard is fractured.
     * @author Boti
     */
    public void checkFracturedShipBoard() throws FracturedShipBoardException {
        // map possible valid shipboards
        List<ShipBoard> validShipBoardsList = possibleShipBoardsMapper();

        // if >1 possible shipboards: fracture
        if (validShipBoardsList.size() > 1) {
            // update destroyedComponents for possible shipboards
            for (ShipBoard tmp : validShipBoardsList) {
                tmp.getShipBoardAttributes().setDestroyedComponents(this.getShipBoardAttributes().getDestroyedComponents());
            }

            // copy possible shipboards into attribute
            possibleFracturedShipBoards = new ArrayList<>(validShipBoardsList);
            throw new FracturedShipBoardException(validShipBoardsList);
        }

        // signal error in fracture logic
        if (validShipBoardsList.isEmpty())
            throw new IllegalStateException("Error: last part with human crew has been removed during fracture.");

        // if 1 possible shipboard, ok, reset list
        possibleFracturedShipBoards = new ArrayList<>();
    }

    /**
     * Finds a new center cabin (human crew >0) for the shipboard, and sets the centerCabin coordinates.
     * To call if the previous center cabin has been removed.
     *
     * @author Boti
     */
    private void findNewCenterCabin() {
        Component component;
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
                component = componentMatrix[realCol][realRow];
                // if current component is a cabin with human crew > 0
                if (component instanceof Cabin && ((Cabin) component).getCrewType().equals(CrewType.Human)
                        && component.getCrewMembers() > 0) {
                    // elect as new center cabin
                    centerCabinCol = realCol;
                    centerCabinRow = realRow;
                    break;
                }
            }
        }
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
        // current center cannot be null (always updated by remove before calling checkFracture)
        if (tmp == null)
            throw new IllegalStateException("Error: center cabin is null while calling checkFracture.");
        validShipboardsList.add(tmp);

        // indicates if the component has already been mapped
        boolean mapped;

        // check if remaining components have all been mapped
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
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
        ShipBoard tmpShipboard = new ShipBoard(gameType, color);

        // remove center cabin added by shipboard constructor, if current mapping is not starting from center cabin
        if (realCol != getRealIndex(CENTER_COL) || realRow != getRealIndex(CENTER_ROW)) {
            tmpShipboard.componentMatrix[getRealIndex(CENTER_COL)][getRealIndex(CENTER_ROW)] = null;
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
                    isConnector(currentComp.getFront()) &&
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
                    isConnector(currentComp.getBack()) &&
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
                    isConnector(currentComp.getLeft()) &&
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
                    isConnector(currentComp.getRight()) &&
                    checkCompatibleJunction(currentComp.getRight(), neighborComp.getLeft())) {
                tmpShipboard.componentMatrix[neighborCoord.getCol()][neighborCoord.getRow()] = neighborComp;
                visited.add(neighborCoord);
                queue.offer(neighborCoord);
            }
        }
        // all reachable components visited

        // update mapped shipboard cabins attributes (for human crew)
        // (not using addComponent to avoid placement conflicts, attributes must be updated manually)
        try {
            tmpShipboard.getShipBoardAttributes().updateCabinsAlienSupports(tmpShipboard);

        } catch (NoHumanCrewLeftException e) {
            // tmp shipboard has no crew left:
            // tmp falls off: erase from real shipboard and return null
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
    private Component getRealComponent(int realCol, int realRow) {
        return componentMatrix[realCol][realRow];
    }

    /**
     * @return the shipBoardAttributes of the shipBoard.
     */
    public ShipBoardAttributes getShipBoardAttributes() {
        return shipBoardAttributes;
    }

    /**
     * Remove the passed shipboard's components from the real shipboard.
     * Uses removeComponent, which updates the real shipBoard, shipBoardAttributes, destroyComponent,
     * centerCabin coordinates automatically.
     * Used to remove parts that fell off from the real shipboard.
     *
     * @author Boti
     */
    public void eraseShipboardFromRealShipboard(ShipBoard toErase) throws NoHumanCrewLeftException {
        // find components of the shipboard to erase
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
                if (toErase.getRealComponent(realCol, realRow) != null) {
                    // remove element without checking for fracture (to avoid infinite loop)
                    // remove updates attributes
                    try {
                        this.removeComponent(getVisibleIndex(realCol), getVisibleIndex(realRow), false);
                    } catch (FracturedShipBoardException e) {
                        throw new IllegalStateException("Error: entering infinite checkFracture loop.");
                    } catch (IllegalSelectionException e) {
                        throw new IllegalArgumentException("Couldn't remove component");
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

    /**
     * @return the matrix of the components of the shipboard.
     */
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
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
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
     * @author Boti
     */
    public void removeBattery(int visibleCol, int visibleRow) throws IllegalSelectionException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Battery))
            throw new IllegalSelectionException("The selected component is not a battery");

        if (component.getBatteryPower() - 1 >= 0) {
            ((Battery) component).removeBattery();
            // update shipboard attributes
            try {
                component.accept(new SBAttributesUpdaterVisitor(this));
            } catch (NoHumanCrewLeftException e) {
                throw new IllegalStateException("Error: no human crew left after removing battery.");
            }
        } else
            throw new IllegalSelectionException("Not enough batteries at the selected component.");
    }

    /**
     * Remove a crew member from the cabin at the given coordinates, if possible.
     * Accounts for humans and aliens.
     * Updates shipBoardAttributes.
     *
     * @throws IllegalArgumentException if removal operation not possible.
     * @author Boti
     */
    public void removeCrewMember(int visibleCol, int visibleRow) throws IllegalSelectionException, NoHumanCrewLeftException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalSelectionException("The selected component is not a cabin");

        // crew >=1 on cabin
        // throw NoHumanCrewLeftException if no crew remains on shipboard after removal
        if (component.getCrewMembers() >= 1) {
            ((Cabin) component).removeInhabitant();
            // update shipboard attributes
            component.accept(new SBAttributesUpdaterVisitor(this));
        }
        // 0 crew on cabin
        else {
            throw new IllegalSelectionException("Not enough crew members at the selected component.");
        }
    }

    /**
     * Sets the crewType of the cabin at the selected coordinates, if possible.
     * Checks for alien supports.
     * Denies change to alien crew if no human crew would be left.
     * Updates shipBoardAttributes.
     *
     * @author Boti
     */
    public void setCrewType(int visibleCol, int visibleRow, CrewType crewType) throws IllegalSelectionException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Cabin))
            throw new IllegalSelectionException("The selected component is not a cabin");

        // save original data if component is a cabin
        Cabin cabin = (Cabin) component;
        int originalCrewCount = cabin.getCrewMembers();
        CrewType originalCrewType = cabin.getCrewType();

        // no crew in cabin
        if (originalCrewCount <= 0)
            throw new IllegalSelectionException("The selected cabin has no crew members.");

        // no crew would be left after changing crewType from Human
        if (cabin.getCrewType().equals(CrewType.Human) && !crewType.equals(it.polimi.ingsw.Model.Components.CrewType.Human) &&
                (shipBoardAttributes.getHumanCrewMembers() - cabin.getCrewMembers() <= 0)) {
            throw new IllegalSelectionException("Cannot change crew type from human: no humans would be left.");
        }

        // change crewtype only if needed
        if (!originalCrewType.equals(crewType)) {
            // purple alien check
            if (crewType.equals(CrewType.Purple)) {
                if (shipBoardAttributes.getPurpleAlien())
                    throw new IllegalSelectionException("Cannot change crew to alien: a purple alien is already present elsewhere.");

                if (!checkForAlienSupport(col, row, crewType))
                    throw new IllegalSelectionException("Cannot change crew to alien: no purple alien support nearby.");
            }

            // brown alien check
            else if (crewType.equals(CrewType.Brown)) {
                if (shipBoardAttributes.getBrownAlien())
                    throw new IllegalSelectionException("Cannot change crew to alien: a brown alien is already present elsewhere.");

                if (!checkForAlienSupport(col, row, crewType))
                    throw new IllegalSelectionException("Cannot change crew to alien: no brown alien support nearby.");
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
     * @author Boti
     */
    public void moveGoods(int visibleColStarter, int visibleRowStarter, int visibleColFinal, int visibleRowFinal, int[] goods)
            throws IllegalSelectionException {
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
     * Add goods to the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @author Boti
     */
    public void addGoods(int visibleCol, int visibleRow, int[] goods) throws IllegalSelectionException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Storage))
            throw new IllegalSelectionException("The selected component is not a storage");

        // negative goods - malicious intent
        if (goods[0] < 0 || goods[1] < 0 || goods[2] < 0 || goods[3] < 0)
            throw new IllegalSelectionException("Cannot add negative number of goods");

        // check red goods slots
        if (goods[0] > component.getAvailableRedSlots())
            throw new IllegalSelectionException("Not enough red goods storage available");

        // check red + normal goods slots
        int totalGoods = 0;
        for (int good : goods)
            totalGoods += good;
        if (totalGoods > (component.getAvailableRedSlots() + component.getAvailableBlueSlots()))
            throw new IllegalSelectionException("Not enough goods storage available");

        // no problems, add goods to component
        ((Storage) component).addGoods(goods);
        // update shipboard attributes
        try {
            component.accept(new SBAttributesUpdaterVisitor(this));
        } catch (NoHumanCrewLeftException e) {
            throw new IllegalStateException("Error: no human crew left after adding goods.");
        }
    }

    /**
     * Remove goods from the storage at the given coordinates, if possible.
     * Updates shipBoardAttributes.
     *
     * @author Boti
     */
    public void removeGoods(int visibleCol, int visibleRow, int[] goods) throws IllegalSelectionException {
        if (!checkCoordinatesInBounds(visibleCol, visibleRow))
            throw new IllegalSelectionException("Coordinates out of bounds.");

        int col = getRealIndex(visibleCol);
        int row = getRealIndex(visibleRow);
        Component component = componentMatrix[col][row];

        if (!(component instanceof Storage))
            throw new IllegalSelectionException("The selected component is not a storage");

        // negative goods - malicious intent
        if (goods[0] < 0 || goods[1] < 0 || goods[2] < 0 || goods[3] < 0)
            throw new IllegalSelectionException("Cannot add negative number of goods");

        // check available goods
        int[] componentGoods = ((Storage) component).getGoods();
        for (int i = 0; i < componentGoods.length; i++) {
            if (componentGoods[i] < goods[i])
                throw new IllegalSelectionException("Not enough goods storage available");
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
        for (int realCol = FIRST_REAL_COL; realCol <= LAST_REAL_COL; realCol++) {
            for (int realRow = FIRST_REAL_ROW; realRow <= LAST_REAL_ROW; realRow++) {
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

}
