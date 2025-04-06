package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that define a default method which give to the selected player
 * the passed number of credits
 *
 * @author carlo
 */

public interface CreditsGain {

    /**
     *
     * @param player indicates the to player to which you wanna give credits
     * @param creditNumber number of credits to give
     *
     * @author Carlo
     */

    default void give(Player player, int creditNumber) {

        player.getShipBoard().getShipBoardAttributes().updateCredits(creditNumber);

    }

}
