package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

public interface FirePowerChoice {


    /**
     * @param player     target player
     * @param flightView class to comunicate with the players
     * @return firePower of the player
     * @author Carlo
     */


    default float chooseFirePower(Player player, FlightView flightView) {

        String message;
        int forwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberForwardDoubleCannons();
        int notForwardDoubleCannons = player.getShipBoard().getShipBoardAttributes().getNumberNotForwardDoubleCannons();
        float defaultFirePower = player.getShipBoard().getShipBoardAttributes().getFirePower();
        int doubleCannonsToActivate = 0;
        float addedFirePower = 0;

        //checking if purple alien is present
        if (defaultFirePower > 0 && (player.getShipBoard().getShipBoardAttributes().getAlienType() == 1 || player.getShipBoard().getShipBoardAttributes().getAlienType() == 2)) {

            defaultFirePower += 2;

        }

        if (forwardDoubleCannons + notForwardDoubleCannons > 0 && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
            //player can increase firePower.

            message = "You're fire power is " + defaultFirePower +
                    ", but you still have " + forwardDoubleCannons + " double cannons pointing forward (+2 each) and " +
                    notForwardDoubleCannons + " double cannons not pointing forward (+1 each). " +
                    " Would you like to use double cannons to increase you're engine power ?";

            if (flightView.askPlayerGenericQuestion(player, message)) {

                message = "Double cannons will be automatically chosen from the ones that gives more fire power" +
                        "to the ones that gives less. Please enter the number of double cannons you want to" +
                        "activate: ";

                while (true) {

                    doubleCannonsToActivate = flightView.askPlayerValue(player, message);

                    if (doubleCannonsToActivate > 0 && doubleCannonsToActivate <= player.getShipBoard().getShipBoardAttributes().getBatteryPower()
                            && doubleCannonsToActivate <= forwardDoubleCannons + notForwardDoubleCannons) {
                        break;
                    }

                    message = "The value you entered is incorrect, please enter a valid one: ";
                }


                int[] coordinates;

                while (doubleCannonsToActivate > 0) {

                    message = "Enter coordinates of the battery you want to use: ";

                    while (true) {

                        coordinates = flightView.askPlayerCoordinates(player, message);

                        if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                            if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                break;
                            }
                        }

                        message = "Invalid coordinate, reenter coordinate: ";

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

        }

        return defaultFirePower + addedFirePower;

    }
}
