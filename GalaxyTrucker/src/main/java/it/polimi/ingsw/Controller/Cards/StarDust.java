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

        PlayerMessenger playerMessenger;

        List<Player> players = gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = gameInformation.getFlightBoard().getPlayerOrderList().size() - 1; i >= 0; i--) {

            changePlayerPosition(players.get(i), -players.get(i).getShipBoard().countExternalJunctions(), gameInformation.getFlightBoard());

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }

}
