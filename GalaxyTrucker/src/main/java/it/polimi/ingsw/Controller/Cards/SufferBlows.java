package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Controller.FlightPhase.FlightView;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Cannon;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that define a default method which handles a player
 * being hit by a sequence of blows of a certain type.
 *
 * @author carlo
 */

//Check that the remove method in shipboard does everything else, including checking if the ship was destroyed
//in 2 different pieces. REMEMBER THIS

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

    default void hit(Player player, Blow[] blows, ElementType blowType, FlightBoard flightBoard, FlightView flightView) {

        int i, xCoord, yCoord;
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        boolean componentFlag, hitFlag;
        String direction, message;

        for(Blow blow: blows) {

            xCoord = 0;
            yCoord = 0;
            hitFlag = false;
            componentFlag = false;

            //blow coming from front
            if (blow.getDirection() == 0) {

                direction = new String("front");

                for (i = rows - 1; i >= 0; i--) {

                    if (player.getShipBoard().getComponent(blow.getRoll(), i) != null) {
                        componentFlag = true;
                        yCoord = i;
                        xCoord = blow.getRoll();
                        break;
                    }

                }

            }
            //blow coming from right
            else if (blow.getDirection() == 1) {


                direction = new String("right");

                for (i = cols - 1; i >= 0; i--) {

                    if (player.getShipBoard().getComponent(i, blow.getRoll()) != null) {
                        componentFlag = true;
                        yCoord = blow.getRoll();
                        xCoord = i;
                        break;
                    }
                }

            }
            //blow coming from back
            else if (blow.getDirection() == 2) {


                direction = new String("back");

                for (i = 0; i < rows; i++) {

                    if (player.getShipBoard().getComponent(blow.getRoll(), i) != null) {
                        componentFlag = true;
                        yCoord = i;
                        xCoord = blow.getRoll();
                        break;
                    }
                }

            }
            //blow coming from left
            else {

                direction = new String("left");

                for (i = 0; i < cols; i++) {

                    if (player.getShipBoard().getComponent(i, blow.getRoll()) != null) {
                        componentFlag = true;
                        yCoord = blow.getRoll();
                        xCoord = i;
                        break;
                    }
                }
            }

            if(componentFlag) {
                //a component was hit

                if (blowType == ElementType.CannonBlow) {

                    if (blow.isBig()) {
                        //player cannot defend itself

                        //checking if the component is a storage component

                        if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                            int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                            flightBoard.addGoods(goodsToRemove);

                        }

                        player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                        hitFlag = true;

                    } else {
                        //player can defend itself

                        if (player.getShipBoard().getShipBoardAttributes().checkSide(blow.getDirection()) &&
                                player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
                            //player can defent itself with a shield

                            message = new String("A small cannon blow is directed on position ["
                                    + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                                    direction + "!\nDo you want to defend yourself with shields ?");

                            if (flightView.askPlayerGenericQuestion(player, message)) {
                                //player decide to defend itself with shields

                                int[] coordinates;

                                message = "Enter coordinate of the battery station you want to use: ";

                                while (true) {

                                    coordinates = flightView.askPlayerCoordinates(player, message);

                                    if(player.getShipBoard().getComponent(coordinates[0], coordinates[1]) != null) {

                                        if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                            if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                                break;
                                            }
                                        }
                                    }

                                    message = "Invalid coordinate, reenter coordinate: ";

                                }

                                player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                            } else { //player decide to not defend itself

                                if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                    int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                    flightBoard.addGoods(goodsToRemove);

                                }

                                player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                                hitFlag = true;

                            }

                        } else { //player cannot defent itself

                            if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                flightBoard.addGoods(goodsToRemove);

                            }

                            player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                            hitFlag = true;

                        }

                    }

                } else {

                    if (blow.isBig()) { // player can defend itself only with cannon

                        int cannonLeftCoordinate = 0, cannonRightCoordinate = 0;
                        boolean hasCannon = false;

                        if (blow.getDirection() == 0) {

                            for (i = rows - 1; i >= 0; i--) {

                                if(player.getShipBoard().getComponent(xCoord , i) != null) {

                                    if (player.getShipBoard().getComponent(xCoord, i).getComponentName().equals("Cannon")
                                            && (player.getShipBoard().getComponent(xCoord, i).getFront() == SideType.Special)) {

                                        //there is a cannon that can hit the blow

                                        cannonLeftCoordinate = xCoord;
                                        cannonRightCoordinate = i;

                                        hasCannon = true;
                                        break;

                                    }
                                }
                            }

                        }
                        else if(blow.getDirection() == 1 || blow.getDirection() == 3){

                            int temp;

                            for (i = 0; i < cols; i++) {

                                temp = yCoord;

                                if(player.getShipBoard().getComponent(i, temp) != null) {

                                    if (player.getShipBoard().getComponent(i, temp).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(i, temp).getRight() == SideType.Special
                                            && blow.getDirection() == 1)
                                            || (player.getShipBoard().getComponent(i, temp).getLeft() == SideType.Special)
                                            && blow.getDirection() == 3)) {

                                        cannonLeftCoordinate = i;
                                        cannonRightCoordinate = temp;

                                        hasCannon = true;
                                        break;

                                    }
                                }

                                temp = yCoord + 1;

                                if(player.getShipBoard().getComponent(i, temp) != null) {

                                    if (player.getShipBoard().getComponent(i, temp).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(i, temp).getRight() == SideType.Special
                                            && blow.getDirection() == 1)
                                            || (player.getShipBoard().getComponent(i, temp).getLeft() == SideType.Special)
                                            && blow.getDirection() == 3)) {

                                        cannonLeftCoordinate = i;
                                        cannonRightCoordinate = temp;

                                        hasCannon = true;
                                        break;

                                    }
                                }

                                temp = yCoord - 1;

                                if(player.getShipBoard().getComponent(i, temp) != null) {

                                    if (player.getShipBoard().getComponent(i,temp).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(i, temp).getRight() == SideType.Special
                                            && blow.getDirection() == 1)
                                            || (player.getShipBoard().getComponent(i,temp).getLeft() == SideType.Special)
                                            && blow.getDirection() == 3)) {

                                        cannonLeftCoordinate = i;
                                        cannonRightCoordinate = temp;

                                        hasCannon = true;
                                        break;

                                    }

                                }
                            }
                        }
                        else{
                            //blow come from the back
                            int temp;

                            for (i = 0; i < rows; i++) {

                                temp = xCoord;

                                if(player.getShipBoard().getComponent(temp, i) != null) {

                                    if (player.getShipBoard().getComponent(temp, i).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(temp, i).getBack() == SideType.Special))) {

                                        cannonLeftCoordinate = temp;
                                        cannonRightCoordinate = i;

                                        hasCannon = true;
                                        break;

                                    }

                                }

                                temp = xCoord + 1;

                                if(player.getShipBoard().getComponent(temp, i) != null) {

                                    if (player.getShipBoard().getComponent(temp, i).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(temp, i).getBack() == SideType.Special))) {

                                        cannonLeftCoordinate = temp;
                                        cannonRightCoordinate = i;

                                        hasCannon = true;
                                        break;

                                    }

                                }

                                temp = xCoord - 1;

                                if(player.getShipBoard().getComponent(temp, i) != null) {

                                    if (player.getShipBoard().getComponent(temp, i).getComponentName().equals("Cannon")
                                            && ((player.getShipBoard().getComponent(temp, i).getBack() == SideType.Special))) {

                                        cannonLeftCoordinate = temp;
                                        cannonRightCoordinate = i;

                                        hasCannon = true;
                                        break;

                                    }

                                }
                            }

                        }

                        if (hasCannon) { //player has a cannon which point toward the blow.

                            if(!((Cannon)player.getShipBoard().getComponent(cannonLeftCoordinate, cannonRightCoordinate)).isSingle()){ //cannon is not single

                                if( player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0){

                                    message = new String("A big asteroid is directed on position ["
                                            + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                                            direction + "!\nDo you want to defend yourself with the " +
                                            "double cannon pointing towards its direction ?");

                                    if(flightView.askPlayerGenericQuestion(player, message)){ //player decide to defend itself

                                        int[] coordinates;

                                        message = "Enter coordinate of the battery want to use: ";

                                        while (true) {

                                            coordinates = flightView.askPlayerCoordinates(player, message);

                                            if(player.getShipBoard().getComponent(coordinates[0], coordinates[1]) != null) {

                                                if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                                    if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                                        break;
                                                    }
                                                }
                                            }

                                            message = "Invalid coordinate, reenter coordinate: ";

                                        }

                                        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                        ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                                    }
                                    else{ //player decide to not defend itself

                                        if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                            int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                            flightBoard.addGoods(goodsToRemove);

                                        }

                                        player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                                        hitFlag = true;

                                    }

                                }
                                else{ //player don't have batteries

                                    if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                        int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                        flightBoard.addGoods(goodsToRemove);

                                    }

                                    player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                                    hitFlag = true;

                                }

                            }

                            //player has a single cannon, which doesn't require any batteries.

                        } else { //player dont have the cannon

                            if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                flightBoard.addGoods(goodsToRemove);

                            }

                            player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
                            hitFlag = true;
                        }

                    }
                    else{ //blow is small

                        //condition is true if the side of the component hit is not smooth
                        if(!((blow.getDirection() == 0
                            && player.getShipBoard().getComponent(xCoord, yCoord).getFront() == SideType.Smooth)
                            || (blow.getDirection() == 1
                            && player.getShipBoard().getComponent(xCoord, yCoord).getRight() == SideType.Smooth)
                            || (blow.getDirection() == 2
                            && player.getShipBoard().getComponent(xCoord, yCoord).getBack() == SideType.Smooth)
                            || (blow.getDirection() == 3
                            && player.getShipBoard().getComponent(xCoord, yCoord).getLeft() == SideType.Smooth)))
                        {
                            if(player.getShipBoard().getShipBoardAttributes().checkSide(blow.getDirection())
                                   && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0){
                                //player can defent itself by using batteries

                                message = new String("A small asteroid is directed on position ["
                                        + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                                        direction + "!\nDo you want to defend yourself with shields ?");

                                if (flightView.askPlayerGenericQuestion(player, message)) { //player decide to defend itself

                                    int[] coordinates;

                                    message = "Enter coordinate of the battery want to use: ";

                                    while (true) {

                                        coordinates = flightView.askPlayerCoordinates(player, message);

                                        if(player.getShipBoard().getComponent(coordinates[0], coordinates[1]) != null) {
                                            if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                                                if (((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                                                    break;
                                                }

                                            }
                                        }

                                        message = "Invalid coordinate, reenter coordinate: ";

                                    }

                                    player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
                                    ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();

                                }
                                else{

                                    if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                        int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                        flightBoard.addGoods(goodsToRemove);

                                    }

                                    hitFlag = true;
                                    player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);

                                }
                            }
                            else{

                                if(player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")){

                                    int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

                                    flightBoard.addGoods(goodsToRemove);

                                }

                                hitFlag = true;
                                player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);

                            }


                        }

                        //side is smooth

                    }

                }

            }

            //notifying everybody of the blow effect on the player.

            if(hitFlag){

                message = "Player " + player.getNickName() + " has been hit by a " +
                        blowType.toString().toLowerCase() + " at position " +
                        "[" + (xCoord + 1) + "," + (yCoord + 1) + "]!";

                flightView.sendMessageToAll(message);
            }
            else if(componentFlag){

                message = "Player " + player.getNickName() + " wasn't damaged by the " +
                        blowType.toString().toLowerCase() + " that hit position " +
                "[" + (xCoord + 1) + "," + (yCoord + 1) + "]!";

                flightView.sendMessageToAll(message);
            }
            else{

                message = "Player " + player.getNickName() + " dodged the " +
                        blowType.toString().toLowerCase() + " coming at him from the " + direction + "!";
                flightView.sendMessageToAll(message);
            }
        }
    }
}
