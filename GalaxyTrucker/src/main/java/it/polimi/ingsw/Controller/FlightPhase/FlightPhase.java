package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
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
        PlayerMessenger playerMessenger;

        setGamePhaseToClientServer(GamePhase.Flight);
        System.out.println("Flight phase has started");

        FlightBoard flightBoard = gameInformation.getFlightBoard();

        // send initial flightBoard to players
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printFlightBoard(flightBoard);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

        }

        // resolve cards
        while (flightBoard.getCardsNumber() > 0) {

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                System.out.println("Error while sleeping");
            }

            card = flightBoard.getNewCard();

            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);
                playerMessenger.printCard(card);

            }

            card.resolve(gameInformation);

            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);

                playerMessenger.printMessage("Your shipboard:\n");

                playerMessenger.printShipboard(player.getShipBoard());

            }
        }

        gameMessenger.sendMessageToAll("Flight phase has ended.\n");

    }

}
