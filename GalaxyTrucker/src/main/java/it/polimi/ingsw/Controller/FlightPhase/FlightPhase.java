package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class FlightPhase extends Phase {

    public FlightPhase(GameInformation gameInformation) {
        super(gameInformation);
    }

    public void start() {
        setGamePhaseToAll(GamePhase.Flight);

        DataContainer dataContainer;
        FlightBoard flightBoard = gameInformation.getFlightBoard();

        // send flightBoard to players
        for (Player player : flightBoard.getPlayerOrderList()) {
            dataContainer = gameMessenger.getPlayerContainer(player);
            dataContainer.setFlightBoard(flightBoard);
            dataContainer.setCommand("printFlightBoard");
            gameMessenger.sendPlayerData(player);
        }

        // resolve cards
        while (flightBoard.getCardsNumber() > 0) {
            flightBoard.getNewCard().resolve(gameInformation);
        }

        System.out.println("Flight phase ended");
    }

}
