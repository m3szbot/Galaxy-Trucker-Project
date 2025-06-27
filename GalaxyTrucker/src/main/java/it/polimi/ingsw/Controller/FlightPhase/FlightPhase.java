package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Class of the flight phase of the game.
 *
 * @author Ludo
 * @author carlo
 * @author boti
 */

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

        for (Player player : flightBoard.getPlayerOrderList()) {

            if (!gameInformation.getPlayerList().contains(player))
                System.err.println("The player in flight on the flightBoard has been disconnected.");

            PlayerFlightInputHandler.addPlayer(player, gameInformation);

        }


        // resolve cards
        // while there are unresolved cards and players in flight
        while (flightBoard.getCardsNumber() > 0 && !flightBoard.getPlayerOrderList().isEmpty()) {

            // print flightBoard for each player
            gameMessenger.sendFlightBoardToAll(flightBoard);

            Sleeper.sleepXSeconds(3);

            // print shipboard for each player
            for (Player player : flightBoard.getPlayerOrderList()) {

                playerMessenger = gameMessenger.getPlayerMessenger(player);
                if (gameMessenger.checkPlayerMessengerPresence(player)) {
                    playerMessenger.printMessage("Your shipboard:\n");
                    playerMessenger.printShipboard(player.getShipBoard());
                }
            }

            Sleeper.sleepXSeconds(3);


            card = flightBoard.getNewCard();
            // print card for each player
            gameMessenger.sendCardToAll(card);

            Sleeper.sleepXSeconds(3);

            card.resolve(gameInformation);

            Sleeper.sleepXSeconds(3);


        }


        //ending input thread
        for (Player player : gameInformation.getPlayerList()) {

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.removePlayer(player);
            }

        }

        //For precaution, checking also the disconnected player list.
        for (Player player : gameInformation.getDisconnectedPlayerList()) {

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.removePlayer(player);
            }

        }

        Sleeper.sleepXSeconds(6);
        gameMessenger.sendMessageToAll("Flight phase has ended.\n");

    }

}
