package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

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

            message = "You're engine power is " + defaultEnginePower +
                    ", but you still have " + player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines() + " double engine." +
                    " Would you like to use double engines to increase you're engine power ?";
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
            /*
            With the new clientMessenger tool, the client answers yes or no.
             */

            try {
                if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {
                    //player decide to activate some double engines

                    message = "How many double engines would you like to activate ? ";
                    dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                    while (true) {

                        doubleEnginesToActivate = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerInt(player);

                        if (doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getBatteryPower() && doubleEnginesToActivate > 0
                                && doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines()) {
                            break;
                        }

                        message = "The value you entered is incorrect, please enter a valid one: ";
                        dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                    }

                    int[] coordinates = new int[2];
                    int temp = doubleEnginesToActivate;

                    while (temp > 0) {

                        message = "Enter coordinates of the battery you want to use: ";
                        dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                        while (true) {

                            coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);

                            if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                    break;
                                }
                            }

                            message = "Invalid coordinates, reenter coordinates: ";
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

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
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                for (Player player1 : gameInformation.getPlayerList()) {
                    dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                    dataContainer.setMessage(message);
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                }
            }

        }

        return defaultEnginePower;
    }

}
