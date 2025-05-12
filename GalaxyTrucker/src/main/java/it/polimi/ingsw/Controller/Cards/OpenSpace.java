package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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

    public void resolve(FlightBoard flightBoard, int gameCode) {

        DataContainer dataContainer;
        int numberOfPlayers = flightBoard.getPlayerOrderList().size();
        int enginePowerChosen;
        List<Player> players = flightBoard.getPlayerOrderList();

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowerChosen = chooseEnginePower(players.get(i), gameCode);
            changePlayerPosition(players.get(i), enginePowerChosen, flightBoard);

            message = "Player " + players.get(i).getNickName() + " has moved " +
                    enginePowerChosen + " position forward!";
            for (Player player : flightBoard.getPlayerOrderList()) {
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
            }

        }

    }
}
