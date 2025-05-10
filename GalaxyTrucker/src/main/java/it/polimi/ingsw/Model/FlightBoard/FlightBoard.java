package it.polimi.ingsw.Model.FlightBoard;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.*;
import java.util.stream.Collectors;

/**
 * FlightBoard class used to model the flight board
 *
 * @author Boti
 */

public class FlightBoard {
    // number of tiles on the flightBoard
    private final int numberOfTiles;
    // free starting tiles
    private List<Integer> startingTiles;
    // Goods: red yellow green blue
    private int[] goodsNumber;

    // playerTilesMap and playerOrderList are interlinked, contain the same players
    // position of players on the flightBoard
    private Map<Player, Integer> playerTilesMap;
    // order of players still in game
    private List<Player> playerOrderList;
    // players forcibly eliminated
    private List<Player> eliminatedList;
    // players who voluntarily gave up
    private List<Player> gaveUpList;
    // stack of adventure cards
    private Stack<Card> cardsStack;

    /**
     * FlightBoard constructor
     *
     * @param gameType Game type to set tiles
     */
    public FlightBoard(GameType gameType, List<Card> cardsList) {
        this.goodsNumber = new int[]{12, 17, 13, 14};
        // game constants
        // normal game
        int cardCount = 12;
        int numberOfTiles = 24;
        Integer[] startingTiles = new Integer[]{1, 2, 4, 7};
        // test game
        if (gameType == GameType.TestGame) {
            cardCount = 8;
            numberOfTiles = 18;
            startingTiles = new Integer[]{1, 2, 3, 5};
        }
        // assign flightBoard attributes
        this.numberOfTiles = numberOfTiles;
        this.startingTiles = new ArrayList<>(Arrays.asList(startingTiles));

        playerTilesMap = new HashMap<>();
        playerOrderList = new ArrayList<>();
        eliminatedList = new ArrayList<>();
        gaveUpList = new ArrayList<>();
        cardsStack = new Stack<>();
        for (int i = 0; i < cardCount; i++)
            cardsStack.push(cardsList.get(i));
    }

    /**
     * @return free starting tiles to choose from
     */
    public List<Integer> getStartingTiles() {
        return startingTiles;
    }

    public List<Player> getEliminatedList() {
        return eliminatedList;
    }

    public List<Player> getGaveUpList() {
        return gaveUpList;
    }

    /**
     * @return number of unresolved cards
     */
    public int getCardsNumber() {
        return this.cardsStack.size();
    }


    /**
     * Get player order in playerOrderList (1-4), if player is in game
     *
     * @param player Player to examine
     * @return player's order in playerOrder List (1-4)
     */
    public int getPlayerOrder(Player player) {
        if (isInGame(player)) {
            return (playerOrderList.indexOf(player) + 1);
        } else {
            // throw exception
            throw new NoSuchElementException("Player not in game");
        }
    }

    /**
     * Check if player is still in game or throws exception for player not present/eliminated/given up.
     * Checks synchronization between playerTilesMap and playerOrderList.
     *
     * @param player Player to examine
     * @return true if player is in game, false if not in game
     */
    public boolean isInGame(Player player) {
        if (playerTilesMap.containsKey(player) && !playerOrderList.contains(player)) {
            throw new IllegalStateException("Player is present in playerTilesMap but not in playerOrderList ");
        }
        if (!playerTilesMap.containsKey(player) && playerOrderList.contains(player)) {
            throw new IllegalStateException("player is present in playerOrderList but not in playerTilesMap");
        }
        return (playerTilesMap.containsKey(player) && playerOrderList.contains(player));
    }

    /**
     * @return true if player is eliminated, false otherwise
     */
    public boolean isEliminated(Player player) {
        return eliminatedList.contains(player);
    }

    /**
     * @return true if player voluntarily gave up, false otherwise
     */
    public boolean isGaveUp(Player player) {
        return gaveUpList.contains(player);
    }

    /**
     * @return playerOrderList
     */
    public List<Player> getPlayerOrderList() {
        return playerOrderList;
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
     * Add player to flight board to the selected tile,
     * if player not already in game and selected tile is free and valid.
     * Synchronized, only 1 player can be added at a time (because of starting tiles).
     *
     * @param player Player to add
     * @param tile   Player's starting tile
     */
    public synchronized void addPlayer(Player player, int tile) {
        if (!isInGame(player)) {
            if (startingTiles.contains(tile)) {
                // add player
                this.playerTilesMap.put(player, tile);
                this.playerOrderList.add(player);
                checkFlightBoard();
                // remove starting tile
                startingTiles.remove((Integer) tile);
            } else {
                throw new IndexOutOfBoundsException("The selected starting tile is not valid");
            }
        } else {
            throw new IllegalArgumentException("Player already in game");
        }
    }

    /**
     * Update playerOrderList and remove lapped players.
     * To call after every modification to the players.
     */
    private void checkFlightBoard() {
        // update playerOrderList - necessary for removal
        updatePlayerOrderList();

        // remove lapped players
        // toRemove list to avoid concurrent modification issues
        List<Player> toRemove = new ArrayList<>();
        Player low, high;
        // start from lowest as it gets removed first
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
        // eliminatePlayer updates playerOrderList
        for (Player player : toRemove) {
            eliminatePlayer(player);
        }
    }

    /**
     * Eliminate player from game (if in game), put him into eliminatedList,
     * and update playerOrderList (necessary).
     *
     * @param player Player to remove
     */
    public void eliminatePlayer(Player player) {
        if (isInGame(player)) {
            playerTilesMap.remove(player);
            eliminatedList.add(player);
            playerOrderList.remove(player);
            updatePlayerOrderList();
        } else {
            throw new NoSuchElementException("Player not in game");
        }
    }

    /**
     * Update playerOrderList based on playerTilesMap positions (highest is first).
     */
    private void updatePlayerOrderList() {
        List<Map.Entry<Player, Integer>> sortedMap = new ArrayList<>(playerTilesMap.entrySet());
        sortedMap.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
        playerOrderList = sortedMap.stream().map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * Increment player tiles by given value, if player is present
     *
     * @param player Player to act on
     * @param tiles  Value of increment of tiles
     */
    public void incrementPlayerTile(Player player, int tiles) {
        if (isInGame(player)) {
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
     * Return player tile position, if player is in game
     *
     * @param player Player to examine
     * @return player's tile on FLightBoard
     */
    public int getPlayerTile(Player player) {
        if (isInGame(player))
            return this.playerTilesMap.get(player);
        else {
            // throw exception
            throw new NoSuchElementException("Player not in game");
        }
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
