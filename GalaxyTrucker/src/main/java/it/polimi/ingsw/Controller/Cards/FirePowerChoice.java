package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
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


    default float chooseFirePower(Player player, GameInformation gameInformation) throws PlayerDisconnectedException {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

        int forwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberForwardDoubleCannons();
        int notForwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberLateralDoubleCannons();
        float defaultFirePower = player.getShipBoard().getShipBoardAttributes().getSingleCannonPower();
        int doubleCannonsToActivate;
        float addedFirePower = 0;

        //checking if purple alien is present
        if (defaultFirePower > 0 && player.getShipBoard().getShipBoardAttributes().getPurpleAlien()) {

            defaultFirePower += 2;

        }

        if (forwardDoubleCannons + notForwardDoubleCannons > 0 && player.getShipBoard().getShipBoardAttributes().getRemainingBatteries() > 0) {

            //player can increase firePower

            message = "Your fire power is " + defaultFirePower +
                    ", but you still have " + forwardDoubleCannons + " double cannons pointing forward (+2 each) and " +
                    notForwardDoubleCannons + " double cannons not pointing forward (+1 each). " +
                    " Would you like to use double cannons to increase your fire power ?";
            playerMessenger.printMessage(message);

            if (playerMessenger.getPlayerBoolean()) {

                message = "Double cannons will be automatically chosen from the ones that give more fire power " +
                        "to the ones that give less. Please enter the number of double cannons you want to " +
                        "activate: ";
                playerMessenger.printMessage(message);

                while (true) {

                    doubleCannonsToActivate = playerMessenger.getPlayerInt();

                    if (doubleCannonsToActivate > 0 && doubleCannonsToActivate <= player.getShipBoard().getShipBoardAttributes().getRemainingBatteries()
                            && doubleCannonsToActivate <= forwardDoubleCannons + notForwardDoubleCannons) {
                        break;
                    }

                    message = "The value you entered is incorrect, please enter a valid one: ";
                    playerMessenger.printMessage(message);

                }


                int[] coordinates;

                while (doubleCannonsToActivate > 0) {

                    message = "Enter coordinates of the battery you want to use: ";
                    playerMessenger.printMessage(message);

                    while (true) {

                        coordinates = playerMessenger.getPlayerCoordinates();

                        try {
                            player.getShipBoard().removeBattery(coordinates[0], coordinates[1]);
                            break;

                        } catch (IllegalArgumentException e) {

                            message = e.getMessage();
                            playerMessenger.printMessage(message);

                        }

                        message = "Invalid coordinate, reenter coordinate: ";
                        playerMessenger.printMessage(message);

                    }

                    doubleCannonsToActivate--;
                    if (forwardDoubleCannons > 0) {

                        addedFirePower += 2;
                        forwardDoubleCannons--;

                    } else {
                        addedFirePower += 1;
                    }

                }

            }


        }

        return defaultFirePower + addedFirePower;

    }
}
