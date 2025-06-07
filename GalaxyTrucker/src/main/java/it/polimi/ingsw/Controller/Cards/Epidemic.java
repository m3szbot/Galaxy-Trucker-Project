package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FracturedShipBoardHandler;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.FracturedShipBoardException;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;

/**
 * class that represent the card epidemic
 *
 * @author carlo
 */

public class Epidemic extends Card {

    public Epidemic(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();

    }

    public void showCard() {

        System.out.println("\nCard name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        Player player;
        String message;
        PlayerMessenger playerMessenger;
        List<int[]> cabinsToInfect;
        int[] coordinates;
        boolean isEliminated;
        int numberOfRemovedInhabitants = 0;

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            isEliminated = false;

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            message = "An epidemic is spreading in your ship!  You may lose many crew members to the disease!\n";
            playerMessenger.printMessage(message);

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error while sleeping");
            }

            cabinsToInfect = player.getShipBoard().getJoinedCabinsVisibleCoordinates();

            if (!cabinsToInfect.isEmpty()) {

                for (int j = 0; j < cabinsToInfect.size(); j++) {

                    coordinates = cabinsToInfect.get(j);

                    try {
                        player.getShipBoard().removeCrewMember(coordinates[0], coordinates[1]);
                        numberOfRemovedInhabitants++;

                    } catch (NoHumanCrewLeftException e) {

                        message = e.getMessage();
                        playerMessenger.printMessage(message);

                        gameInformation.getFlightBoard().eliminatePlayer(player);
                        isEliminated = true;
                        i--;
                        break;

                    }

                }
            } else {

                message = "The disease couldn't spread in your ship.\n";
                playerMessenger.printMessage(message);

            }

            if (isEliminated) {

                message = "Player " + player.getNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            } else {

                message = "Player " + player.getNickName() + " lost " + numberOfRemovedInhabitants +
                        " inhabitants from the epidemic!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            }

            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                System.out.println("Error while sleeping");
            }

        }

        gameInformation.getFlightBoard().updateFlightBoard();

        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player1);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }

}
