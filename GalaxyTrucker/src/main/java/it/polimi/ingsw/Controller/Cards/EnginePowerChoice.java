package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that defines a default method which let the player choose
 * its engine power.
 *
 * @author carlo
 */

public interface EnginePowerChoice {


    /**
     * @param player target player
     * @return enginePower chosen.
     * @author Carlo
     */

    default int chooseEnginePower(Player player, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int defaultEnginePower = player.getShipBoard().getShipBoardAttributes().getSingleEnginePower();
        int doubleEnginesToActivate;

        //Checking if the brown alien is present
        if (defaultEnginePower > 0 && player.getShipBoard().getShipBoardAttributes().getBrownAlien()) {
            defaultEnginePower += 2;
        }

        if (player.getShipBoard().getShipBoardAttributes().getDoubleEnginePower() > 0 && player.getShipBoard().getShipBoardAttributes().getRemainingBatteries() > 0) {
            //player has the possibility to increase his engine power with batteries

            message = "Your engine power is " + defaultEnginePower +
                    ", but you still have " + player.getShipBoard().getShipBoardAttributes().getDoubleEnginePower() + " double engine.\n" +
                    " Would you like to use double engines to increase you're engine power ?";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);
            /*
            With the new clientMessenger tool, the client answers yes or no.
             */

            try {
                if (playerMessenger.getPlayerBoolean()) {
                    //player decide to activate some double engines

                    message = "How many double engines would you like to activate ? ";
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);

                    while (true) {

                        doubleEnginesToActivate = playerMessenger.getPlayerInt();

                        if (doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getRemainingBatteries() && doubleEnginesToActivate > 0
                                && doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getDoubleEnginePower()) {
                            break;
                        }

                        message = "The value you entered is incorrect, please enter a valid one: ";
                        playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                        playerMessenger.printMessage(message);

                    }

                    int[] coordinates;
                    int temp = doubleEnginesToActivate;

                    while (temp > 0) {

                        message = "Enter coordinates of the battery you want to use: ";
                        playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                        playerMessenger.printMessage(message);

                        while (true) {

                            coordinates = playerMessenger.getPlayerCoordinates();

                            try {
                                player.getShipBoard().removeBattery(coordinates[0], coordinates[1]);

                                break;
                            } catch (IllegalArgumentException e) {
                                message = e.getMessage();
                                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                                playerMessenger.printMessage(message);
                            }


                            message = "Invalid coordinates, reenter coordinates: ";
                            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                            playerMessenger.printMessage(message);

                        }
                        temp--;
                    }

                    return defaultEnginePower + doubleEnginesToActivate * 2;

                } else {
                    return defaultEnginePower;
                }
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            }

        }

        return defaultEnginePower;
    }

}
