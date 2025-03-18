package it.polimi.ingsw.FlightBoard;

import java.util.*;

import it.polimi.ingsw.shipboard.Player;
import it.polimi.ingsw.application.GameType;

// numberOfTiles, startingTiles selected based on

public class FlightBoard {
    // constants - game types:
    final int normalNumberOfTiles = 24;
    final int[] normalStartingTiles = {1, 2, 4, 7};
    final int testNumberOfTiles = 18;
    final int[] testStartingTiles = {1, 2, 3, 5};

    // attributes
    private final int numberOfTiles;
    private final int[] startingTiles;
    // HashMap - no order
    private Map<Player, Integer> playerTilesMap;
    // Arraylist
    private List<Player> playerOrderList;

    public FlightBoard(GameType gameType) {
        if (gameType.equals("NormalGame")) {
            this.numberOfTiles = normalNumberOfTiles;
            this.startingTiles = normalStartingTiles;
        } else {
            this.numberOfTiles = testNumberOfTiles;
            this.startingTiles = testStartingTiles;
        }
        playerTilesMap = new HashMap<>();
        playerOrderList = new ArrayList<>();
    }

    // add player and it's position to the flight board
    public void addPlayer(Player player, int tile) {
        this.playerTilesMap.putIfAbsent(player, tile);
        this.playerOrderList.add(player);
        check();
    }

    public void incrementPlayerTile(Player player, int tiles) {
        this.playerTilesMap.computeIfPresent(player, (key, value) -> value + tiles);
        check();
    }

    public int getPlayerTile(Player player) {
        return this.playerTilesMap.get(player);
    }

    // check for lapped players, then update player order
    public void check() {
        checkLappedPlayers();
        updatePlayerOrder();
    }

    // TODO
    //  updates playerOrderList based on player tiles
    // doesn't add or remove players
    public void updatePlayerOrder() {
        for (int i = 0; i < (playerOrderList.size() - 1); i++) {
            // swap elements
            if (playerTilesMap.get(playerOrderList.get(i)) < playerTilesMap.get(playerOrderList.get(i + 1))) {
                Player tmp = playerOrderList.get(i);
                playerOrderList.set(i, playerOrderList.get(i + 1));
                playerOrderList.set(i + 1, tmp);
            }
        }
    }

    // if player is lapped, remove automatically from flightBoard
    public void checkLappedPlayers() {
        for (Map.Entry<Player, Integer> current : playerTilesMap.entrySet()) {
            for (Map.Entry<Player, Integer> other : playerTilesMap.entrySet()) {
                if (!current.equals(other) &&
                        Math.abs(current.getValue() - other.getValue()) > numberOfTiles) {
                    eliminatePlayer(current.getKey());
                }
            }
        }
    }

    // eliminate player from flight board
    public void eliminatePlayer(Player player) {
        playerTilesMap.remove(player);
        playerOrderList.remove(player);
    }

}
