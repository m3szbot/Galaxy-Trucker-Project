package it.polimi.ingsw.FlightBoard;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Application.GameType;

/**
 * FlightBoard class used to model the flight board
 *
 * @author Boti
 */

public class FlightBoard {
    // constants - game types:
    private final int normalNumberOfTiles = 24;
    private final int[] normalStartingTiles = {1, 2, 4, 7};
    private final int testNumberOfTiles = 18;
    private final int[] testStartingTiles = {1, 2, 3, 5};

    // attributes
    // numberOfTiles, startingTiles selected based on gameType
    private final int numberOfTiles;
    private final int[] startingTiles;
    // HashMap - unordered
    private Map<Player, Integer> playerTilesMap;
    // Arraylist
    private List<Player> playerOrderList;

    /**
     * FlightBoard constructor
     *
     * @param gameType Game type to set tiles
     */
    public FlightBoard(GameType gameType) {
        if (gameType == GameType.NormalGame) {
            this.numberOfTiles = normalNumberOfTiles;
            this.startingTiles = new int[normalStartingTiles.length];
            System.arraycopy(normalStartingTiles, 0, startingTiles, 0, normalStartingTiles.length);
        } else {
            this.numberOfTiles = testNumberOfTiles;
            this.startingTiles = new int[testStartingTiles.length];
            System.arraycopy(testStartingTiles, 0, startingTiles, 0, testStartingTiles.length);
        }
        playerTilesMap = new HashMap<>();
        playerOrderList = new ArrayList<>();
    }

    /**
     * Check if player is present on FlightBoard
     *
     * @param player Player to examine
     * @return true if player is present, false if not present
     */
    private boolean isPresent(Player player) {
        return (playerTilesMap.containsKey(player) && playerOrderList.contains(player));
    }

    /**
     * Add player to flight board, if not already present
     *
     * @param player Player to add
     * @param tile   Player's starting position
     */
    // add player and it's position to the flight board
    public void addPlayer(Player player, int tile) {
        if (!isPresent(player)) {
            this.playerTilesMap.put(player, tile);
            this.playerOrderList.add(player);
            checkBoard();
        } else {
            // throw exception
        }
    }

    /**
     * Increment player tiles by given value, if player is present
     *
     * @param player Player to act on
     * @param tiles  Value of increment of tiles
     */
    public void incrementPlayerTile(Player player, int tiles) {
        if (isPresent(player)) {
            this.playerTilesMap.compute(player, (key, value) -> value + tiles);
            checkBoard();
        } else {
            // throw exception
        }
    }

    /**
     * Return player tile position, if player is present
     *
     * @param player Player to examine
     * @return player's tile on FLightBoard
     */
    public int getPlayerTile(Player player) {
        if (isPresent(player))
            return this.playerTilesMap.get(player);
        else {
            // throw exception
            return 0;
        }
    }

    /**
     * Get player order in playerOrderList (1-4), if player is present
     *
     * @param player Player to examine
     * @return player's order in playerOrder List (1-4)
     */
    public int getPlayerOrder(Player player) {
        if (isPresent(player)) {
            return (playerOrderList.indexOf(player) + 1);
        } else {
            // throw exception
        }
        return 0;
    }

    /**
     * Check FlightBoard for lapped players and update order based on tiles
     */
    public void checkBoard() {
        checkLappedPlayers();
        updatePlayerOrderList();
    }

    /**
     * Update playerOrderList based on playerTilesMap
     */
    public void updatePlayerOrderList() {
        List<Map.Entry<Player, Integer>> sortedMap = new ArrayList<>(playerTilesMap.entrySet());
        sortedMap.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        playerOrderList = sortedMap.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Check for lapped players and remove them from the FlightBoard
     * does not update order (part of checkBoard)
     */
    public void checkLappedPlayers() {
        for (Map.Entry<Player, Integer> current : playerTilesMap.entrySet()) {
            for (Map.Entry<Player, Integer> other : playerTilesMap.entrySet()) {
                if (!current.equals(other) &&
                        Math.abs(current.getValue() - other.getValue()) > numberOfTiles)
                    removePlayer(current.getKey());
            }
        }
    }

    /**
     * Remove player from FlightBoard, if present
     * does not update order (part of checkLappedPlayers)
     *
     * @param player Player to remove
     */
    public void removePlayer(Player player) {
        if (isPresent(player)) {
            playerTilesMap.remove(player);
            playerOrderList.remove(player);
        } else {
            // throw exception
        }
    }
}
