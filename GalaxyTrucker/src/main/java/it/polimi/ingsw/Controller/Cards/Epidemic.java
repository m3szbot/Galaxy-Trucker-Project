package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import javax.management.ObjectInstance;
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
        boolean isEliminated = false;
        int numberOfRemovedInhabitants = 0;


        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

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
                        break;

                    }

                }
            }

            if (isEliminated) {

                message = "Player " + player.getNickName() + " has no crew members left to continue the voyage and has been eliminated!\n";
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

    /**
     * method that handles infected cabins when the epidemic
     * adventure card is being solved: a crew member needs to be removed for each occupied cabin connected to another occupied cabin
     *
     * @param player
     * @author Carlo
     */

    private void removeAdjacentAstronauts(Player player, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        int i, j, numberOfRemovedInhabitants = 0;
        boolean isEliminated = false;
        Component centralComponent, backComponent, frontComponent, leftComponent, rightComponent;

        //all false by default
        boolean[][] infectedCabins = new boolean[rows][cols];

        for (i = 2; i < rows - 1; i++) {

            for (j = 3; j < cols - 2; j++) {

                centralComponent = player.getShipBoard().getRealComponent(i, j);

                //component is not null
                if (centralComponent != null) {
                    //component is a cabin
                    if (centralComponent instanceof Cabin) {
                        //cabin is not empty
                        if (centralComponent.getCrewMembers() > 0) {

                            //the cabin can be potentially infected if some conditions are verified

                            frontComponent = player.getShipBoard().getRealComponent(i, j + 1);
                            backComponent = player.getShipBoard().getRealComponent(i, j - 1);
                            rightComponent = player.getShipBoard().getRealComponent(i + 1, j);
                            leftComponent = player.getShipBoard().getRealComponent(i - 1, j);

                            try {
                                //checking if there is a cabin on the front
                                if (frontComponent != null && !infectedCabins[i][j]) {

                                    if (frontComponent instanceof Cabin) {

                                        if (frontComponent.getCrewMembers() > 0 || infectedCabins[i][j + 1]) {

                                            infectedCabins[i][j] = true;
                                            numberOfRemovedInhabitants++;
                                            player.getShipBoard().removeCrewMember(i + 1, j + 1);
                                        }
                                    }
                                }

                                //checking if there is a cabin on the back
                                if (backComponent != null && !infectedCabins[i][j]) {

                                    if (backComponent instanceof Cabin) {

                                        if (backComponent.getCrewMembers() > 0 || infectedCabins[i][j - 1]) {

                                            infectedCabins[i][j] = true;
                                            numberOfRemovedInhabitants++;
                                            player.getShipBoard().removeCrewMember(i + 1, j + 1);
                                        }
                                    }
                                }

                                //checking if there is a cabin on the right
                                if (rightComponent != null && !infectedCabins[i][j]) {

                                    if (rightComponent instanceof Cabin) {

                                        if (rightComponent.getCrewMembers() > 0 || infectedCabins[i + 1][j]) {

                                            infectedCabins[i][j] = true;
                                            numberOfRemovedInhabitants++;
                                            player.getShipBoard().removeCrewMember(i + 1, j + 1);
                                        }
                                    }
                                }

                                //checking if there is a cabin on the left
                                if (leftComponent != null && !infectedCabins[i][j]) {

                                    if (leftComponent instanceof Cabin) {

                                        if (leftComponent.getCrewMembers() > 0 || infectedCabins[i - 1][j]) {

                                            infectedCabins[i][j] = true;
                                            numberOfRemovedInhabitants++;
                                            player.getShipBoard().removeCrewMember(i + 1, j + 1);
                                        }
                                    }
                                }
                            } catch (NoHumanCrewLeftException e) {
                                message = e.getMessage();
                                playerMessenger.printMessage(message);

                                gameInformation.getFlightBoard().eliminatePlayer(player);
                                isEliminated = true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        if (isEliminated) {
            message = "Player " + player.getNickName() + " has no crew members left to continue the voyage and has been eliminated!\n";
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

}
