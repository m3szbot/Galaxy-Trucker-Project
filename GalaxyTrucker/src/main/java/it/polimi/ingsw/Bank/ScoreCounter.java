package it.polimi.ingsw.Bank;

import java.util.*;

import it.polimi.ingsw.shipboard.Player;

public class ScoreCounter {
    // use ArrayList?
    // more players can have the minimum and both receive points
    private List<Player> leastExposedLinksList;

    // constructor updates leastExposedLinks with the adequate player(s)
    // so that it can be checked when calculating individual player scores
    public ScoreCounter(Player[] playerList) {
        this.leastExposedLinksList = new ArrayList<Player>();
        calculateLeastExposedLinks(playerList);
    }

    // calculate score of individual player
    public int calculatePlayerScore(Player player) {
        int playerPoints = 0;
        playerPoints += calculateStoragePoints(player);
        playerPoints += calculateLostComponentsPoints(player);
        // equals or == ?
        if (leastExposedLinksList.contains(player)) {
            playerPoints += 0;
        }
        return playerPoints;
    }

    private int calculateStoragePoints(Player player) {
        int storagePoints = 0;
        return storagePoints;
    }

    // return negative or 0
    private int calculateLostComponentsPoints(Player player) {
        // lostPoints <= 0
        int lostPoints = 0;
        return lostPoints;
    }

    // add players with leastExposedLinks to leastLinksExposedList
    private void calculateLeastExposedLinks(Player[] playerList) {
        // Map of exposed links of players
        Map<Player, Integer> playerExposedLinks = new Hashtable<Player, Integer>();
        // calculate exposed links and add to Map

        // find minimum value
        int minValue = playerExposedLinks.values().stream().min(Integer::compare).orElse(Integer.MAX_VALUE);
        // add player to leastExposedLinksList

    }
}

