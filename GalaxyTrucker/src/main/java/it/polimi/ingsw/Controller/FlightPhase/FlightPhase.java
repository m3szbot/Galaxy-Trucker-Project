package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
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
        Card card;
        setGamePhaseToAll(GamePhase.Flight);
        System.out.println("Flight phase has started");

        DataContainer dataContainer;
        FlightBoard flightBoard = gameInformation.getFlightBoard();

        // send initial flightBoard to players
        for (Player player : flightBoard.getPlayerOrderList()) {
            dataContainer = gameMessenger.getPlayerContainer(player);
            dataContainer.setFlightBoard(flightBoard);
            dataContainer.setCommand("printFlightBoard");
            gameMessenger.sendPlayerData(player);
            dataContainer.clearContainer();
        }

        // resolve cards
        while (flightBoard.getCardsNumber() > 0) {

            card = flightBoard.getNewCard();

            for (Player player : flightBoard.getPlayerOrderList()) {

                dataContainer = gameMessenger.getPlayerContainer(player);
                dataContainer.setCard(card);
                dataContainer.setCommand("printCard");
                gameMessenger.sendPlayerData(player);
                dataContainer.clearContainer();

            }
            card.resolve(gameInformation);
        }

        System.out.println("Flight phase ended");

    }

}
