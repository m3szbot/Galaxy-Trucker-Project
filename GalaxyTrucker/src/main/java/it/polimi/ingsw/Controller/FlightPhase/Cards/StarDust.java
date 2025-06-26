package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

//check that the count external junctions method in shipBoard does what it is supposed to do

/**
 * Class that represents the card StarDust.
 *
 * @author carlo
 * @author Ludo
 */

public class StarDust extends Card implements Movable {


    public StarDust(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.imagePath = cardBuilder.getImagePath();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        int externalJunctions;
        Player player;

        gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = gameInformation.getFlightBoard().getPlayerOrderList().size() - 1; i >= 0; i--) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            externalJunctions = player.getShipBoard().countExternalJunctions();

            if (externalJunctions > 0) {
                changePlayerPosition(player, -externalJunctions, gameInformation.getFlightBoard());
            }

            if (externalJunctions > 0) {

                message = "Player " + player.getColouredNickName() + " has receded of " + externalJunctions + " positions!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                message = "Player " + player.getColouredNickName() + " has not receded!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            }


            Sleeper.sleepXSeconds(3);

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
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
