package it.polimi.ingsw.Bank;

import java.util.*;

import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Shipboard.Player;

/**
 * ScoreCounter class to evaluate player score at end of flight
 *
 * @author Boti
 */

public class ScoreCounter {
    // map of player scores
    private Map<Player, Integer> playerScoresMap;

    // more players can have the minimum and both receive points
    private List<Player> leastExposedLinksList;
    // constants
    private final int leastExposedLinksPoints;
    private final int lostComponentsPoints;
    private final int[] finishOrderPoints;
    private final int[] goodsPoints;

    /**
     * Constructor, populates playerScoresMap
     *
     * @param gameType        Game type that determines scoring
     * @param playerOrderList List of player orders from FlightBoard!
     */
    public ScoreCounter(GameType gameType, List<Player> playerOrderList) {
        // set scoring based on game type
        // normal game values
        int leastExposedLinksPoints = 4;
        int lostComponentsPoints = -1;
        int[] finishOrderPoints = {8, 6, 4, 2};
        int[] goodsPoints = {8, 6, 4, 2};
        // test game values
        if (gameType == GameType.TestGame) {
            leastExposedLinksPoints = 2;
            finishOrderPoints = new int[]{4, 3, 2, 1};
            goodsPoints = new int[]{4, 3, 2, 1};
        }
        // set scoring attributes
        this.leastExposedLinksPoints = leastExposedLinksPoints;
        this.lostComponentsPoints = lostComponentsPoints;
        this.finishOrderPoints = new int[finishOrderPoints.length];
        System.arraycopy(finishOrderPoints, 0, this.finishOrderPoints, 0, finishOrderPoints.length);
        this.goodsPoints = new int[goodsPoints.length];
        System.arraycopy(goodsPoints, 0, this.goodsPoints, 0, goodsPoints.length);
        // create and fill leastExposedLinksList with players
        this.leastExposedLinksList = new ArrayList<>();
        calculateLeastExposedLinks(playerOrderList);
        // populate playerScoresMap
        this.playerScoresMap = new HashMap<>();
        for (Player player : playerOrderList) {
            playerScoresMap.put(player, calculatePlayerScore(player, playerOrderList));
        }
    }

    /**
     * Return score of given player
     *
     * @param player Player to examine
     * @return Score of given player
     */
    public int getPlayerScore(Player player) {
        return playerScoresMap.get(player);
    }

    /**
     * Populate leastExposedLinksList with players
     *
     * @param playerOrderList List of players
     */
    private void calculateLeastExposedLinks(List<Player> playerOrderList) {
        // Map: player - player exposed links
        Map<Player, Integer> playerExposedLinks = new Hashtable<>();
        // calculate exposed links of player and add to temporary Map
        for (Player player : playerOrderList) {
            playerExposedLinks.put(player, player.getShipBoard().countExternalJunctions());
        }
        // find minimum value
        int minValue = playerExposedLinks.values().stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
        // add minimum player to leastExposedLinksList
        for (Map.Entry<Player, Integer> entry : playerExposedLinks.entrySet()) {
            if (entry.getValue() == minValue) {
                leastExposedLinksList.add(entry.getKey());
            }
        }
    }

    /**
     * Calculate score of a player (credits assigned by controller)
     *
     * @param player Player whose score is to be calculated
     * @return Score of the given player
     */
    public int calculatePlayerScore(Player player, List<Player> playerOrderList) {
        int playerPoints = 0;
        playerPoints += calculateFinishOrderPoints(player, playerOrderList);
        playerPoints += calculateLeastExposedLinksPoints(player);
        playerPoints += calculateGoodsPoints(player);
        playerPoints += calculateLostComponentsPoints(player);
        return playerPoints;
    }

    /**
     * Calculate player score based on finish position
     * no points if DNF
     *
     * @param player          Player to examine
     * @param playerOrderList Finish order of players
     * @return Score of player given for finish position
     */
    private int calculateFinishOrderPoints(Player player, List<Player> playerOrderList) {
        int finishPoints = 0;
        finishPoints += finishOrderPoints[playerOrderList.indexOf(player)];
        return finishPoints;
    }

    /**
     * Calculates points given for least exposed components ship
     *
     * @param player Player to evaluate
     * @return Earned points
     */
    private int calculateLeastExposedLinksPoints(Player player) {
        if (leastExposedLinksList.contains(player))
            return this.leastExposedLinksPoints;
        else
            return 0;
    }

    /**
     * Calculate points given for selling goods
     *
     * @param player Player to evaluate
     * @return Earned points
     */
    private int calculateGoodsPoints(Player player) {
        int goodsPoints = 0;
        for (int i = 0; i < this.goodsPoints.length; i++) {
            goodsPoints += this.goodsPoints[i] * player.getShipBoard().getShipBoardAttributes().getGoods()[i];
        }
        return goodsPoints;
    }

    /**
     * Calculate (negative) points given for lost components
     *
     * @param player Player to evaluate
     * @return Earned (negative) points
     */
    private int calculateLostComponentsPoints(Player player) {
        int lostPoints = 0;
        lostPoints += lostComponentsPoints * player.getShipBoard().getShipBoardAttributes().getDestroyedComponents();
        return lostPoints;
    }
}


