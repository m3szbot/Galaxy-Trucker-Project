package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Controller.Sleeper;
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

        // send initial flightBoard to players and starting the threads
        for (Player player : flightBoard.getPlayerOrderList()) {

            playerMessenger = gameMessenger.getPlayerMessenger(player);
            playerMessenger.printFlightBoard(flightBoard);

            Sleeper.sleepXSeconds(3);

            playerMessenger.printMessage("Your shipboard:\n");

            playerMessenger.printShipboard(player.getShipBoard());

            PlayerFlightInputHandler.addPlayer(player, gameInformation);

        }


        // resolve cards
        // while there are unresolved cards and players in flight
        while (flightBoard.getCardsNumber() > 0 && !flightBoard.getPlayerOrderList().isEmpty()) {

            Sleeper.sleepXSeconds(3);

            card = flightBoard.getNewCard();

            // print card for each player
            for (Player player : gameInformation.getPlayerList()) {
                playerMessenger = gameMessenger.getPlayerMessenger(player);
                playerMessenger.printCard(card);

            }


            Sleeper.sleepXSeconds(3);

            card.resolve(gameInformation);

            Sleeper.sleepXSeconds(3);

            //Printing necessary information after each card to every player
            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);

                playerMessenger.printFlightBoard(flightBoard);

                Sleeper.sleepXSeconds(3);

                playerMessenger.printMessage("Your shipboard:\n");

                playerMessenger.printShipboard(player.getShipBoard());

            }
        }


        //ending input thread
        for (Player player : flightBoard.getPlayerOrderList()) {

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.removePlayer(player);
            }

        }

        gameMessenger.sendMessageToAll("Flight phase has ended.\n");

    }

}
