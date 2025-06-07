package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

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

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int externalJunctions;
        Player player;

        gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = gameInformation.getFlightBoard().getPlayerOrderList().size() - 1; i >= 0; i--) {

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            externalJunctions = player.getShipBoard().countExternalJunctions();

            changePlayerPosition(player, -externalJunctions, gameInformation.getFlightBoard());

            if (externalJunctions > 0) {
                message = "Player " + player.getNickName() + " has receded of " + externalJunctions + " positions!\n";
                playerMessenger.printMessage(message);

            } else {
                message = "Player " + player.getNickName() + " has not receded!\n";
                playerMessenger.printMessage(message);

            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error while sleeping");
            }

        }

        gameInformation.getFlightBoard().updateFlightBoard();

        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player1);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }

}
