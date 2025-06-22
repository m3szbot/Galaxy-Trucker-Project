package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Interface that define a method which handles a player receiving
 * a certain amount of goods
 *
 * @author carlo
 */

public interface GoodsGain {

    /**
     * @param player indicates the target player
     * @param goods  goods to give to the target player
     * @author Carlo
     */

    default void giveGoods(Player player, int[] goods, GameInformation gameInformation) throws PlayerDisconnectedException {

        discardingPhase(player, gameInformation);
        rearrangementPhase(player, gameInformation);
        GoodsPlacementPhase(player, goods, gameInformation);

    }

    private void discardingPhase(Player player, GameInformation gameInformation) throws PlayerDisconnectedException {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        boolean discardingPhaseFlag;
        int[] coordinates;

        message = "Are there some goods that you want to discard ?";
        playerMessenger.printMessage(message);

        discardingPhaseFlag = playerMessenger.getPlayerBoolean();

        while (discardingPhaseFlag) {
            //player decides to discard some goods

            message = "Enter coordinates of the storage component: ";
            playerMessenger.printMessage(message);

            coordinates = playerMessenger.getPlayerCoordinates();


            int[] goodsToRemove;
            goodsToRemove = askForGoods(player, "remove", 0, 3, gameInformation);

            try {
                //Tries to remove the goods inserted by the player
                player.getShipBoard().removeGoods(coordinates[0], coordinates[1], goodsToRemove);

                message = "The goods have been successfully removed from the component at [" + coordinates[0] + "," + coordinates[1] + "].\n";
                playerMessenger.printMessage(message);

            } catch (IllegalSelectionException e) {

                message = e.getMessage();
                playerMessenger.printMessage(message);

            }

            message = "Do you still want to discard some goods?";
            playerMessenger.printMessage(message);

            discardingPhaseFlag = playerMessenger.getPlayerBoolean();

            //Resets the array for the next cycle
            for (int j = 0; j <= 3; j++) {
                goodsToRemove[j] = 0;
            }

        }

    }

    private int[] askForGoods(Player player, String messageType, int start, int end, GameInformation gameInformation) throws PlayerDisconnectedException {

        int[] goods = {0, 0, 0, 0};
        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        String[] colors = {"red", "yellow", "green", "blue"};

        for (int i = start; i <= end; i++) {

            message = "Enter number of " + colors[i] + " goods to " + messageType;
            playerMessenger.printMessage(message);

            goods[i] = playerMessenger.getPlayerInt();


        }

        return goods;
    }

    private void rearrangementPhase(Player player, GameInformation gameInformation) throws PlayerDisconnectedException {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        boolean rearrangementPhaseFlag;

        message = "Are there some goods that you want to rearrange ?";
        playerMessenger.printMessage(message);

        rearrangementPhaseFlag = playerMessenger.getPlayerBoolean();

        while (rearrangementPhaseFlag) {
            //player decide that he wants to rearrange some goods

            int[] sourceCoordinates, destCoordinates;

            message = "Enter coordinates of the source storage : ";
            playerMessenger.printMessage(message);

            sourceCoordinates = playerMessenger.getPlayerCoordinates();

            message = "Enter coordinates of the destination storage : ";
            playerMessenger.printMessage(message);

            destCoordinates = playerMessenger.getPlayerCoordinates();

            int[] movingGoods;
            movingGoods = askForGoods(player, "move", 0, 3, gameInformation);

            try {
                player.getShipBoard().moveGoods(sourceCoordinates[0], sourceCoordinates[1], destCoordinates[0], destCoordinates[1], movingGoods);

                message = "The goods have successfully been moved.\n";
                playerMessenger.printMessage(message);

            } catch (IllegalSelectionException e) {

                //If an error occurs (caused by the player giving wrong information), they are asked if they still want to rearrange and repeat the cycle
                message = e.getMessage();
                playerMessenger.printMessage(message);

            }

            message = "Do you still want to rearrange some goods ?";
            playerMessenger.printMessage(message);

            rearrangementPhaseFlag = playerMessenger.getPlayerBoolean();

            //Resets the array for the next cycle
            for (int j = 0; j <= 3; j++) {
                movingGoods[j] = 0;
            }

        }
    }

    private void GoodsPlacementPhase(Player player, int[] goods, GameInformation gameInformation) throws PlayerDisconnectedException {
        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        boolean placementPhaseFlag, isDone;
        int[] coordinates, goodsToAdd = new int[4];
        int remainingRedSlots, remainingBlueSlots, numberOfGoods = 0;
        String[] colors = {"red", "yellow", "green", "blue"};

        //Checks if there's goods to add
        if (goods[0] > 0 || goods[1] > 0 || goods[2] > 0 || goods[3] > 0) {

            //Checks if there's storage space left and asks if the player wants to remove some goods
            if (!(player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() > 0 || player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() > 0)) {
                message = "You don't have enough storage slots available on your ship.\n";
                playerMessenger.printMessage(message);

                discardingPhase(player, gameInformation);

            }

            //If the player has space on their ship
            if (player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() > 0 || player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() > 0) {

                message = "Do you want to add goods to your ship ? ";
                playerMessenger.printMessage(message);

                placementPhaseFlag = playerMessenger.getPlayerBoolean();

                while (placementPhaseFlag) {

                    isDone = false;

                    for (int i = 0; i <= 3 && !isDone; i++) {

                        if (goods[i] != 0) {

                            message = "There are " + goods[i] + " " + colors[i] + " goods, do you want to add some? ";
                            playerMessenger.printMessage(message);

                            isDone = !playerMessenger.getPlayerBoolean();

                            while (!isDone) {

                                //Asks for the number of goods of color i to add
                                message = "How many " + colors[i] + " goods do you want to add? ";
                                playerMessenger.printMessage(message);

                                goodsToAdd[i] = playerMessenger.getPlayerInt();

                                //Asks for coordinates
                                message = "Enter coordinates of storage component where to place the goods: ";
                                playerMessenger.printMessage(message);

                                coordinates = playerMessenger.getPlayerCoordinates();

                                try {
                                    //Tries to add the goods to the specified component
                                    player.getShipBoard().addGoods(coordinates[0], coordinates[1], goodsToAdd);

                                    goods[i] -= goodsToAdd[i];

                                    message = "The goods have been successfully added to the component at [" + coordinates[0] + "," + coordinates[1] + "].\n";
                                    playerMessenger.printMessage(message);

                                } catch (IllegalSelectionException e) {

                                    message = e.getMessage();
                                    playerMessenger.printMessage(message);

                                }

                                remainingRedSlots = player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots();
                                remainingBlueSlots = player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots();

                                //If there's goods of color i left to be taken
                                if (goods[i] != 0 && (remainingBlueSlots > 0 || remainingRedSlots > 0)) {

                                    message = "Do you still want to add " + goods[i] + " " + colors[i] + " goods to your ship? ";
                                    playerMessenger.printMessage(message);

                                    isDone = !playerMessenger.getPlayerBoolean();

                                } else {
                                    isDone = true;
                                }

                                //Resets the array for the next cycle
                                for (int j = 0; j <= 3; j++) {
                                    goodsToAdd[j] = 0;
                                }

                            }
                        }

                        remainingRedSlots = player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots();
                        remainingBlueSlots = player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots();

                        //Checks if the player can add more goods
                        isDone = !((goods[0] > 0 || goods[1] > 0 || goods[2] > 0 || goods[3] > 0) && (remainingRedSlots > 0 || remainingBlueSlots > 0));

                        //If there's goods left to take and the player still has space on the ship the cycle repeats

                    }

                    remainingRedSlots = player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots();
                    remainingBlueSlots = player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots();

                    //Checks if the player can add more goods
                    if ((goods[0] > 0 || goods[1] > 0 || goods[2] > 0 || goods[3] > 0) && (remainingRedSlots > 0 || remainingBlueSlots > 0)) {

                        //Asks if the player wants to keep adding good to their ship
                        message = "Do you still want to add goods to your ship ?";
                        playerMessenger.printMessage(message);

                        placementPhaseFlag = playerMessenger.getPlayerBoolean();

                    } else {
                        placementPhaseFlag = false;
                    }

                }

            }

        } else {

            message = "There's no more goods left to take.\n";
            playerMessenger.printMessage(message);

        }
    }

}
