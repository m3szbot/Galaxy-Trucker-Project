package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

/**
 * Interface that define a method which handles a player receiving
 * a certain ammount of goods
 *
 * @author carlo
 */

public interface GoodsGain {

    /**
     * @param player     indicates the target player
     * @param goods      goods to give to the target player
     * @param flightView class to comunicate with the player
     * @author Carlo
     */

    default void giveGoods(Player player, int[] goods, FlightBoard flightBoard, FlightView flightView) {

        int i, j;
        int[] coordinates;
        String message;
        Component component;
        boolean discardingPhaseFlag, rearrangementPhaseFlag, placementPhaseFlag, errorFlag = true;

        //Discarding phase

        message = "Are there some goods that you want to discard ?";

        discardingPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

        while (discardingPhaseFlag) {
            //player decide to discard some goods

            message = "Enter coordinate of the storage component: ";
            coordinates = flightView.askPlayerCoordinates(player, message);
            component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

            if (component.getComponentName().equals("Storage")) {

                int availableGoods[] = ((Storage) component).getGoods();

                if (!((Storage) component).isEmpty()) {

                    while (errorFlag) {

                        int goodsToRemove[] = {0, 0, 0, 0};

                        message = "Do you want to remove red goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of red goods to remove: ";
                            goodsToRemove[0] = flightView.askPlayerValue(player, message);

                        }

                        message = "Do you want to remove yellow goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of yellow goods to remove: ";
                            goodsToRemove[1] = flightView.askPlayerValue(player, message);

                        }

                        message = "Do you want to remove green goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of green goods to remove: ";
                            goodsToRemove[2] = flightView.askPlayerValue(player, message);

                        }

                        message = "Do you want to remove blue goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of blue goods to remove: ";
                            goodsToRemove[3] = flightView.askPlayerValue(player, message);

                        }

                        if (goodsToRemove[0] <= availableGoods[0] && goodsToRemove[1] <= availableGoods[1] && goodsToRemove[2] <= availableGoods[2] && goodsToRemove[3] <= availableGoods[3]) {
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

                        }
                        //value entered are incorrect

                    }
                } else {

                    message = "The storage component you entered is empty, do you still want to discard some goods ?";
                    discardingPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                    continue;

                }
            } else {

                message = "The component you entered is not a storage component! Do you still" +
                        " want to discard some goods ?";
                discardingPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                continue;
            }

            message = "Are there some other goods that you want to discard ?";
            errorFlag = true;
            discardingPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

        }

        //Rearrangement phase
        message = "Are there some goods that you want to rearrange ?";

        rearrangementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

        while (rearrangementPhaseFlag) {
            //player decide that he wants to rearrange some goods

            int[] sourceCoordinates, destCoordinates;
            Component sourceComponent, destComponent;
            int[] sourceGoods;


            message = "Enter coordinate of the source storage component: ";
            sourceCoordinates = flightView.askPlayerCoordinates(player, message);


            message = "Enter coordinate of the destination storage component: ";
            destCoordinates = flightView.askPlayerCoordinates(player, message);

            sourceComponent = player.getShipBoard().getComponent(sourceCoordinates[0], sourceCoordinates[1]);
            destComponent = player.getShipBoard().getComponent(destCoordinates[0], destCoordinates[1]);

            if (sourceComponent.getComponentName().equals("Storage") && destComponent.getComponentName().equals("Storage")) {

                sourceGoods = ((Storage) sourceComponent).getGoods();

                if (!((Storage) sourceComponent).isEmpty() && !((Storage) destComponent).isFull()) {

                    while (errorFlag) {

                        int[] movingGoods = {0, 0, 0, 0};

                        message = "Do you want to move red goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of red goods to move: ";
                            movingGoods[0] = flightView.askPlayerValue(player, message);

                        }

                        message = "Do you want to move yellow goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of yellow goods to move: ";
                            movingGoods[1] = flightView.askPlayerValue(player, message);

                        }

                        message = "Do you want to move green goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of green goods to move: ";
                            movingGoods[2] = flightView.askPlayerValue(player, message);
                        }

                        message = "Do you want to move blue goods ?";

                        if (flightView.askPlayerGenericQuestion(player, message)) {

                            message = "Enter number of blue goods to move: ";
                            movingGoods[3] = flightView.askPlayerValue(player, message);
                        }

                        if (movingGoods[0] <= sourceGoods[0] && movingGoods[1] <= sourceGoods[1] && movingGoods[2] <= sourceGoods[2] && movingGoods[3] <= sourceGoods[3]) {

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

                            }


                        }


                    }

                } else {

                    message = "The source storage you entered is empty or the destination storage you entered is full, " +
                            "do you still want to rearrange some goods ?";
                    rearrangementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                    continue;
                }
            } else {

                message = "The components you entered are not both storages! Do you still want to rearrange some goods ?";
                rearrangementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                continue;
            }

            message = "Are there some other goods you want to rearrange ?";
            errorFlag = true;
            rearrangementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

        }

        //Goods placement phase

        if (goods[0] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() > 0) {
            //red goods can be added

            message = "Do you want to add to your ship red goods ? ";

            //check if you can do this
            placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                coordinates = flightView.askPlayerCoordinates(player, message);
                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);
                int redGoodsToAdd;
                errorFlag = true;

                if (component.getComponentName().equals("Storage")) {
                    if (((Storage) component).isRed() && !((Storage) component).isFull()) {

                        while (errorFlag) {

                            message = "Enter red goods to add to the selected storage component: ";
                            redGoodsToAdd = flightView.askPlayerValue(player, message);

                            if (redGoodsToAdd <= goods[0] && redGoodsToAdd <= ((Storage) component).getAvailableRedSlots()) {

                                errorFlag = false;
                                goods[0] -= redGoodsToAdd;

                                try {

                                    flightBoard.removeGoods(new int[]{redGoodsToAdd, 0, 0, 0});
                                    ((Storage) component).addGoods(new int[]{redGoodsToAdd, 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{redGoodsToAdd, 0, 0, 0});
                                    player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, -redGoodsToAdd);

                                } catch (IllegalArgumentException e) {
                                    //Error management will be developed later, for now this is enough

                                    System.out.println(e.getMessage());

                                }

                            }

                        }
                    } else {

                        message = "The storage you entered is either full or is not red, do you still want to add red goods to your ship ? ";
                        placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add red goods to your ship? ";
                    placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                    continue;
                }

                if (goods[0] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() == 0) {
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other red goods to your ship ?";
                    placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                }

            }

        }

        if (goods[1] + goods[2] + goods[3] > 0 && player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() > 0) {
            //other goods can be added
            message = "Do you want to add to your ship goods that are not red ?";
            placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

            while (placementPhaseFlag) {

                message = "Enter coordinates of storage component: ";
                coordinates = flightView.askPlayerCoordinates(player, message);
                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);
                errorFlag = true;
                boolean redFlag = false;

                if (component.getComponentName().equals("Storage")) {
                    if (!((Storage) component).isFull()) {

                        if (((Storage) component).isRed()) {
                            redFlag = true;
                        }

                        while (errorFlag) {

                            int[] goodsToAdd = {0, 0, 0, 0};
                            int totalGoodsAdded = 0;

                            if (goods[1] > 0) {

                                message = "Enter number of yellow goods to add: ";
                                goodsToAdd[1] = flightView.askPlayerValue(player, message);
                                totalGoodsAdded += goodsToAdd[1];

                            }
                            if (goods[2] > 0) {

                                message = "Enter number of green goods to add: ";
                                goodsToAdd[2] = flightView.askPlayerValue(player, message);
                                totalGoodsAdded += goodsToAdd[2];

                            }
                            if (goods[3] > 0) {

                                message = "Enter number of blue goods to add: ";
                                goodsToAdd[3] = flightView.askPlayerValue(player, message);
                                totalGoodsAdded += goodsToAdd[3];

                            }

                            if (goodsToAdd[1] <= goods[1] && goodsToAdd[2] <= goods[2] && goodsToAdd[3] <= goods[3] && totalGoodsAdded <= ((Storage) component).getAvailableRedSlots() + ((Storage) component).getAvailableBlueSlots()) {

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
                                    System.out.println(e.getMessage());
                                }
                            }

                        }
                    } else {

                        message = "The storage component you entered is already full! Do you still want to add non red goods to your ship? ";
                        placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                        continue;
                    }

                } else {

                    message = "The selected component is not a storage! Do you still want to add non red goods to your ship? ";
                    placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                    continue;
                }

                if (goods[1] + goods[2] + goods[3] == 0 || player.getShipBoard().getShipBoardAttributes().getAvailableRedSlots() + player.getShipBoard().getShipBoardAttributes().getAvailableBlueSlots() == 0) {
                    //goods to add are all placed or no space left on ship
                    placementPhaseFlag = false;
                } else {

                    message = "Do you want to add other non red goods to your ship ?";
                    placementPhaseFlag = flightView.askPlayerGenericQuestion(player, message);

                }

            }

        }

    }
}
