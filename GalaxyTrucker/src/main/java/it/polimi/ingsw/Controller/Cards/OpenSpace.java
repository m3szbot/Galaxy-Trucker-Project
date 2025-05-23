package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * class that represent the card openspace
 *
 * @author carlo
 */

public class OpenSpace extends Card implements Movable, EnginePowerChoice {

    public OpenSpace(CardBuilder cardBuilder) {

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
        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int enginePowerChosen;
        List<Player> players = gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowerChosen = chooseEnginePower(players.get(i), gameInformation);
            changePlayerPosition(players.get(i), enginePowerChosen, gameInformation.getFlightBoard());

            message = "Player " + players.get(i).getNickName() + " has moved " +
                    enginePowerChosen + " position forward!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }

        gameInformation.getFlightBoard().updateFlightBoard();

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }
    }
}
