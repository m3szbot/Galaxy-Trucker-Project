package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

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

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size();
        int enginePowerChosen;
        List<Player> players = flightBoard.getPlayerOrderList();

        for (int i = 0; i < numberOfPlayers; i++) {

            enginePowerChosen = chooseEnginePower(players.get(i), flightView);
            changePlayerPosition(players.get(i), enginePowerChosen, flightBoard);

            message = "Player " + players.get(i).getNickName() + " has moved " +
                    enginePowerChosen + " position forward!";

            flightView.sendMessageToAll(message);

        }

    }
}
