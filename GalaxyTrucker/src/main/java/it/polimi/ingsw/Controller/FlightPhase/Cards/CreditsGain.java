package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that defines a default method which gives the
 * given number of credits to the selected player.
 *
 * @author carlo
 */

public interface CreditsGain {

    /**
     * @param player       indicates the player to which you want give credits
     * @param creditNumber number of credits to give
     * @author Carlo
     */


    default void giveCredits(Player player, int creditNumber) {

        player.getShipBoard().getShipBoardAttributes().addCredits(creditNumber);

    }

}
