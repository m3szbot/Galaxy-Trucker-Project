package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * Util which check for the for loop index at the beginning of
 * it every time. Realign the index based on the disconnected players.
 *
 * @author carlo
 *
 */

public class IndexChecker {

    /**
     *
     * @param gameInformation
     * @param currentIndex
     * @return the index corrected
     */

    public static int checkIndex(GameInformation gameInformation, int currentIndex){

        List<Player> disconnectedPlayer = gameInformation.getDisconnectedPlayerList();
        List<Player> flightBoardPlayersList = gameInformation.getFlightBoard().getPlayerOrderList();
        int updatedIndex = currentIndex;

        for(int i = 0; i <= currentIndex; i++){

            if(disconnectedPlayer.contains(flightBoardPlayersList.get(i))){
               updatedIndex--;
            }

        }

        return updatedIndex;


    }


}
