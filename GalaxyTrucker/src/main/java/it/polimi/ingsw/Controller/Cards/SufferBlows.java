package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Messengers.PlayerMessenger;
import it.polimi.ingsw.Model.Components.Cannon;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

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

        int[] componentCoordinates = new int[2];
        boolean hitFlag;

        for (Blow blow : blows) {

            hitFlag = false;

            if (blow != null) {

                //Pause before each blow
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    System.out.println("Error while sleeping");
                }

                componentCoordinates = findHitComponent(player, blow, componentCoordinates);

                if (componentCoordinates[0] != -1 && componentCoordinates[1] != -1) {

                    //a component was hit
                    if (blowType == ElementType.CannonBlow) {

                        if (blow.isBig()) {

                            hitFlag = bigCannonBlowHit(player, gameInformation, componentCoordinates[0], componentCoordinates[1]);

                        } else {

                            //player can defend itself
                            hitFlag = smallCannonBlowHit(player, componentCoordinates[0], componentCoordinates[1], blow.getDirection(), gameInformation);
                        }
                    } else {

                        //BlowType is Meteorite
                        if (blow.isBig()) {

                            // player can defend itself only with cannon
                            hitFlag = bigMeteorBlowHit(player, blow.getDirection(), componentCoordinates[0], componentCoordinates[1], gameInformation);

                        } else {

                            //blow is small
                            hitFlag = smallMeteorBlowHit(player, blow.getDirection(), componentCoordinates[0], componentCoordinates[1], gameInformation);

                        }
                    }
                }

                //notifying everybody of the blow effect on the player.
                notifyAll(player, blow.getDirection(), hitFlag, componentCoordinates[0], componentCoordinates[1], blowType, gameInformation);
            }
        }
    }

    private int[] findHitComponent(Player player, Blow blow, int[] coords) {

        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        int i, roll = blow.getRoll();

        //Default value if there's no component to be hit in a specific row or column
        coords[0] = -1;
        coords[1] = -1;

        //blow coming from front
        if (blow.getDirection() == 0) {

            //From visible row index 5 up to 9 (5 ->9)
            for (i = 4; i <= rows - 3; i++) {

                if (player.getShipBoard().getRealComponent(roll, i) != null) {

                    //If it finds a component that could be hit
                    coords[1] = i;
                    coords[0] = roll;

                }
            }

        }
        //blow coming from right
        else if (blow.getDirection() == 1) {

            //From visible col index 10 down to 4 (10 -> 4)
            for (i = cols - 2; i >= 3; i--) {

                if (player.getShipBoard().getRealComponent(i, roll) != null) {
                    coords[1] = roll;
                    coords[0] = i;
                }
            }

        }
        //blow coming from back
        else if (blow.getDirection() == 2) {

            //From visible row index 9 down to 5 (9 -> 5)
            for (i = rows - 3; i >= 4; i--) {

                if (player.getShipBoard().getRealComponent(roll, i) != null) {
                    coords[1] = i;
                    coords[0] = roll;
                }
            }

        }
        //blow coming from left
        else {

            //From visible col index 4 up to 10 (4 -> 10)
            for (i = 3; i <= cols - 2; i++) {

                if (player.getShipBoard().getRealComponent(i, roll) != null) {
                    coords[1] = roll;
                    coords[0] = i;
                }
            }

        }

        return coords;

    }

    private boolean bigCannonBlowHit(Player player, GameInformation gameInformation, int xCoord, int yCoord) {

        return removeComponent(player, xCoord, yCoord, gameInformation);

    }

    private boolean removeComponent(Player player, int xCoord, int yCoord, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;

        try {
            player.getShipBoard().removeComponent(xCoord + 1, yCoord + 1, true);
        } catch (NoHumanCrewLeftException e) {
            message = e.getMessage();
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

            gameInformation.getFlightBoard().eliminatePlayer(player);
        }
        return true;

    }

    private boolean smallCannonBlowHit(Player player, int xCoord, int yCoord, int direction, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        boolean hitFlag = false;
        ShipBoard playerShipBoard = player.getShipBoard();

        if (playerShipBoard.getShipBoardAttributes().checkSideShieldProtected(direction) && playerShipBoard.getShipBoardAttributes().getRemainingBatteries() > 0) {

            //player can defend themselves with a shield and has batteries to do so
            message = "A small cannon blow is directed on position ["
                    + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                    directionSolver(direction) + "!\nDo you want to defend yourself with shields ?";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

            try {
                if (playerMessenger.getPlayerBoolean()) {

                    //player decides to defend themselves with shields
                    hitFlag = useBattery(player, gameInformation);

                } else { //player decides to not defend themselves

                    hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);

                }
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
            }

        } else {

            //Removes the component if there's no shield
            hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);
        }

        //False if not hit
        return hitFlag;
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

    //Returns
    private boolean useBattery(Player player, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int[] coordinates = new int[2];

        message = "Enter the coordinates of the battery you want to use: ";
        playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        playerMessenger.printMessage(message);

        while (true) {

            try {
                coordinates = playerMessenger.getPlayerCoordinates();

            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
            }

            try {
                player.getShipBoard().removeBattery(coordinates[0], coordinates[1]);
                break;
            } catch (IllegalArgumentException e) {
                message = e.getMessage();
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);
            }

            //If there was an exception
            message = "Reenter coordinates: ";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

        }

        return false;

    }

    private boolean bigMeteorBlowHit(Player player, int direction, int xCoord, int yCoord, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        ShipBoard playerShipBoard = player.getShipBoard();
        boolean hitFlag = false;

        int[] cannonCoords = hasCannon(direction, xCoord, yCoord, player);

        if (cannonCoords[0] != -1) { //player has a cannon which points toward the blow.

            if (!player.getShipBoard().getRealComponent(cannonCoords[0], cannonCoords[1]).isSingle()) { //cannon is not single

                if (playerShipBoard.getShipBoardAttributes().getRemainingBatteries() > 0) {

                    message = "A big asteroid is directed on position ["
                            + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                            directionSolver(direction) + "!\nDo you want to defend yourself with the " +
                            "double cannon pointing towards its direction ?";
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);

                    try {
                        if (playerMessenger.getPlayerBoolean()) { //player decides to defend themselves

                            hitFlag = useBattery(player, gameInformation);

                        } else { //player decide to not defend itself

                            hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);
                        }
                    } catch (PlayerDisconnectedException e) {
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    }

                } else { //player doesn't have batteries

                    hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);

                }

            } else {

                //If the player has a single cannon directed towards the meteorite the blow hits
                hitFlag = true;
            }

        } else { //player doesn't have a cannon

            hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);
        }

        return hitFlag;

    }

    private int[] hasCannon(int direction, int xCoord, int yCoord, Player player) {

        int[] cannonCoords = {-1, -1};
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        int i;

        if (direction == 0) { //Blow comes from the front

            //Searches for the first not null component on the x column
            for (i = rows - 1; i >= 0; i--) {

                if (player.getShipBoard().getRealComponent(xCoord, i) != null) {

                    if (player.getShipBoard().getRealComponent(xCoord, i) instanceof Cannon
                            && (player.getShipBoard().getRealComponent(xCoord, i).getFront() == SideType.Special)) {

                        //there is a cannon that can hit the blow

                        cannonCoords[0] = xCoord;
                        cannonCoords[1] = i;

                    }
                }
            }

        } else if (direction == 1 || direction == 3) { //Blow comes from the sides

            int temp;

            //Searches for the first not null component on the y row
            for (i = 0; i < cols; i++) {

                temp = yCoord;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;

                temp = yCoord + 1;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;

                temp = yCoord - 1;

                if (checkCannonPresenceOnSides(direction, player, cannonCoords, i, temp)) return cannonCoords;
            }

        } else {
            //blow comes from the back
            int temp;

            //Same as direction == 0 but inverted
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

    private boolean checkCannonPresenceOnSides(int direction, Player player, int[] cannonCoords, int i, int temp) {

        if (player.getShipBoard().getRealComponent(i, temp) != null) {

            if (player.getShipBoard().getRealComponent(i, temp) instanceof Cannon
                    && ((player.getShipBoard().getRealComponent(i, temp).getRight() == SideType.Special
                    && direction == 1)
                    || (player.getShipBoard().getRealComponent(i, temp).getLeft() == SideType.Special)
                    && direction == 3)) {

                cannonCoords[0] = i;
                cannonCoords[1] = temp;

                return true;

            }
        }
        return false;
    }

    private boolean checkCannonPresenceOnBack(Player player, int[] cannonCoords, int i, int temp) {
        if (player.getShipBoard().getRealComponent(temp, i) != null) {

            if (player.getShipBoard().getRealComponent(temp, i) instanceof Cannon
                    && ((player.getShipBoard().getRealComponent(temp, i).getBack() == SideType.Special))) {

                cannonCoords[0] = temp;
                cannonCoords[1] = i;

                return true;
            }

        }
        return false;
    }

    private boolean smallMeteorBlowHit(Player player, int direction, int xCoord, int yCoord, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        ShipBoard playerShipBoard = player.getShipBoard();
        boolean hitFlag = false;

        //condition is true if the side of the component hit is not smooth
        if ((direction == 0
                && player.getShipBoard().getRealComponent(xCoord, yCoord).getFront() != SideType.Smooth)
                || (direction == 1
                && player.getShipBoard().getRealComponent(xCoord, yCoord).getRight() != SideType.Smooth)
                || (direction == 2
                && player.getShipBoard().getRealComponent(xCoord, yCoord).getBack() != SideType.Smooth)
                || (direction == 3
                && player.getShipBoard().getRealComponent(xCoord, yCoord).getLeft() != SideType.Smooth)) {

            if (playerShipBoard.getShipBoardAttributes().checkSideShieldProtected(direction) && playerShipBoard.getShipBoardAttributes().getRemainingBatteries() > 0) {

                //player can defend themselves by using shields
                message = "A small asteroid is directed on position ["
                        + (xCoord + 1) + "," + (yCoord + 1) + "] from the " +
                        directionSolver(direction) + "!\nDo you want to defend yourself with shields ?";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) { //player decides to defend themselves

                        hitFlag = useBattery(player, gameInformation);

                    } else {

                        hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);

                    }
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }
            } else {

                hitFlag = removeComponent(player, xCoord, yCoord, gameInformation);

            }

        }

        //If side is smooth there is no damage
        //False if not hit
        return hitFlag;
    }

    private void notifyAll(Player player, int direction, boolean hitFlag, int xCoord, int yCoord, ElementType blowType, GameInformation gameInformation) {

        String message;

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
                    blowType.toString().toLowerCase() + " coming at him from the " + directionSolver(direction) + "!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

        }
    }
}
