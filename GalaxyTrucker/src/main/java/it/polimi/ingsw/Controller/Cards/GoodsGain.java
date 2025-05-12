package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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

    default void giveGoods(Player player, int[] goods, FlightBoard flightBoard, int gameCode) {

        discardingPhase(player, flightBoard, gameCode);
        rearrangementPhase(player, gameCode);
        redGoodsPlacementPhase(player, goods, flightBoard, gameCode);
        nonRedGoodsPlacementPhasePhase(player, goods, flightBoard, gameCode);

    }

    private void discardingPhase(Player player, FlightBoard flightBoard, int gameCode) {

        String message;
        DataContainer dataContainer;
        boolean discardingPhaseFlag, errorFlag = true;
        int[] coordinates = new int[2];
        Component component;

        message = "Are there some goods that you want to discard ?";
        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

        if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {

            discardingPhaseFlag = true;

        } else {

            discardingPhaseFlag = false;

        }


        while (discardingPhaseFlag) {
            //player decide to discard some goods

            message = "Enter coordinates of the storage component: ";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            coordinates[0] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
            coordinates[1] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
            component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

            if (component.getComponentName().equals("Storage")) {

                int availableGoods[] = ((Storage) component).getGoods();

                if (!((Storage) component).isEmpty()) {

                    while (errorFlag) {

                        int goodsToRemove[];

                        goodsToRemove = askForGoods(player, "remove", 0, 3, gameCode);

                        if (checkGoodsAvailability(goodsToRemove, availableGoods, 0, 3, gameCode)) {
                            //value entered are correct

                            ((Storage) component).removeGoods(goodsToRemove);
                            flightBoard.addGoods(goodsToRemove);
                            player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{-goodsToRemove[0], -goodsToRemove[1], -goodsToRemove[2], -goodsToRemove[3]});

                            if (((Storage) component).isRed()) {

                                player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, goodsToRemove[0] + goodsToRemove[1] + goodsToRemove[2] + goodsToRemove[3]);

                            } else {

                                player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, goodsToRemove[1] + goodsToRemove[2] + goodsToRemove[3]);

                            }

                            errorFlag = false;

                        } else {

                            message = "The goods you entered are incorrect";
                            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        }

                        //value entered are incorrect, add notification to player

                    }
                } else {

                    message = "The storage component you entered is empty, do you still want to discard some goods ?";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        discardingPhaseFlag = true;
                    } else {
                        discardingPhaseFlag = false;
                    }

                    continue;

                }
            } else {

                message = "The component you entered is not a storage component! Do you still" +
                        " want to discard some goods ?";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                    discardingPhaseFlag = true;
                } else {
                    discardingPhaseFlag = false;
                }

                continue;
            }

            message = "Are there some other goods that you want to discard ?";
            errorFlag = true;
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                discardingPhaseFlag = true;
            } else {
                discardingPhaseFlag = false;
            }
        }

    }

    private void rearrangementPhase(Player player, int gameCode) {

        String message;
        DataContainer dataContainer;
        boolean rearrangementPhaseFlag, errorFlag = true;

        message = "Are there some goods that you want to rearrange ?";
        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

        if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
            rearrangementPhaseFlag = true;
        } else {
            rearrangementPhaseFlag = false;
        }

        while (rearrangementPhaseFlag) {
            //player decide that he wants to rearrange some goods

            int[] sourceCoordinates = new int[2], destCoordinates = new int[2];
            Component sourceComponent, destComponent;
            int[] sourceGoods;

            message = "Enter coordinate of the source storage component: ";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            sourceCoordinates[0] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
            sourceCoordinates[1] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));

            message = "Enter coordinate of the destination storage component: ";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            destCoordinates[0] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
            destCoordinates[1] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));

            sourceComponent = player.getShipBoard().getComponent(sourceCoordinates[0], sourceCoordinates[1]);
            destComponent = player.getShipBoard().getComponent(destCoordinates[0], destCoordinates[1]);

            if (sourceComponent.getComponentName().equals("Storage") && destComponent.getComponentName().equals("Storage")) {

                sourceGoods = ((Storage) sourceComponent).getGoods();

                if (!((Storage) sourceComponent).isEmpty() && !((Storage) destComponent).isFull()) {

                    while (errorFlag) {

                        int[] movingGoods;

                        movingGoods = askForGoods(player, "move", 0, 3, gameCode);

                        if (checkGoodsAvailability(movingGoods, sourceGoods, 0, 3, gameCode)) {

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
                                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                            }
                        } else {

                            message = "The moving goods you entered are too many";
                            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        }
                    }

                } else {

                    message = "The source storage you entered is empty or the destination storage you entered is full, " +
                            "do you still want to rearrange some goods ?";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        rearrangementPhaseFlag = true;
                    } else {
                        rearrangementPhaseFlag = false;
                    }

                    continue;
                }
            } else {

                message = "The components you entered are not both storages! Do you still want to rearrange some goods ?";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                    rearrangementPhaseFlag = true;
                } else {
                    rearrangementPhaseFlag = false;
                }

                continue;
            }

            message = "Are there some other goods you want to rearrange ?";
            errorFlag = true;
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                rearrangementPhaseFlag = true;
            } else {
                rearrangementPhaseFlag = false;
            }

        }
    }

    private void redGoodsPlacementPhase(Player player, int[] goods, FlightBoard flightBoard, int gameCode) {

        String message;
        DataContainer dataContainer;
        boolean placementPhaseFlag, errorFlag;
        int[] coordinates = new int[2];
        Component component;

        if (goods[0] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() > 0) {
            //red goods can be added

            message = "Do you want to add to your ship red goods ? ";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            //check if you can do this

            if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                placementPhaseFlag = true;
            } else {
                placementPhaseFlag = false;
            }

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                coordinates[0] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
                coordinates[1] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));

                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);
                int redGoodsToAdd[];
                errorFlag = true;

                if (component.getComponentName().equals("Storage")) {
                    if (((Storage) component).isRed() && !((Storage) component).isFull()) {

                        while (errorFlag) {

                            redGoodsToAdd = askForGoods(player, "add", 0, 0, gameCode);


                            if (checkGoodsAvailability(redGoodsToAdd, goods, 0, 0, gameCode) && redGoodsToAdd[0] <= ((Storage) component).getAvailableRedSlots()) {

                                errorFlag = false;
                                goods[0] -= redGoodsToAdd[0];

                                try {

                                    flightBoard.removeGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    ((Storage) component).addGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{redGoodsToAdd[0], 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -redGoodsToAdd[0]);

                                } catch (IllegalArgumentException e) {

                                    goods[0] += redGoodsToAdd[0];
                                    message = "The are not enough red goods in the flight board, check it to see the available ones";
                                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                    dataContainer.setMessage(message);
                                    dataContainer.setCommand("printMessage");
                                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                                    break;

                                }

                            } else {
                                message = "The value you entered is incorrect";
                                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                            }

                        }
                    } else {

                        message = "The storage you entered is either full or is not red, do you still want to add red goods to your ship ? ";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                            placementPhaseFlag = true;
                        } else {
                            placementPhaseFlag = false;
                        }

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add red goods to your ship? ";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        placementPhaseFlag = true;
                    } else {
                        placementPhaseFlag = false;
                    }

                    continue;
                }

                if (goods[0] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 0) {
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other red goods to your ship ?";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        placementPhaseFlag = true;
                    } else {
                        placementPhaseFlag = false;
                    }

                }

            }

        }
    }

    private void nonRedGoodsPlacementPhasePhase(Player player, int[] goods, FlightBoard flightBoard, int gameCode) {

        String message;
        DataContainer dataContainer;
        boolean placementPhaseFlag, errorFlag;
        Component component;
        int[] coordinates = new int[2];

        if (goods[1] + goods[2] + goods[3] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() + player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() > 0) {
            //other goods can be added
            message = "Do you want to add to your ship goods that are not red ?";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                placementPhaseFlag = true;
            } else {
                placementPhaseFlag = false;
            }

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                coordinates[0] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));
                coordinates[1] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));

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

                            goodsToAdd = askForGoods(player, "add", 1, 3, gameCode);

                            if (checkGoodsAvailability(goodsToAdd, goods, 1, 3, gameCode) && (goodsToAdd[1] + goodsToAdd[2] + goodsToAdd[3]) <= ((Storage) component).getAvailableRedSlots() + ((Storage) component).getAvailableBlueSlots()) {

                                errorFlag = false;
                                goods[1] -= goodsToAdd[1];
                                goods[2] -= goodsToAdd[2];
                                goods[3] -= goodsToAdd[3];
                                try {

                                    flightBoard.removeGoods(goodsToAdd);
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
                                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                    dataContainer.setMessage(message);
                                    dataContainer.setCommand("printMessage");
                                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                                    break;
                                }
                            } else {
                                message = "The goods you entered are incorrect";
                                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                            }

                        }
                    } else {

                        message = "The storage component you entered is already full! Do you still want to add non red goods to your ship? ";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                            placementPhaseFlag = true;
                        } else {
                            placementPhaseFlag = false;
                        }

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add non red goods to your ship? ";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        placementPhaseFlag = true;
                    } else {
                        placementPhaseFlag = false;
                    }

                    continue;
                }

                if (goods[1] + goods[2] + goods[3] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() + player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0) {
                    //goods to add are all placed or no space left on ship
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other non red goods to your ship ?";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                        placementPhaseFlag = true;
                    } else {
                        placementPhaseFlag = false;
                    }

                }

            }

        }

    }

    private int[] askForGoods(Player player, String messageType, int start, int end, int gameCode) {

        int[] goods = {0, 0, 0, 0};
        String message;
        DataContainer dataContainer;
        String[] colors = {"red", "yellow", "green", "blue"};

        for (int i = start; i <= end; i++) {

            message = "Enter number of " + colors[i] + " goods to " + messageType;
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            goods[i] = Integer.parseInt(ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player));

        }

        return goods;
    }

    private boolean checkGoodsAvailability(int[] firstGoods, int[] secondGoods, int start, int end, int gameCode) {

        for (int i = start; i <= end; i++) {

            if (firstGoods[i] > secondGoods[i]) {
                return false;
            }

        }

        return true;

    }

}
