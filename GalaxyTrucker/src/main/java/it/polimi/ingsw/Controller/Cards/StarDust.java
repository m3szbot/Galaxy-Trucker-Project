package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

//check that the count external junctions method in shipBoard does what it is supposed to do

/**
 * class that represent the card StarDust.
 *
 * @author carlo
 */

public class StarDust extends Card implements Movable {


    public StarDust(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int externalJunctions;
        Player player;

        gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = gameInformation.getFlightBoard().getPlayerOrderList().size() - 1; i >= 0; i--) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            externalJunctions = player.getShipBoard().countExternalJunctions();

            if (externalJunctions > 0) {
                changePlayerPosition(player, -externalJunctions, gameInformation.getFlightBoard());
            }

            if (playerMessenger != null) {

                if (externalJunctions > 0) {

                    message = "Player " + player.getNickName() + " has receded of " + externalJunctions + " positions!\n";
                    playerMessenger.printMessage(message);

                } else {

                    message = "Player " + player.getNickName() + " has not receded!\n";
                    playerMessenger.printMessage(message);

                }
            }


            Sleeper.sleepXSeconds(3);

            if (player != null) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        try {
            gameInformation.getFlightBoard().updateFlightBoard();

        } catch (LappedPlayersException e) {
            ExceptionsHandler.handleLappedPlayersException(ClientMessenger.getGameMessenger(gameInformation.getGameCode()), e);
        }

    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println();

    }

}
