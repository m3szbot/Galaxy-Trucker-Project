package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.Components.Battery;
import it.polimi.ingsw.Model.Components.Cannon;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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
     * @param player   player that is being hit
     * @param blows    array of blows that is hitting the player
     * @param blowType type of blow
     * @author Carlo
     */

    default void hit(Player player, Blow[] blows, ElementType blowType, GameInformation gameInformation) {

        int[] componentCoord = {-1, -1};
        boolean hitFlag = false;

        for (Blow blow : blows) {


            componentCoord = findHitComponent(player, blow, componentCoord);

            if (componentCoord[0] != -1) {
                //a component was hit

                if (blowType == ElementType.CannonBlow) {

                    if (blow.isBig()) {

                        hitFlag = bigCannonBlowHit(player, gameInformation.getFlightBoard(), componentCoord[0], componentCoord[1]);

                    } else {
                        //player can defend itself
                        hitFlag = smallCannonBlowHit(player, componentCoord[0], componentCoord[1], blow.getDirection(), gameInformation);
                    }
                } else {

                    if (blow.isBig()) { // player can defend itself only with cannon


                        hitFlag = bigMeteorBlowHit(player, blow.getDirection(), componentCoord[0], componentCoord[1], gameInformation);

                    } else { //blow is small

                        hitFlag = smallMeteorBlowHit(player, blow.getDirection(), componentCoord[0], componentCoord[1], gameInformation);

                    }
                }
            }

            //notifying everybody of the blow effect on the player.

            notifyAll(player, blow.getDirection(), hitFlag, componentCoord[0], componentCoord[1], blowType, gameInformation);
        }
    }

    private int[] findHitComponent(Player player, Blow blow, int[] coords) {

        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        boolean componentFlag = false;
        int i;
        //blow coming from front
        if (blow.getDirection() == 0) {

            for (i = rows - 1; i >= 0; i--) {

                if (player.getShipBoard().getComponent(blow.getRoll(), i) != null) {
                    coords[1] = i;
                    coords[0] = blow.getRoll();
                    return coords;
                }

            }

        }
        //blow coming from right
        else if (blow.getDirection() == 1) {

            for (i = cols - 1; i >= 0; i--) {

                if (player.getShipBoard().getComponent(i, blow.getRoll()) != null) {
                    coords[1] = blow.getRoll();
                    coords[0] = i;
                    return coords;
                }
            }

        }
        //blow coming from back
        else if (blow.getDirection() == 2) {

            for (i = 0; i < rows; i++) {

                if (player.getShipBoard().getComponent(blow.getRoll(), i) != null) {
                    coords[1] = i;
                    coords[0] = blow.getRoll();
                    return coords;
                }
            }

        }
        //blow coming from left
        else {

            for (i = 0; i < cols; i++) {

                if (player.getShipBoard().getComponent(i, blow.getRoll()) != null) {
                    coords[1] = blow.getRoll();
                    coords[0] = i;
                    return coords;
                }
            }
        }

        return coords;

    }

    private String directionSolver(int direction) {

        if (direction == 0) {
            return "front";
        } else if (direction == 1) {
            return "right";
        } else if (direction == 2) {
            return "back";
        } else if (direction == 3) {
            return "left";
        } else {
            return null;
        }
    }

    private int[] hasCannon(int direction, int xCoord, int yCoord, Player player) {

        int[] cannonCoords = {-1, -1};
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        int i;

        if (direction == 0) {

            for (i = rows - 1; i >= 0; i--) {

                if (player.getShipBoard().getComponent(xCoord, i) != null) {

                    if (player.getShipBoard().getComponent(xCoord, i).getComponentName().equals("Cannon")
                            && (player.getShipBoard().getComponent(xCoord, i).getFront() == SideType.Special)) {

                        //there is a cannon that can hit the blow

                        cannonCoords[0] = xCoord;
                        cannonCoords[1] = i;
                        return cannonCoords;

                    }
                }
            }

        } else if (direction == 1 || direction == 3) {

            int temp;

            for (i = 0; i < cols; i++) {

                temp = yCoord;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;

                temp = yCoord + 1;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;

                temp = yCoord - 1;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;
            }
        } else {
            //blow come from the back
            int temp;

            for (i = 0; i < rows; i++) {

                temp = xCoord;

                if (checkCannonPresenceOnBack(player, cannonCoords, i, temp)) return cannonCoords;

                temp = xCoord + 1;

                if (checkCannonPresenceOnBack(player, cannonCoords, i, temp)) return cannonCoords;

                temp = xCoord - 1;

                if (checkCannonPresenceOnBack(player, cannonCoords, i, temp)) return cannonCoords;
            }

        }

        return cannonCoords;
    }

    private boolean checkCannonPresenceOnBack(Player player, int[] cannonCoords, int i, int temp) {
        if (player.getShipBoard().getComponent(temp, i) != null) {

            if (player.getShipBoard().getComponent(temp, i).getComponentName().equals("Cannon")
                    && ((player.getShipBoard().getComponent(temp, i).getBack() == SideType.Special))) {

                cannonCoords[0] = temp;
                cannonCoords[1] = i;

                return true;
            }

        }
        return false;
    }

    private boolean checkCannonPresenceOnSides(int direction, Player player, int[] cannonCoords, int i, int temp) {

        if (player.getShipBoard().getComponent(i, temp) != null) {

            if (player.getShipBoard().getComponent(i, temp).getComponentName().equals("Cannon")
                    && ((player.getShipBoard().getComponent(i, temp).getRight() == SideType.Special
                    && direction == 1)
                    || (player.getShipBoard().getComponent(i, temp).getLeft() == SideType.Special)
                    && direction == 3)) {

                cannonCoords[0] = i;
                cannonCoords[1] = temp;

                return true;

            }
        }
        return false;
    }

    private boolean bigCannonBlowHit(Player player, FlightBoard flightBoard, int xCoord, int yCoord) {

        return removeComponent(player, xCoord, yCoord, flightBoard);
    }

    private boolean smallCannonBlowHit(Player player, int xCoord, int yCoord, int direction, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;

        if (player.getShipBoard().getShipBoardAttributes().checkSide(direction) &&
                player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
            //player can defend themselves with a shield

            message = "A small cannon blow is directed on position ["
                    + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                    directionSolver(direction) + "!\nDo you want to defend yourself with shields ?";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {
                    //player decide to defend itself with shields

                    return useBattery(player, gameInformation);

                } else { //player decide to not defend itself

                    return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());

                }
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                ;
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            }

        }

        return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());

    }

    private boolean bigMeteorBlowHit(Player player, int direction, int xCoord, int yCoord, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;

        int[] cannonCoords = hasCannon(direction, xCoord, yCoord, player);

        if (cannonCoords[0] != -1) { //player has a cannon which points toward the blow.

            if (!((Cannon) player.getShipBoard().getComponent(cannonCoords[0], cannonCoords[1])).isSingle()) { //cannon is not single

                if (player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {

                    message = "A big asteroid is directed on position ["
                            + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                            direction + "!\nDo you want to defend yourself with the " +
                            "double cannon pointing towards its direction ?";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) { //player decides to defend themselves

                            return useBattery(player, gameInformation);

                        } else { //player decide to not defend itself

                            return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());
                        }
                    } catch (PlayerDisconnectedException e) {
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                        ;
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                    }

                } else { //player don't have batteries

                    return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());

                }

            }

            return false;

        } else { //player doesn't have a cannon

            return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());
        }

    }

    private boolean smallMeteorBlowHit(Player player, int direction, int xCoord, int yCoord, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        //condition is true if the side of the component hit is not smooth
        if (!((direction == 0
                && player.getShipBoard().getComponent(xCoord, yCoord).getFront() == SideType.Smooth)
                || (direction == 1
                && player.getShipBoard().getComponent(xCoord, yCoord).getRight() == SideType.Smooth)
                || (direction == 2
                && player.getShipBoard().getComponent(xCoord, yCoord).getBack() == SideType.Smooth)
                || (direction == 3
                && player.getShipBoard().getComponent(xCoord, yCoord).getLeft() == SideType.Smooth))) {

            if (player.getShipBoard().getShipBoardAttributes().checkSide(direction)
                    && player.getShipBoard().getShipBoardAttributes().getBatteryPower() > 0) {
                //player can defend themselves by using batteries

                message = "A small asteroid is directed on position ["
                        + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                        direction + "!\nDo you want to defend yourself with shields ?";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) { //player decides to defend themselves

                        return useBattery(player, gameInformation);

                    } else {

                        return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());

                    }
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    ;
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                }
            } else {

                return removeComponent(player, xCoord, yCoord, gameInformation.getFlightBoard());

            }


        }

        return false;
        //side is smooth
    }

    private boolean removeComponent(Player player, int xCoord, int yCoord, FlightBoard flightBoard) {

        if (player.getShipBoard().getComponent(xCoord, yCoord).getComponentName().equals("Storage")) {

            int[] goodsToRemove = ((Storage) player.getShipBoard().getComponent(xCoord, yCoord)).getGoods();

            flightBoard.addGoods(goodsToRemove);

        }

        player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
        return true;
    }

    private void notifyAll(Player player, int direction, boolean hitFlag, int xCoord, int yCoord, ElementType blowType, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;

        if (hitFlag) {

            message = "Player " + player.getNickName() + " has been hit by a " +
                    blowType.toString().toLowerCase() + " at position " +
                    "[" + (xCoord + 1) + "," + (yCoord + 1) + "]!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        } else if (xCoord != -1) {

            message = "Player " + player.getNickName() + " wasn't damaged by the " +
                    blowType.toString().toLowerCase() + " that hit position " +
                    "[" + (xCoord + 1) + "," + (yCoord + 1) + "]!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        } else {

            message = "Player " + player.getNickName() + " dodged the " +
                    blowType.toString().toLowerCase() + " coming at him from the " + direction + "!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }
    }

    private boolean useBattery(Player player, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        int[] coordinates = new int[2];

        message = "Enter the coordinates of the battery want to use: ";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

        while (true) {

            try {
                coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                ;
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            }

            if (player.getShipBoard().getComponent(coordinates[0], coordinates[1]) != null) {

                if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))) {
                    if ((player.getShipBoard().getComponent(coordinates[0], coordinates[1])).getBatteryPower() > 0) {
                        break;
                    }
                }
            }

            message = "Invalid coordinates, reenter coordinates: ";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

        }

        player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-1);
        ((Battery) player.getShipBoard().getComponent(coordinates[0], coordinates[1])).removeBattery();
        return false;
    }
}
