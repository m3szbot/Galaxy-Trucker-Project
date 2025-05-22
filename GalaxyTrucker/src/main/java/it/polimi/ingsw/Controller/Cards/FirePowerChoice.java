package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

public interface FirePowerChoice {


    /**
     * @param player target player
     * @return firePower of the player
     * @author Carlo
     */


    default float chooseFirePower(Player player, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int forwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberForwardDoubleCannons();
        int notForwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberNotForwardDoubleCannons();
        float defaultFirePower = player.getShipBoard().getShipBoardAttributes().getFirePower();
        int doubleCannonsToActivate;
        float addedFirePower = 0;

        //checking if purple alien is present
        if (defaultFirePower > 0 && (player.getShipBoard().getShipBoardAttributes().getAlienType() == 1 || player.getShipBoard().getShipBoardAttributes().getAlienType() == 2)) {

            defaultFirePower += 2;

        }

        if (forwardDoubleCannons + notForwardDoubleCannons > 0 && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
            //player can increase firePower.

            message = "Your fire power is " + defaultFirePower +
                    ", but you still have " + forwardDoubleCannons + " double cannons pointing forward (+2 each) and " +
                    notForwardDoubleCannons + " double cannons not pointing forward (+1 each). " +
                    " Would you like to use double cannons to increase you're fire power ?";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

            try {
                if (playerMessenger.getPlayerBoolean()) {

                    message = "Double cannons will be automatically chosen from the ones that give more fire power" +
                            "to the ones that give less. Please enter the number of double cannons you want to" +
                            "activate: ";
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);

                    while (true) {

                        doubleCannonsToActivate = playerMessenger.getPlayerInt();

                        if (doubleCannonsToActivate > 0 && doubleCannonsToActivate <= player.getShipBoard().getShipBoardAttributes().getBatteryPower()
                                && doubleCannonsToActivate <= forwardDoubleCannons + notForwardDoubleCannons) {
                            break;
                        }

                        message = "The value you entered is incorrect, please enter a valid one: ";
                        playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                        playerMessenger.printMessage(message);

                    }


                    int[] coordinates;

                    while (doubleCannonsToActivate > 0) {

                        message = "Enter coordinates of the battery you want to use: ";
                        playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                        playerMessenger.printMessage(message);

                        while (true) {

                            coordinates = playerMessenger.getPlayerCoordinates();

                            Component component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                            if (component != null) {
                                if (component.getComponentName().equals("Battery")) {
                                    if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                        break;
                                    }
                                }
                            }

                            message = "Invalid coordinate, reenter coordinate: ";
                            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                            playerMessenger.printMessage(message);

                        }

                        doubleCannonsToActivate--;
                        if (forwardDoubleCannons > 0) {

                            addedFirePower += 2;
                            forwardDoubleCannons--;

                        } else {
                            addedFirePower += 1;
                        }
                        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                        ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                    }
                }
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            }

        }

        return defaultFirePower + addedFirePower;

    }
}
