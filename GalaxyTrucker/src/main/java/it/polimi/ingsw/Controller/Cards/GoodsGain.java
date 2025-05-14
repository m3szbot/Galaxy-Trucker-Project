package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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

    default void giveGoods(Player player, int[] goods, GameInformation gameInformation) {

        discardingPhase(player, gameInformation);
        rearrangementPhase(player, gameInformation);
        redGoodsPlacementPhase(player, goods, gameInformation);
        nonRedGoodsPlacementPhasePhase(player, goods, gameInformation);

    }

    private void discardingPhase(Player player, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        boolean discardingPhaseFlag = false, errorFlag = true;
        int[] coordinates = new int[2];
        Component component;

        message = "Are there some goods that you want to discard ?";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

        try {
            discardingPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
        } catch (PlayerDisconnectedException e) {
            gameInformation.disconnectPlayer(player);
            message = e.getMessage();
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
        }

        while (discardingPhaseFlag) {
            //player decide to discard some goods

            message = "Enter coordinates of the storage component: ";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

            component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

            if (component.getComponentName().equals("Storage")) {

                int availableGoods[] = ((Storage) component).getGoods();

                if (!((Storage) component).isEmpty()) {

                    while (errorFlag) {

                        int goodsToRemove[];

                        goodsToRemove = askForGoods(player, "remove", 0, 3, gameInformation);

                        if (checkGoodsAvailability(goodsToRemove, availableGoods, 0, 3)) {
                            //value entered are correct

                            ((Storage) component).removeGoods(goodsToRemove);
                            gameInformation.getFlightBoard().addGoods(goodsToRemove);
                            player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{-goodsToRemove[0], -goodsToRemove[1], -goodsToRemove[2], -goodsToRemove[3]});

                            if (((Storage) component).isRed()) {

                                player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, goodsToRemove[0] + goodsToRemove[1] + goodsToRemove[2] + goodsToRemove[3]);

                            } else {

                                player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, goodsToRemove[1] + goodsToRemove[2] + goodsToRemove[3]);

                            }

                            errorFlag = false;

                        } else {

                            message = "The goods you entered are incorrect";
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        }

                        //value entered are incorrect, add notification to player

                    }
                } else {

                    message = "The storage component you entered is empty, do you still want to discard some goods ?";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        discardingPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                    continue;

                }
            } else {

                message = "The component you entered is not a storage component! Do you still" +
                        " want to discard some goods ? (Yes/No)";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    discardingPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }

                continue;
            }

            message = "Are there some other goods that you want to discard ?";
            errorFlag = true;
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                discardingPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

        }

    }

    private void rearrangementPhase(Player player, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        boolean rearrangementPhaseFlag = false, errorFlag = true;

        message = "Are there some goods that you want to rearrange ?";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

        try {
            rearrangementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
        } catch (PlayerDisconnectedException e) {
            gameInformation.disconnectPlayer(player);
            message = e.getMessage();
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
        }

        while (rearrangementPhaseFlag) {
            //player decide that he wants to rearrange some goods

            int[] sourceCoordinates = new int[2], destCoordinates = new int[2];
            Component sourceComponent, destComponent;
            int[] sourceGoods;

            message = "Enter coordinate of the source storage component: ";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                sourceCoordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

            message = "Enter coordinate of the destination storage component: ";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                destCoordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

            sourceComponent = player.getShipBoard().getComponent(sourceCoordinates[0], sourceCoordinates[1]);
            destComponent = player.getShipBoard().getComponent(destCoordinates[0], destCoordinates[1]);

            if (sourceComponent.getComponentName().equals("Storage") && destComponent.getComponentName().equals("Storage")) {

                sourceGoods = ((Storage) sourceComponent).getGoods();

                if (!((Storage) sourceComponent).isEmpty() && !((Storage) destComponent).isFull()) {

                    while (errorFlag) {

                        int[] movingGoods;

                        movingGoods = askForGoods(player, "move", 0, 3, gameInformation);

                        if (checkGoodsAvailability(movingGoods, sourceGoods, 0, 3)) {

                           /*
                           two possible scenarios:

                           first: I'm moving red goods, therefore the destination component must be red and have enough slots.
                           Second: I'm not moving red goods, therefore the destination component can be either red or not red, with enough slots.
                            */

                            if ((movingGoods[0] + movingGoods[1] + movingGoods[2] + movingGoods[3] <= ((Storage) destComponent).getAvailableRedSlots()) || (movingGoods[0] == 0 && movingGoods[1] + movingGoods[2] + movingGoods[3] <= ((Storage) destComponent).getAvailableBlueSlots())) {

                                ((Storage) sourceComponent).removeGoods(movingGoods);
                                ((Storage) destComponent).addGoods(movingGoods);

                                if (((Storage) sourceComponent).isRed() && !((Storage) destComponent).isRed()) {

                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, movingGoods[0] + movingGoods[1] + movingGoods[2] + movingGoods[3]);
                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, -(movingGoods[0] + movingGoods[1] + movingGoods[2] + movingGoods[3]));

                                } else if (!((Storage) sourceComponent).isRed() && ((Storage) destComponent).isRed()) {

                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -(movingGoods[0] + movingGoods[1] + movingGoods[2] + movingGoods[3]));
                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, movingGoods[0] + movingGoods[1] + movingGoods[2] + movingGoods[3]);

                                }

                                errorFlag = false;

                            } else {

                                message = "The destination component doesn't have enough space for the goods to move";
                                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                            }
                        } else {

                            message = "The moving goods you entered are too many";
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        }
                    }

                } else {

                    message = "The source storage you entered is empty or the destination storage you entered is full, " +
                            "do you still want to rearrange some goods ?";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        rearrangementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                    continue;
                }
            } else {

                message = "The components you entered are not both storages! Do you still want to rearrange some goods ?";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    rearrangementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }

                continue;
            }

            message = "Are there some other goods you want to rearrange ?";
            errorFlag = true;
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                rearrangementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

        }
    }

    private void redGoodsPlacementPhase(Player player, int[] goods, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        boolean placementPhaseFlag = false, errorFlag;
        int[] coordinates = new int[2];
        Component component;

        if (goods[0] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() > 0) {
            //red goods can be added

            message = "Do you want to add to your ship red goods ? ";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            //check if you can do this

            try {
                placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }

                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);
                int redGoodsToAdd[];
                errorFlag = true;

                if (component.getComponentName().equals("Storage")) {
                    if (((Storage) component).isRed() && !((Storage) component).isFull()) {

                        while (errorFlag) {

                            redGoodsToAdd = askForGoods(player, "add", 0, 0, gameInformation);


                            if (checkGoodsAvailability(redGoodsToAdd, goods, 0, 0) && redGoodsToAdd[0] <= ((Storage) component).getAvailableRedSlots()) {

                                errorFlag = false;
                                goods[0] -= redGoodsToAdd[0];

                                try {

                                    gameInformation.getFlightBoard().removeGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    ((Storage) component).addGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -redGoodsToAdd[0]);

                                } catch (IllegalArgumentException e) {

                                    goods[0] += redGoodsToAdd[0];
                                    message = "The are not enough red goods in the flight board, check it to see the available ones";
                                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                                    break;

                                }

                            } else {
                                message = "The value you entered is incorrect";
                                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);
                            }

                        }
                    } else {

                        message = "The storage you entered is either full or is not red, do you still want to add red goods to your ship ? ";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        try {
                            placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                        } catch (PlayerDisconnectedException e) {
                            gameInformation.disconnectPlayer(player);
                            message = e.getMessage();
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                        }

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add red goods to your ship? ";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                    continue;
                }

                if (goods[0] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 0) {
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other red goods to your ship ?";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                }

            }

        }
    }

    private void nonRedGoodsPlacementPhasePhase(Player player, int[] goods, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        boolean placementPhaseFlag = false, errorFlag;
        Component component;
        int[] coordinates = new int[2];

        if (goods[1] + goods[2] + goods[3] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() + player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() > 0) {
            //other goods can be added
            message = "Do you want to add to your ship goods that are not red ?";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    coordinates = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerCoordinates(player);
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }

                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);
                errorFlag = true;
                boolean redFlag = false;

                if (component.getComponentName().equals("Storage")) {
                    if (!((Storage) component).isFull()) {

                        if (((Storage) component).isRed()) {
                            redFlag = true;
                        }

                        while (errorFlag) {

                            int[] goodsToAdd;

                            goodsToAdd = askForGoods(player, "add", 1, 3, gameInformation);

                            if (checkGoodsAvailability(goodsToAdd, goods, 1, 3) && (goodsToAdd[1] + goodsToAdd[2] + goodsToAdd[3]) <= ((Storage) component).getAvailableRedSlots() + ((Storage) component).getAvailableBlueSlots()) {

                                errorFlag = false;
                                goods[1] -= goodsToAdd[1];
                                goods[2] -= goodsToAdd[2];
                                goods[3] -= goodsToAdd[3];
                                try {

                                    gameInformation.getFlightBoard().removeGoods(goodsToAdd);
                                    player.getShipBoard().getShipBoardAttributes().updateGoods(goodsToAdd);
                                    ((Storage) component).addGoods(goodsToAdd);

                                    if (redFlag) {

                                        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -(goodsToAdd[1] + goodsToAdd[2] + goodsToAdd[3]));

                                    } else {

                                        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, -(goodsToAdd[1] + goodsToAdd[2] + goodsToAdd[3]));

                                    }
                                } catch (IllegalArgumentException e) {

                                    goods[1] += goodsToAdd[1];
                                    goods[2] += goodsToAdd[2];
                                    goods[3] += goodsToAdd[3];

                                    message = "There are not enough goods in the flight board, check it to see the available ones";
                                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                                    break;
                                }
                            } else {
                                message = "The goods you entered are incorrect";
                                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);
                            }

                        }
                    } else {

                        message = "The storage component you entered is already full! Do you still want to add non red goods to your ship? ";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                        try {
                            placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                        } catch (PlayerDisconnectedException e) {
                            gameInformation.disconnectPlayer(player);
                            message = e.getMessage();
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                        }

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add non red goods to your ship? ";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                    continue;
                }

                if (goods[1] + goods[2] + goods[3] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() + player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0) {
                    //goods to add are all placed or no space left on ship
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other non red goods to your ship ?";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                    try {
                        placementPhaseFlag = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player);
                    } catch (PlayerDisconnectedException e) {
                        gameInformation.disconnectPlayer(player);
                        message = e.getMessage();
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                    }

                }

            }

        }

    }

    private int[] askForGoods(Player player, String messageType, int start, int end, GameInformation gameInformation) {

        int[] goods = {0, 0, 0, 0};
        String message;
        DataContainer dataContainer;
        String[] colors = {"red", "yellow", "green", "blue"};

        for (int i = start; i <= end; i++) {

            message = "Enter number of " + colors[i] + " goods to " + messageType;
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

            try {
                goods[i] = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerInt(player);
            } catch (PlayerDisconnectedException e) {
                gameInformation.disconnectPlayer(player);
                message = e.getMessage();
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
            }

        }

        return goods;
    }

    private boolean checkGoodsAvailability(int[] firstGoods, int[] secondGoods, int start, int end) {

        for (int i = start; i <= end; i++) {

            if (firstGoods[i] > secondGoods[i]) {
                return false;
            }

        }

        return true;

    }

}
