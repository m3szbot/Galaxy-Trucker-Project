package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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
        boolean discardingPhaseFlag = false;
        int[] coordinates = new int[2];

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

            //Tries to remove the goods inserted by the player
            player.getShipBoard().removeGoods(coordinates[0], coordinates[1], goodsToRemove);

            message = "The goods have been successfully removed from the component at [" + coordinates[0] + "," + coordinates[1] + "].\n";
            playerMessenger.printMessage(message);


            message = "Do you still want to discard some goods?";
            playerMessenger.printMessage(message);

            discardingPhaseFlag = playerMessenger.getPlayerBoolean();
        }

    }

    private void rearrangementPhase(Player player, GameInformation gameInformation) throws PlayerDisconnectedException {

        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        boolean rearrangementPhaseFlag = false, errorFlag = true;

        message = "Are there some goods that you want to rearrange ?";
        playerMessenger.printMessage(message);

        rearrangementPhaseFlag = playerMessenger.getPlayerBoolean();

        while (rearrangementPhaseFlag) {
            //player decide that he wants to rearrange some goods

            int[] sourceCoordinates = new int[2], destCoordinates = new int[2];
            int[] sourceGoods;

            message = "Enter coordinate of the source storage component: ";
            playerMessenger.printMessage(message);

            sourceCoordinates = playerMessenger.getPlayerCoordinates();

            message = "Enter coordinate of the destination storage component: ";
            playerMessenger.printMessage(message);

            destCoordinates = playerMessenger.getPlayerCoordinates();

            int[] movingGoods;
            movingGoods = askForGoods(player, "move", 0, 3, gameInformation);

            try {
                player.getShipBoard().moveGoods(sourceCoordinates[0], sourceCoordinates[1], destCoordinates[0], destCoordinates[1], movingGoods);

                message = "The goods have successfully been moved.\n";
                playerMessenger.printMessage(message);

            } catch (IllegalArgumentException e) {
                //If an error occurs (caused by the player giving wrong information), they are asked if they still want to rearrange and repeat the cycle
                message = e.getMessage();
                playerMessenger.printMessage(message);
            }

            message = "Do you still want to rearrange some goods ?";
            playerMessenger.printMessage(message);

            rearrangementPhaseFlag = playerMessenger.getPlayerBoolean();

        }
    }


    private void GoodsPlacementPhase(Player player, int[] goods, GameInformation gameInformation) throws PlayerDisconnectedException {
        String message;
        PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
        boolean placementPhaseFlag = false;
        int[] coordinates = new int[2], goodsToAdd;
        int remainingRedSlots, remainingBlueSlots;

        //Check if there's goods left to add
        if (goods[0] > 0 || goods[1] > 0 || goods[2] > 0 || goods[3] > 0) {

            //Check if there's storage space left
            if (!(player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots() > 0 || player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots() > 0)) {
                message = "You don't have enough storage slots available on your ship.\n";
                playerMessenger.printMessage(message);
            } else {

                message = "Do you want to add goods to your ship ? ";
                playerMessenger.printMessage(message);


                placementPhaseFlag = playerMessenger.getPlayerBoolean();

                while (placementPhaseFlag) {

                    //Asks for coordinates
                    message = "Enter coordinates of storage component to place the goods: ";
                    playerMessenger.printMessage(message);

                    coordinates = playerMessenger.getPlayerCoordinates();

                    //Asks for which goods they want to add to the storage
                    goodsToAdd = askForGoods(player, "add", 0, 3, gameInformation);

                    //Tries to add the goods to the specified component
                    player.getShipBoard().addGoods(coordinates[0], coordinates[1], goodsToAdd);

                    message = "The goods have been successfully added to the component at [" + coordinates[0] + "," + coordinates[1] + "].\n";
                    playerMessenger.printMessage(message);


                    remainingRedSlots = player.getShipBoard().getShipBoardAttributes().getRemainingRedSlots();
                    remainingBlueSlots = player.getShipBoard().getShipBoardAttributes().getRemainingBlueSlots();

                    //Checks if the player can add more goods
                    if ((goods[0] > 0 || goods[1] > 0 || goods[2] > 0 || goods[3] > 0) && (remainingRedSlots > 0 || remainingBlueSlots > 0)) {

                        //Asks if the player wants to keep adding good to their ship
                        message = "Do you still want to add goods to your ship ?";
                        playerMessenger.printMessage(message);

                        placementPhaseFlag = playerMessenger.getPlayerBoolean();

                        //Resets the array for the next cycle
                        for (int j = 0; j <= 3; j++) {
                            goodsToAdd[j] = 0;
                        }
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

}
