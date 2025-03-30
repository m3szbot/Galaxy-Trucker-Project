package it.polimi.ingsw.Cards;

import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that defines a method which move the player
 * position on the flight board
 *
 * @author carlo
 */

public interface Movable {

    default void changePlayerPosition(Player player, int numberOfDays, FlightBoard flightBoard) {

        flightBoard.incrementPlayerTile(player, numberOfDays);

    }

}
