package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that define a default method which give to the selected player
 * the passed number of credits
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
