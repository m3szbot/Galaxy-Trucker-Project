package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.ShipBoard.Player;

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

    //note that the method doesn't have to be tested as it calls another method.
    //updateCredits must be tested.

    default void giveCredits(Player player, int creditNumber) {

        player.getShipBoard().getShipBoardAttributes().updateCredits(creditNumber);

    }

}
