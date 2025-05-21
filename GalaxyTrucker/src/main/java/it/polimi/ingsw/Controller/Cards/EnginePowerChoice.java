package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
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
        DataContainer dataContainer;
        int defaultEnginePower = player.getShipBoard().getShipBoardAttributes().getDrivingPower();
        int doubleEnginesToActivate;

        //Checking if the brown alien is present
        if (defaultEnginePower > 0 && (player.getShipBoard().getShipBoardAttributes().getAlienType() == 1 || player.getShipBoard().getShipBoardAttributes().getAlienType() == 3)) {
            defaultEnginePower += 2;
        }


        if (player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines() > 0 && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
            //player have the possibility to increase his engine power with batteries

            message = "Your engine power is " + defaultEnginePower +
                    ", but you still have " + player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines() + " double engine." +
                    " Would you like to use double engines to increase you're engine power ?";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);
            /*
            With the new clientMessenger tool, the client answers yes or no.
             */

            try {
                if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {
                    //player decide to activate some double engines

                    message = "How many double engines would you like to activate ? ";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    while (true) {

                        doubleEnginesToActivate = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerInt(player);

                        if (doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getBatteryPower() && doubleEnginesToActivate > 0
                                && doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines()) {
                            break;
                        }

                        message = "The value you entered is incorrect, please enter a valid one: ";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    }

                    int[] coordinates;
                    int temp = doubleEnginesToActivate;

                    while (temp > 0) {

                        message = "Enter coordinates of the battery you want to use: ";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        while (true) {

                            coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);

                            Component component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                            if (component != null) {
                                if (component.getComponentName().equals("Battery")) {
                                    if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                        break;
                                    }
                                }
                            }

                            message = "Invalid coordinates, reenter coordinates: ";
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        }

                        temp--;
                        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                        ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

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
