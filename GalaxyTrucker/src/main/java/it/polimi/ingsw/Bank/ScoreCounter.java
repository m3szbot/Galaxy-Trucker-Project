package it.polimi.ingsw.Bank;

import java.util.*;

import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.shipboard.Player;

/**
 * ScoreCounter class to evaluate player score at end of flight
 *
 * @author Boti
 */

public class ScoreCounter {
    // constants for scores depending on game type
    // normal game
    private final int normalLeastExposedLinksPoints = 4;
    private final int[] normalFinishOrderPoints = {8, 6, 4, 2};
    private final int[] normalGoodsPoints = {8, 6, 4, 2};
    // test game
    private final int testLeastExposedLinksPoints = 2;
    private final int[] testFinishOrderPoints = {4, 3, 2, 1};
    private final int[] testGoodsPoints = {4, 3, 2, 1};

    // attributes
    private final int leastExposedLinksPoints;
    private final int[] finishOrderPoints;
    private final int[] goodsPoints;
    private final int lostComponentsPoints = -1;

    // more players can have the minimum and both receive points
    private List<Player> leastExposedLinksList;

    /**
     * Constructor, calculates leastExposedLinks player(s)
     *
     * @param gameType   Game type that determines scoring
     * @param playerList List of players
     */
    public ScoreCounter(GameType gameType, Player[] playerList) {
        // set score point attributes based on game type
        if (gameType == GameType.NormalGame) {
            this.leastExposedLinksPoints = normalLeastExposedLinksPoints;
            this.finishOrderPoints = new int[normalFinishOrderPoints.length];
            System.arraycopy(normalFinishOrderPoints, 0, finishOrderPoints, 0, normalFinishOrderPoints.length);
            this.goodsPoints = new int[normalGoodsPoints.length];
            System.arraycopy(normalGoodsPoints, 0, goodsPoints, 0, normalGoodsPoints.length);
        } else {
            this.leastExposedLinksPoints = testLeastExposedLinksPoints;
            this.finishOrderPoints = new int[testFinishOrderPoints.length];
            System.arraycopy(testFinishOrderPoints, 0, finishOrderPoints, 0, testFinishOrderPoints.length);
            this.goodsPoints = new int[testGoodsPoints.length];
            System.arraycopy(testGoodsPoints, 0, goodsPoints, 0, testGoodsPoints.length);
        }
        this.leastExposedLinksList = new ArrayList<Player>();
        calculateLeastExposedLinks(playerList);
    }

    // TODO assign player credits based on scores

    /**
     * Calculate score of a player
     *
     * @param player Player whose score is to be calculated
     * @return Score of the given player
     */
    public int calculatePlayerScore(Player player) {
        int playerPoints = 0;
        playerPoints += calculateLeastExposedLinksPoints(player);
        playerPoints += calculateGoodsPoints(player);
        playerPoints += calculateLostComponentsPoints(player);
        return playerPoints;
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
        int storagePoints = 0;
        // TODO to finish
        //for()
        return storagePoints;
    }

    /**
     * Calculate earned points given for lost components
     *
     * @param player Player to evaluate
     * @return Earned (negative) points
     */
    private int calculateLostComponentsPoints(Player player) {
        int lostPoints = 0;
        // TODO to finish
        return lostPoints;
    }

    /**
     * Add players with the least exposed links to leastExposedLinks list
     *
     * @param playerList List of players
     */
    private void calculateLeastExposedLinks(Player[] playerList) {
        // Map of exposed links of players
        Map<Player, Integer> playerExposedLinks = new Hashtable<Player, Integer>();
        // calculate exposed links and add to Map

        // find minimum value
        int minValue = playerExposedLinks.values().stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
        // add player to leastExposedLinksList

    }
}

