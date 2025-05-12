package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

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

    @Override

    public void resolve(GameInformation gameInformation) {

        DataContainer dataContainer;
        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size();
        int enginePowerChosen;
        List<Player> players = gameInformation.getFlightBoard().getPlayerOrderList();

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowerChosen = chooseEnginePower(players.get(i), gameInformation.getGameCode());
            changePlayerPosition(players.get(i), enginePowerChosen, gameInformation.getFlightBoard());

            message = "Player " + players.get(i).getNickName() + " has moved " +
                    enginePowerChosen + " position forward!";
            for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
                dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
            }

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }
    }
}
