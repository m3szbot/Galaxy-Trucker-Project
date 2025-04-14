package it.polimi.ingsw.ScoreCounter;

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
    private final int[] finishOrderPoints;
    private final int leastExposedLinksPoints;
    private final int[] goodsPoints;
    private final int lostComponentsPoints;

    /**
     * Constructor, populates playerScoresMap
     * playerList needed to calculate score for all players
     * playerOrderList needed to calculate finishOrderPoints
     *
     * @param gameType        Game type that determines scoring
     * @param playerList      List of all players
     * @param playerOrderList Finish order of players (only players still in game)
     */
    public ScoreCounter(GameType gameType, List<Player> playerList, List<Player> playerOrderList) {
        // set scoring based on game type
        // normal game values
        int[] finishOrderPoints = {8, 6, 4, 2};
        int leastExposedLinksPoints = 4;
        int[] goodsPoints = {4, 3, 2, 1};
        int lostComponentsPoints = -1;
        // test game values
        if (gameType == GameType.TestGame) {
            finishOrderPoints = new int[]{4, 3, 2, 1};
            leastExposedLinksPoints = 2;
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
        calculateLeastExposedLinks(playerList);
        // populate playerScoresMap
        this.playerScoresMap = new HashMap<>();
        for (Player player : playerList) {
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
     * @param playerList List of all players
     */
    private void calculateLeastExposedLinks(List<Player> playerList) {
        // Map: player - player exposed links
        Map<Player, Integer> playerExposedLinks = new Hashtable<>();
        // calculate exposed links of player and add to temporary Map
        for (Player player : playerList) {
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
     * @param player          Player whose score is to be calculated
     * @param playerOrderList Finish order of players (only players still in game)
     * @return Score of the given player
     */
    private int calculatePlayerScore(Player player, List<Player> playerOrderList) {
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


