package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * class that represent the card openspace
 *
 * @author carlo
 */

public class OpenSpace extends Card implements Movable, EnginePowerChoice {

    public OpenSpace(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size();
        int enginePowerChosen;
        List<Player> players = flightBoard.getPlayerOrderList();

        for(int i = 0; i < numberOfPlayers; i++){

            enginePowerChosen = chooseEnginePower(players.get(i), flightView);
            changePlayerPosition(players.get(i), enginePowerChosen, flightBoard);

            message = "Player " + players.get(i).getNickName() + " has moved " +
                    enginePowerChosen + " position forward!";

            flightView.sendMessageToAll(message);

        }

    }
}
