package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;
import it.polimi.ingsw.View.FlightView.FlightViewTUI;

import java.util.HashMap;
import java.util.Map;

public class FlightPhase implements Startable {
    Map<Player, FlightView> playerViewMap;

    public FlightPhase(GameInformation gameInformation) {
        playerViewMap = new HashMap<>();
        // create player-specific flight views
        for (Player player : gameInformation.getPlayerList()) {
            playerViewMap.put(player, new FlightViewTUI());
        }
    }

    public void start(GameInformation gameInformation) {
        System.out.println("Flight phase started");
        DataContainer dataContainer;
        // TODO use playerViewMap
        FlightView flightView = new FlightViewTUI();
        FlightBoard flightBoard = gameInformation.getFlightBoard();
        int gameCode = gameInformation.getGameCode();

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage("FlightPhase is starting ...\n");
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
        }

        for (Player player : flightBoard.getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setFlightBoard(flightBoard);
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
        }

        while (flightBoard.getCardsNumber() > 0) {
            // TODO pass playerViewMap so each user sees his own speicific view
            flightBoard.getNewCard().resolve(gameInformation);
        }
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setCommand("advancePhase");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
        }

        System.out.println("Flight phase ended");
    }

}
