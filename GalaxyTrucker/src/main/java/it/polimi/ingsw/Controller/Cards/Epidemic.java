package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Messengers.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

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

    @Override

    public void resolve(GameInformation gameInformation) {

        Player player;
        String message;
        PlayerMessenger playerMessenger;

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);

            message = "An epidemic is spreading in your ship!\t You may lose many crew members to the disease!\n";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

            removeAdjacentAstronauts(player, gameInformation);

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player1);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }
    }

    public void showCard() {

        System.out.println("\nCard name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());

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
