package it.polimi.ingsw.FlightBoard;

import java.util.*;
import java.util.stream.Collectors;

import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Application.GameType;

/**
 * FlightBoard class used to model the flight board
 *
 * @author Boti
 */

public class FlightBoard {
    // numberOfTiles, startingTiles selected based on gameType
    private final int numberOfTiles;
    private final int[] startingTiles;
    // Goods: red yellow green blue
    private int[] goodsNumber;

    /**
     * Player lists:
     * Both need to contain the same players, must be updated together
     * Contain only players still in game
     * Eliminated/DNF players are not in the Player lists
     */

    // HashMap - unordered
    private Map<Player, Integer> playerTilesMap;
    // Arraylist
    private List<Player> playerOrderList;
    // stack of event cards
    private Stack<Card> cardsStack;

    /**
     * FlightBoard constructor
     *
     * @param gameType Game type to set tiles
     */
    public FlightBoard(GameType gameType, List<Card> cardsList) {
        this.goodsNumber = new int[]{12, 17, 13, 14};
        // normal game
        int cardCount = 12;
        int numberOfTiles = 24;
        int[] startingTiles = new int[]{1, 2, 4, 7};
        // test game
        if (gameType == GameType.TestGame) {
            cardCount = 8;
            numberOfTiles = 18;
            startingTiles = new int[]{1, 2, 3, 5};
        }
        // assign attributes
        // (final - can be assigned once)
        this.numberOfTiles = numberOfTiles;
        this.startingTiles = new int[startingTiles.length];
        System.arraycopy(startingTiles, 0, this.startingTiles, 0, startingTiles.length);

        playerTilesMap = new HashMap<>();
        playerOrderList = new ArrayList<>();
        cardsStack = new Stack<>();
        for (int i = 0; i < cardCount; i++)
            cardsStack.push(cardsList.get(i));
    }

    /**
     * Return possible starting tiles to choose from
     *
     * @return startingTiles
     */
    public int[] getStartingTiles() {
        return startingTiles;
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
            throw new NoSuchElementException("player not present");
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
            throw new NoSuchElementException("player not present");
        }
    }

    /**
     * Return playerOrderList
     *
     * @return Copy of playerOrderList (safe)
     */
    public List<Player> getPlayerOrderList() {
        return new ArrayList<>(playerOrderList);
    }

    /**
     * Get new card from the cardStack
     *
     * @return new card
     */
    public Card getNewCard() {
        return cardsStack.pop();
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
            checkFlightBoard();
        } else {
            // throw exception
            throw new IllegalArgumentException("player already present, no duplicates allowed");
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
            int nextTile = this.getPlayerTile(player) + tiles;
            // if tile to move to is occupied, jump before/behind
            // circular board (% numberOftTiles)
            List<Integer> tilesList = new ArrayList<>();
            for (Map.Entry<Player, Integer> entry : playerTilesMap.entrySet()) {
                if (!player.equals(entry.getKey())) {
                    tilesList.add(entry.getValue() % numberOfTiles);
                }
            }
            // tile is occupied
            while (tilesList.contains(nextTile % numberOfTiles)) {
                if (tiles < 0) {
                    nextTile--;
                } else {
                    nextTile++;
                }
            }
            // update player tile
            this.playerTilesMap.put(player, nextTile);
            checkFlightBoard();
        } else {
            // throw exception
            throw new NoSuchElementException("player not present");
        }
    }

    /**
     * Remove player from FlightBoard if present, and update playerOrderList
     * must update order! (public)
     *
     * @param player Player to remove
     */
    public void removePlayer(Player player) {
        if (isPresent(player)) {
            playerTilesMap.remove(player);
            playerOrderList.remove(player);
            updatePlayerOrderList();
        } else {
            // throw exception
            throw new NoSuchElementException("player not present");
        }
    }

    /**
     * Update playerOrderList and remove lapped players
     * to call after every modification to the players
     */
    private void checkFlightBoard() {
        // update playerOrderList - necessary for removal
        updatePlayerOrderList();

        // remove lapped players
        // toRemove list to avoid concurrent modification issues
        List<Player> toRemove = new ArrayList<>();
        Player low, high;
        // start from low as it gets removed
        for (int i = playerOrderList.size() - 1; i > 0; i--) {
            low = playerOrderList.get(i);
            for (int j = 0; j < i; j++) {
                high = playerOrderList.get(j);
                if (playerTilesMap.get(high) - playerTilesMap.get(low) > this.numberOfTiles) {
                    toRemove.add(low);
                    // skip to next low
                    break;
                }
            }
        }
        // remove lapped players
        // removePlayer updates playerOrderList
        for (Player player : toRemove) {
            removePlayer(player);
        }
    }

    /**
     * Update playerOrderList based on playerTilesMap positions (highest is first)
     */
    private void updatePlayerOrderList() {
        List<Map.Entry<Player, Integer>> sortedMap = new ArrayList<>(playerTilesMap.entrySet());
        sortedMap.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        playerOrderList = sortedMap.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Check if player is present on FlightBoard
     *
     * @param player Player to examine
     * @return true if player is present, false if not present
     */
    private boolean isPresent(Player player) {
        if (playerTilesMap.containsKey(player) && !playerOrderList.contains(player)) {
            throw new IllegalStateException("player is present in playerTilesMap but not in playerOrderList ");
        }
        if (!playerTilesMap.containsKey(player) && playerOrderList.contains(player)) {
            throw new IllegalStateException("player is present in playerOrderList but not in playerTilesMap");
        }
        return (playerTilesMap.containsKey(player) && playerOrderList.contains(player));
    }

    /**
     * Remove goods from inventory
     *
     * @param goods Array of the 4 good types containing the quantities to remove
     */
    public void removeGoods(int[] goods) {
        // first check for depletion
        for (int i = 0; i < 4; i++) {
            if (this.goodsNumber[i] - goods[i] < 0)
                throw new IllegalArgumentException(String.format("Not enough goods left in inventory (%d) ", i));
        }
        // then remove elements
        for (int i = 0; i < 4; i++) {
            this.goodsNumber[i] -= goods[i];
        }
    }

    /**
     * Add goods to inventory
     *
     * @param goods Array of number of goods to add to bank inventory
     */
    public void addGoods(int[] goods) {
        for (int i = 0; i < 4; i++) {
            this.goodsNumber[i] += goods[i];
        }
    }
}
