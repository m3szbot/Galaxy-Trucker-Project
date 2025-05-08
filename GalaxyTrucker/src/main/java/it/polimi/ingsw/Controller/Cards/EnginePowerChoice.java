package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

/**
 * Interface that defines a default method which let the player choose
 * its engine power.
 *
 * @author carlo
 */

public interface EnginePowerChoice {


    /**
     * @param player     target player
     * @param flightView class to comunicate with the player
     * @return enginePower chosen.
     * @author Carlo
     */

    default int chooseEnginePower(Player player, FlightView flightView) {

        String message;
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
            /*
            With the new clientMessenger tool, the client answers yes or no.
             */

            if (flightView.askPlayerGenericQuestion(player, message)) {
                //player decide to activate some double engines

                message = "How many double engines would you like to activate ? ";

                while (true) {

                    doubleEnginesToActivate = flightView.askPlayerValue(player, message);

                    if (doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getBatteryPower() && doubleEnginesToActivate > 0
                            && doubleEnginesToActivate <= player.getShipBoard().getShipBoardAttributes().getNumberDoubleEngines()) {
                        break;
                    }

                    message = "The value you entered is incorrect, please enter a valid one: ";

                }

                int[] coordinates;
                int temp = doubleEnginesToActivate;

                while (temp > 0) {

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

                    temp--;
                    player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                    ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                }

                return defaultEnginePower + doubleEnginesToActivate * 2;


            } else {
                return defaultEnginePower;
            }

        } else {
            return defaultEnginePower;
        }

    }

}
