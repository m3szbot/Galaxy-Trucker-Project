package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.Components.Battery;
import it.polimi.ingsw.Components.Cannon;
import it.polimi.ingsw.Components.SideType;
import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that define a default method which handles a player
 * being hit by a sequence of blows of a certain type.
 *
 * @author carlo
 */

public interface SufferBlows {

    /**
     *
     * @param player player that is being hit
     * @param blows array of blows that is hitting the player
     * @param blowType type of blow
     * @param flightView class to comunicate with the player
     *
     * @author Carlo
     */

    default void hit(Player player, Blow[] blows, ElementType blowType, FlightView flightView) {

        int randomNumber, i, leftCoord = 0, rightCoord = 0;
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        boolean componentFlag = false, hitFlag = false;
        String direction, message;

        for(Blow blow: blows) {

            randomNumber = (int) (Math.random() * 13); //Generating random number from 0 to 12.


            //blow coming from front
            if (blow.getDirection() == 0) {

                direction = new String("front");

                for (i = 0; i < rows; i++) {

                    if (player.getShipBoard().getComponent(i, randomNumber) != null) {
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }

                }

            }
            //blow coming from right
            else if (blow.getDirection() == 1) {


                direction = new String("right");

                for (i = cols - 1; i >= 0; i--) {

                    if (player.getShipBoard().getComponent(randomNumber, i) != null) {
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }

            }
            //blow coming from back
            else if (blow.getDirection() == 2) {


                direction = new String("back");

                for (i = rows - 1; i >= 0; i--) {

                    if (player.getShipBoard().getComponent(i, randomNumber) != null) {
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }
                }

            }
            //blow coming from left
            else {

                direction = new String("left");

                for (i = 0; i < cols; i++) {

                    if (player.getShipBoard().getComponent(randomNumber, i) != null) {
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }
            }

            if(componentFlag) {
                //a component was hit

                if (blowType == ElementType.CannonBlow) {

                    if (blow.isBig()) {
                        //player cannot defend itself

                        player.getShipBoard().removeComponent(leftCoord, rightCoord);
                        hitFlag = true;

                    } else {
                        //player can defend itself

                        if (player.getShipBoard().getShipBoardAttributes().checkSide(blow.getDirection()) &&
                                player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
                            //player can defent itself with a shield

                            message = new String("A small cannon blow is directed on position ["
                                    + leftCoord + "," + rightCoord + "] from the " +
                                    direction + "!\n Do you want to defend yourself with shields ?");

                            if (flightView.askPlayerGenericQuestion(player, message)) {
                                //player decide to defend itself with shields

                                int[] coordinates;

                                message = "Enter coordinate of the battery station you want to use: ";

                                while (true) {

                                    coordinates = flightView.askPlayerCoordinates(player, message);

                                    if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                        if(((Battery)player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                            break;
                                        }
                                    }

                                    message = "Invalid coordinate, reenter coordinate: ";

                                }

                                player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                            } else { //player decide to not defend itself

                                player.getShipBoard().removeComponent(leftCoord, rightCoord);
                                hitFlag = true;

                            }

                        } else { //player cannot defent itself

                            player.getShipBoard().removeComponent(leftCoord, rightCoord);
                            hitFlag = true;

                        }

                    }

                } else {

                    if (blow.isBig()) { // player can defend itself only with cannon

                        int cannonLeftCoordinate = 0, cannonRightCoordinate = 0;
                        boolean hasCannon = false;

                        if (blow.getDirection() == 0 || blow.getDirection() == 2) {

                            for (i = 0; i < rows; i++) {

                                if (player.getShipBoard().getComponent(i, rightCoord).getComponentName().equals("Cannon")
                                        && ((player.getShipBoard().getComponent(i, rightCoord).getFront() == SideType.Special
                                        && blow.getDirection() == 0)
                                        || (player.getShipBoard().getComponent(i, rightCoord).getBack() == SideType.Special)
                                        && blow.getDirection() == 2)) {

                                    //there is a cannon that can hit the blow

                                    cannonLeftCoordinate = i;
                                    cannonRightCoordinate = rightCoord;

                                    hasCannon = true;
                                    break;

                                }
                            }

                        } else {

                            for (i = 0; i < cols; i++) {

                                if (player.getShipBoard().getComponent(leftCoord, i).getComponentName().equals("Cannon")
                                        && ((player.getShipBoard().getComponent(leftCoord, i).getFront() == SideType.Special
                                        && blow.getDirection() == 0)
                                        || (player.getShipBoard().getComponent(leftCoord, i).getBack() == SideType.Special)
                                        && blow.getDirection() == 2)) {

                                    cannonLeftCoordinate = leftCoord;
                                    cannonRightCoordinate = i;

                                    hasCannon = true;
                                    break;

                                }
                            }

                        }

                        if (hasCannon) { //player has a cannon which point toward the blow.

                            if(!((Cannon)player.getShipBoard().getComponent(cannonLeftCoordinate, cannonRightCoordinate)).isSingle()){ //cannon is not single

                                if( player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0){

                                    message = new String("A big asteroid is directed on position ["
                                            + leftCoord + "," + rightCoord + "] from the " +
                                            direction + "!\n Do you want to defend yourself with the " +
                                            "double cannon pointing towards its direction ?");

                                    if(flightView.askPlayerGenericQuestion(player, message)){ //player decide to defend itself

                                        int[] coordinates = new int[2];

                                        message = "Enter coordinate of the battery want to use: ";

                                        while (true) {

                                            coordinates = flightView.askPlayerCoordinates(player, message);

                                            if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                                if(((Battery)player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                                    break;
                                                }
                                            }

                                            message = "Invalid coordinate, reenter coordinate: ";

                                        }

                                        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                        ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                                    }
                                    else{ //player decide to not defend itself

                                        player.getShipBoard().removeComponent(leftCoord, rightCoord);
                                        hitFlag = true;

                                    }

                                }
                                else{ //player don't have batteries

                                    player.getShipBoard().removeComponent(leftCoord, rightCoord);
                                    hitFlag = true;

                                }

                            }

                            //player has a single cannon, which doesn't require any batteries.

                        } else { //player dont have the cannon

                            player.getShipBoard().removeComponent(leftCoord, rightCoord);
                            hitFlag = true;
                        }

                    }
                    else{ //blow is small

                        //condition is true if the side of the component hit is not smooth
                        if(!((blow.getDirection() == 0
                            && player.getShipBoard().getComponent(leftCoord, rightCoord).getFront() == SideType.Smooth)
                            || (blow.getDirection() == 1
                            && player.getShipBoard().getComponent(leftCoord, rightCoord).getRight() == SideType.Smooth)
                            || (blow.getDirection() == 2
                            && player.getShipBoard().getComponent(leftCoord, rightCoord).getBack() == SideType.Smooth)
                            || (blow.getDirection() == 3
                            && player.getShipBoard().getComponent(leftCoord, rightCoord).getLeft() == SideType.Smooth)))
                        {
                            if(player.getShipBoard().getShipBoardAttributes().checkSide(blow.getDirection())
                                   && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0){
                                //player can defent itself by using batteries

                                message = new String("A small asteroid is directed on position ["
                                        + leftCoord + "," + rightCoord + "] from the " +
                                        direction + "!\n Do you want to defend yourself with shields ?");

                                if (flightView.askPlayerGenericQuestion(player, message)) { //player decide to defend itself

                                    int[] coordinates;

                                    message = "Enter coordinate of the battery want to use: ";

                                    while (true) {

                                        coordinates = flightView.askPlayerCoordinates(player, message);

                                        if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                            if(((Battery)player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                                break;
                                            }
                                        }

                                        message = "Invalid coordinate, reenter coordinate: ";

                                    }

                                    player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                    ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                                }
                                else{

                                    hitFlag = true;
                                    player.getShipBoard().removeComponent(leftCoord, rightCoord);

                                }
                            }
                            else{

                                hitFlag = true;
                                player.getShipBoard().removeComponent(leftCoord, rightCoord);

                            }


                        }

                        //side is smooth

                    }

                }

            }

            //notifying everybody of the blow effect on the player.

            if(hitFlag){

                player.getShipBoard().getShipBoardAttributes().updateDestroyedComponents(1);

                message = "Player " + player.getNickName() + " has been hit by a " +
                        blowType.toString().toLowerCase() + "!";

                flightView.sendMessageToAll(message);
            }
            else{

                message = "Player " + player.getNickName() + " wasn't damaged by the " +
                        blowType.toString().toLowerCase() + " coming at him!";
                flightView.sendMessageToAll(message);
            }
        }
    }

}
