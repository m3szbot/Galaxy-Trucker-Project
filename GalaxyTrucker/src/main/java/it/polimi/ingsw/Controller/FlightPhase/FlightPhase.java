package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

import java.util.HashMap;
import java.util.Map;

public class FlightPhase implements Startable {
    Map<Player, FlightView> playerViewMap;

    public FlightPhase(GameInformation gameInformation) {
        playerViewMap = new HashMap<>();
        // create player-specific flight views
        for (Player player : gameInformation.getPlayerList()) {
            playerViewMap.put(player, new FlightView(gameInformation));
        }
    }

    public void start(GameInformation gameInformation) {
        // TODO use playerViewMap
        FlightView flightView = new FlightView(gameInformation);
        FlightBoard flightBoard = gameInformation.getFlightBoard();

        while (flightBoard.getCardsNumber() > 0) {
            // TODO pass playerViewMap so each user sees his own speicific view
            flightBoard.getNewCard().resolve(flightBoard, flightView);
        }

    }

}
