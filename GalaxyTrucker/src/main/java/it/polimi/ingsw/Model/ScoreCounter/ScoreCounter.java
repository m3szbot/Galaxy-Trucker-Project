package it.polimi.ingsw.Model.ScoreCounter;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.*;

/**
 * ScoreCounter class to evaluate player score at end of flight.
 * <p>
 * Finishing players: normal pointing
 * Did not finish (gave up/eliminated): special pointing:
 * No finishOrder points, no leastExposedLinks points
 * 1/2 goods points
 * Normal lostComponents points
 *
 * @author Boti
 */

public class ScoreCounter {
    // scoring constants
    private final int[] finishOrderPoints;
    private final int[] goodsPoints;
    private final int leastExposedLinksPoints;
    private final int lostComponentsPoints;
    // map of player scores
    private Map<Player, Integer> playerScoresMap;
    // more players can have the minimum and both receive points
    private List<Player> leastExposedLinksList;

    /**
     * Constructor, calculates player scores and puts them in playerScoresMap.
     * Use  getPlayerScore to get calculated scores.
     *
     * @param gameType    Game type that determines scoring
     * @param flightBoard contains playerOrderList (players who finished), eliminatedList, gaveUpList
     */
    public ScoreCounter(FlightBoard flightBoard, GameType gameType) {
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
        this.finishOrderPoints = finishOrderPoints.clone();
        this.goodsPoints = goodsPoints.clone();
        // create and fill leastExposedLinksList
        this.leastExposedLinksList = new ArrayList<>();
        // only finishing players count for leastExposedLinks points
        calculateLeastExposedLinks(flightBoard.getPlayerOrderList());
        // populate playerScoresMap
        this.playerScoresMap = new HashMap<>();
        // finishing players
        for (Player player : flightBoard.getPlayerOrderList()) {
            playerScoresMap.put(player, calculateFinishingPlayerScore(player, flightBoard.getPlayerOrderList()));
        }
        // eliminated players
        for (Player player : flightBoard.getEliminatedList()) {
            playerScoresMap.put(player, calculateDNFPlayerScore(player));
        }
        // gave up players
        for (Player player : flightBoard.getGaveUpList()) {
            playerScoresMap.put(player, calculateDNFPlayerScore(player));
        }

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
     * Calculate score of a finishing player.
     * Credits assigned by controller.
     *
     * @return Score of the given player.
     */
    private int calculateFinishingPlayerScore(Player player, List<Player> playerOrderList) {
        int playerPoints = 0;
        playerPoints += calculateFinishOrderPoints(player, playerOrderList);
        playerPoints += calculateLeastExposedLinksPoints(player);
        playerPoints += calculateGoodsPoints(player);
        playerPoints += calculateLostComponentsPoints(player);
        return playerPoints;
    }

    /**
     * Calculate score of a not finishing/DNF player.
     * Credits assigned by controller.
     *
     * @return Score of the given player.
     */
    private int calculateDNFPlayerScore(Player player) {
        int playerPoints = 0;
        playerPoints += calculateGoodsPoints(player) / 2;
        playerPoints += calculateLostComponentsPoints(player);
        return playerPoints;
    }

    /**
     * Calculate player score based on finish position. Called only for finishing players.
     *
     * @param player          Player to examine
     * @param playerOrderList Finish order of players
     * @return Score of player given for finish position
     */
    private int calculateFinishOrderPoints(Player player, List<Player> playerOrderList) {
        return finishOrderPoints[playerOrderList.indexOf(player)];
    }

    /**
     * Calculates points given for least exposed components ship for player.
     *
     * @param player Player to evaluate
     * @return Earned points
     */
    private int calculateLeastExposedLinksPoints(Player player) {
        if (leastExposedLinksList.contains(player)) {
            return this.leastExposedLinksPoints;
        } else {
            return 0;
        }
    }

    /**
     * Calculate points given for selling goods of the player.
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
     * Calculate (negative) points given for lost components of player.
     *
     * @param player Player to evaluate
     * @return Earned (negative) points
     */
    private int calculateLostComponentsPoints(Player player) {
        int lostPoints = 0;
        lostPoints += lostComponentsPoints * player.getShipBoard().getShipBoardAttributes().getDestroyedComponents();
        return lostPoints;
    }

    /**
     * Return score of given player.
     *
     * @param player Player to examine
     * @return Score of given player
     */
    public int getPlayerScore(Player player) {
        if (playerScoresMap.containsKey(player)) {
            return playerScoresMap.get(player);
        } else
            throw new IllegalArgumentException("Player not present");
    }
}


