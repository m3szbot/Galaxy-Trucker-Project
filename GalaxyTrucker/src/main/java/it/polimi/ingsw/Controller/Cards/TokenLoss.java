package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.Messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.Arrays;

/**
 * Interface that define a default method which handles a player being
 * inflicted a loss in terms of crew members or goods.
 *
 * @author carlo
 */

//Player can lose the game if the inhabitants are 0.


public interface TokenLoss {

    /**
     * @param player   target player
     * @param lossType can be either inhabitants or goods
     * @param quantity quantity of the loss
     * @author Carlo
     */

    default void inflictLoss(Player player, ElementType lossType, int quantity, GameInformation gameInformation) {


        if (lossType == ElementType.CrewMember) {

            removeCrewMembers(player, quantity, gameInformation);

        } else {
            //removing goods
            int[] goodsOnShip = player.getShipBoard().getShipBoardAttributes().getGoods();

            //Checking if there's goods on the ship to be removed
            if (goodsOnShip[0] + goodsOnShip[1] + goodsOnShip[2] + goodsOnShip[3] > 0) {

                System.out.println("There are " + Arrays.toString(goodsOnShip) + " goods to be removed on your ship.\n");

                //there are some goods that can be removed
                quantity = removeGoods(player, quantity, goodsOnShip, gameInformation);

            }

            if (quantity > 0) {

                //need to remove batteries
                removeBatteries(player, quantity, gameInformation);

            }
        }

    }

    private void removeCrewMembers(Player player, int quantity, GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;
        int[] coordinates = new int[2];

        //removing crew members or aliens
        int availableCrew = player.getShipBoard().getShipBoardAttributes().getCrewMembers();
        int numberOfCrewToRemove;

        if (quantity >= availableCrew) {
            numberOfCrewToRemove = availableCrew;
        } else {
            numberOfCrewToRemove = quantity;
        }

        while (numberOfCrewToRemove > 0) {

            message = "You must remove " + numberOfCrewToRemove + " inhabitants." +
                    " Enter the coordinates of the cabin of the crew member you want to remove: ";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

            try {
                coordinates = playerMessenger.getPlayerCoordinates();
            } catch (PlayerDisconnectedException e) {
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
            }

            try {
                player.getShipBoard().removeCrewMember(coordinates[0], coordinates[1]);
                numberOfCrewToRemove--;
            } catch (IllegalArgumentException e) {
                message = e.getMessage();
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);
            } catch (NoHumanCrewLeftException e) {
                message = e.getMessage();
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                gameInformation.getFlightBoard().eliminatePlayer(player);
                numberOfCrewToRemove = 0;
            }
        }
    }

    private int removeGoods(Player player, int quantity, int[] goodsOnShip, GameInformation gameInformation) {
        String message;
        PlayerMessenger playerMessenger;
        int numberOfColourGoodsToRemove;
        boolean errorFlag;
        int[] coordinates = new int[2], tempNumberToRemove = new int[]{0, 0, 0, 0};

        //Cycles through the different types of goods on the ship, from the most valuable to the least
        for (int i = 0; i <= 3; i++) {
            //Sets the right number of goods of a particular color that the player has to remove
            if (quantity < goodsOnShip[i]) {
                //Part of them if the total left to remove is lower than the number of goods of that color left
                numberOfColourGoodsToRemove = quantity;
                quantity = 0;
            } else {
                //All of them if the total left to remove is lower than the number of goods of that color left
                numberOfColourGoodsToRemove = goodsOnShip[i];
                quantity = quantity - numberOfColourGoodsToRemove;
            }

            errorFlag = true;
            //Stays in the cycle until there is no error and the number of goods of a color to be removed is 0
            while (errorFlag && numberOfColourGoodsToRemove > 0) {

                errorFlag = false;
                message = "You have to remove " + numberOfColourGoodsToRemove + " " + colorSolver(i) + " goods." +
                        " Enter the coordinates of the storage from which to remove the " + colorSolver(i) + " goods:";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                //Asks the coordinates
                try {
                    coordinates = playerMessenger.getPlayerCoordinates();
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }

                message = "Enter the number of " + colorSolver(i) + " goods you want to remove from this storage:";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    tempNumberToRemove[i] = playerMessenger.getPlayerInt();
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }

                try {
                    player.getShipBoard().removeGoods(coordinates[0], coordinates[1], tempNumberToRemove);
                    numberOfColourGoodsToRemove -= tempNumberToRemove[i];

                } catch (IllegalArgumentException e) {
                    //This makes sure the cycle asking to remove a certain number of goods is repeated until the player puts in the right information
                    errorFlag = true;
                    message = e.getMessage();
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);
                }

                //resets the array for the next cycle
                for (int j = 0; j <= 3; j++) {
                    tempNumberToRemove[j] = 0;
                }
            }
        }

        //In case the goods on the ship were not enough the remaining number is taken from the battery stock
        return quantity;
    }

    private String colorSolver(int good) {

        if (good == 0) {

            return "red";

        } else if (good == 1) {

            return "yellow";

        } else if (good == 2) {

            return "green";

        } else {

            return "blue";

        }
    }

    private void removeBatteries(Player player, int quantity, GameInformation gameInformation) {

        int batteriesAvailable = player.getShipBoard().getShipBoardAttributes().getRemainingBatteries();
        String message;
        PlayerMessenger playerMessenger;
        int numberOfBatteriesToRemove;
        int[] coordinates = new int[2];

        if (batteriesAvailable > 0) {

            //there are batteries that can be removed
            if (batteriesAvailable >= quantity) {
                numberOfBatteriesToRemove = quantity;
            } else {
                numberOfBatteriesToRemove = batteriesAvailable;
            }

            while (numberOfBatteriesToRemove > 0) {

                message = "Enter coordinates of the battery station from which to remove a battery: ";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    coordinates = playerMessenger.getPlayerCoordinates();
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }

                try {
                    player.getShipBoard().removeBattery(coordinates[0], coordinates[1]);
                    numberOfBatteriesToRemove--;

                } catch (IllegalArgumentException e) {
                    message = e.getMessage();
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);
                }
            }

        }
    }

}
