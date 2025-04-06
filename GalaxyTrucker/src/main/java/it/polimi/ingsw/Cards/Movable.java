package it.polimi.ingsw.Cards;

import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that defines a default method which move the player
 * position on the flight board
 *
 * @author carlo
 */

public interface Movable {

    /**
     *
     * @param player target player
     * @param numberOfDays number of tiles the player will be moved forward
     * @param flightBoard flightboard of the current game
     *
     * @author Carlo
     */

    default void changePlayerPosition(Player player, int numberOfDays, FlightBoard flightBoard) {

        flightBoard.incrementPlayerTile(player, numberOfDays);

    }

}
