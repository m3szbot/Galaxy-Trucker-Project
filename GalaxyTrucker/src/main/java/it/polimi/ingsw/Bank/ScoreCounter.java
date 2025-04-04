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
    // more players can have the minimum and both receive points
    private List<Player> leastExposedLinksList;

    // constants
    private final int leastExposedLinksPoints;
    private final int lostComponentsPoints;
    private final int[] finishOrderPoints;
    private final int[] goodsPoints;

    /**
     * Constructor, calculates leastExposedLinks player(s)
     *
     * @param gameType   Game type that determines scoring
     * @param playerList List of players
     */
    public ScoreCounter(GameType gameType, List<Player> playerList) {
        // set scoring attributes based on game type
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
        // set  instance attributes
        this.leastExposedLinksPoints = leastExposedLinksPoints;
        this.lostComponentsPoints = lostComponentsPoints;
        this.finishOrderPoints = new int[finishOrderPoints.length];
        System.arraycopy(finishOrderPoints, 0, finishOrderPoints, 0, finishOrderPoints.length);
        this.goodsPoints = new int[goodsPoints.length];
        System.arraycopy(goodsPoints, 0, goodsPoints, 0, goodsPoints.length);
        // create and fill leastExposedLinksList with players
        this.leastExposedLinksList = new ArrayList<Player>();
        calculateLeastExposedLinks(playerList);
    }

    /**
     * Calculate score of a player (credits assigned by controller)
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
        // TODO
        // for(int goods : player.shipBoard.)
        return storagePoints;
    }

    /**
     * Calculate earned (negative) points given for lost components
     *
     * @param player Player to evaluate
     * @return Earned (negative) points
     */
    private int calculateLostComponentsPoints(Player player) {
        int lostPoints = 0;
        // TODO
        // lostPoints -= player.shipBoard.
        return lostPoints;
    }

    /**
     * Add players with the least exposed links to leastExposedLinks list
     *
     * @param playerList List of players
     */
    private void calculateLeastExposedLinks(List<Player> playerList) {
        // Map of exposed links of players
        Map<Player, Integer> playerExposedLinks = new Hashtable<Player, Integer>();
        // calculate exposed links and add to Map
        // TODO
        //for (Player player : playerList) {
        //playerExposedLinks.put(player, player.shipBoard.)
        //}

        // find minimum value
        int minValue = playerExposedLinks.values().stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
        // add player to leastExposedLinksList
        for (Map.Entry<Player, Integer> entry : playerExposedLinks.entrySet()) {
            if (entry.getValue() == minValue) {
                leastExposedLinksList.add(entry.getKey());
            }
        }
    }
}

