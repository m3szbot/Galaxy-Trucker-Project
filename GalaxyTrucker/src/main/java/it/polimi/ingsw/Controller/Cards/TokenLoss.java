package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

/**
 * Interface that define a default method which handles a player being
 * inflicted a loss in terms of crew members or goods.
 *
 * @author carlo
 */

//Player can lose the game if the inhabitants are 0.


public interface TokenLoss {

    /**
     * @param player      target player
     * @param lossType    can be either inhabitants or goods
     * @param quantity    quantity of the loss
     * @param flightBoard flightboard of the actual game
     * @author Carlo
     */

    default void inflictLoss(Player player, ElementType lossType, int quantity, FlightBoard flightBoard, int gameCode) {


        if (lossType == ElementType.CrewMember) {

            removeCrewMembers(player, quantity, gameCode);

        } else {
            //removing goods
            int goodsOnShip[] = player.getShipBoard().getShipBoardAttributes().getGoods();

            if (goodsOnShip[0] + goodsOnShip[1] + goodsOnShip[2] + goodsOnShip[3] > 0) {
                //there are some goods that can be removed

                quantity = removeGoods(player, flightBoard, quantity, goodsOnShip, gameCode);

            }

            if (quantity > 0) {
                //need to remove batteries

                removeBatteries(player, quantity, gameCode);
            }
        }

    }

    private void removeCrewMembers(Player player, int quantity, int gameCode) {

        String message;
        DataContainer dataContainer;
        Component component;
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
                    " Enter coordinates of cabin: ";
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            coordinates = ClientMessenger.getGameMessenger(gameCode).getPlayerCoordinates(player);

            component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

            if (component.getComponentName().equals("Cabin")) {

                if (((Cabin) component).getCrewMembers() == 1) {
                    //cabin with aliens have one 1 inhabitant, i.e, the alien.

                    if (((Cabin) component).getCrewType() == CrewType.Brown) {

                        player.getShipBoard().getShipBoardAttributes().updateAlien(CrewType.Brown, true);

                    } else if (((Cabin) component).getCrewType() == CrewType.Purple) {

                        player.getShipBoard().getShipBoardAttributes().updateAlien(CrewType.Purple, true);

                    }

                    //immediately remove crew member
                    numberOfCrewToRemove--;
                    ((Cabin) component).removeInhabitant();
                    player.getShipBoard().getShipBoardAttributes().updateCrewMembers(-1);

                } else if (((Cabin) component).getCrewMembers() == 2) {
                    //let the player choose if he wants to remove 2 or 1 crew member (or 0)

                    while (true) {

                        message = "Enter number of inhabitants to remove: ";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        int numberOfRemovedCrew = ClientMessenger.getGameMessenger(gameCode).getPlayerInt(player);

                        if (numberOfRemovedCrew <= 2 && numberOfRemovedCrew <= numberOfCrewToRemove && numberOfRemovedCrew > 0) {

                            numberOfCrewToRemove -= numberOfRemovedCrew;

                            for (int i = 0; i < numberOfRemovedCrew; i++) {
                                ((Cabin) component).removeInhabitant();
                            }
                            player.getShipBoard().getShipBoardAttributes().updateCrewMembers(-numberOfRemovedCrew);
                            break;
                        }

                        message = "The number of inhabitants you entered is incorrect";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    }

                } else {

                    message = "The cabin you selected is empty";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                }
            } else {

                message = "The component you entered is not a cabin";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

            }
        }


    }

    private int removeGoods(Player player, FlightBoard flightBoard, int quantity, int[] goodsOnShip, int gameCode) {

        String message;
        DataContainer dataContainer;
        String goodColor;
        int numberOfGoodsToRemove;
        Component component;
        int[] coordinates = new int[2];

        for (int i = 0; i < 4 && quantity > 0; i++) {

            if (goodsOnShip[i] > 0) {

                goodColor = colorSolver(i);

                if (goodsOnShip[i] >= quantity) {


                    numberOfGoodsToRemove = quantity;
                    quantity = 0;

                } else {

                    quantity -= goodsOnShip[i];
                    numberOfGoodsToRemove = goodsOnShip[i];
                }


                while (numberOfGoodsToRemove > 0) {

                    int[] availableGoods;
                    int numberOfRemovedGoods;
                    int[] goodsRemoved = {0, 0, 0, 0};

                    message = "You must remove " + numberOfGoodsToRemove + " " + goodColor + " goods, " +
                            "enter coordinate of storage component: ";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                    coordinates = ClientMessenger.getGameMessenger(gameCode).getPlayerCoordinates(player);
                    component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                    if (component.getComponentName().equals("Storage")) {

                        availableGoods = ((Storage) component).getGoods();

                        if (availableGoods[i] > 0) {

                            while (true) {

                                message = "Enter number of " + goodColor + " goods that you want to remove: ";
                                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                                numberOfRemovedGoods = ClientMessenger.getGameMessenger(gameCode).getPlayerInt(player);
                                goodsRemoved[i] = numberOfRemovedGoods;

                                if (numberOfRemovedGoods <= availableGoods[i] && numberOfRemovedGoods <= numberOfGoodsToRemove && numberOfRemovedGoods > 0) {

                                    numberOfGoodsToRemove -= numberOfRemovedGoods;
                                    flightBoard.addGoods(goodsRemoved);
                                    ((Storage) component).removeGoods(goodsRemoved);
                                    player.getShipBoard().getShipBoardAttributes().updateGoods(new int[]{-goodsRemoved[0], -goodsRemoved[1], -goodsRemoved[2], -goodsRemoved[3]});

                                    if (((Storage) component).isRed()) {
                                        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, numberOfRemovedGoods);
                                    } else {
                                        player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, numberOfRemovedGoods);
                                    }
                                    break;

                                }

                                message = "The number of goods you entered is invalid";
                                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                                dataContainer.setMessage(message);
                                dataContainer.setCommand("printMessage");
                                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                            }

                        } else {
                            message = "The storage component you selected does not contain " + goodColor + " goods";
                            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                        }

                    } else {
                        message = "The component you selected is not a storage";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                    }

                }

            }

        }

        return quantity;
    }

    private void removeBatteries(Player player, int quantity, int gameCode) {

        int batteriesAvailable = player.getShipBoard().getShipBoardAttributes().getBatteryPower();
        String message;
        DataContainer dataContainer;
        int numberOfBatteriesToRemove;
        Component component;
        int[] coordinates = new int[2];

        if (batteriesAvailable > 0) {
            //there are batteries that can be removed

            if (batteriesAvailable >= quantity) {
                numberOfBatteriesToRemove = quantity;
            } else {
                numberOfBatteriesToRemove = batteriesAvailable;
            }

            player.getShipBoard().getShipBoardAttributes().updateBatteryPower(-numberOfBatteriesToRemove);

            while (numberOfBatteriesToRemove > 0) {

                int numberOfRemovedBatteries;
                message = "Enter coordinate of the battery station: ";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                coordinates = ClientMessenger.getGameMessenger(gameCode).getPlayerCoordinates(player);
                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                if (component.getComponentName().equals("Battery")) {
                    if (((Battery) component).getBatteryPower() > 0) {

                        while (true) {
                            message = "Enter number of batteries you want to remove: ";
                            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                            numberOfRemovedBatteries = ClientMessenger.getGameMessenger(gameCode).getPlayerInt(player);

                            if (numberOfRemovedBatteries <= ((Battery) component).getBatteryPower() && numberOfRemovedBatteries <= numberOfBatteriesToRemove && numberOfRemovedBatteries > 0) {

                                numberOfBatteriesToRemove -= numberOfRemovedBatteries;

                                for (int i = 0; i < numberOfRemovedBatteries; i++) {
                                    ((Battery) component).removeBattery();
                                }
                                break;
                            }

                            message = "The number of batteries you entered is invalid";
                            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                        }

                    } else {
                        message = "The battery station you entered is empty";
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                    }
                } else {
                    message = "The component you entered is not a battery";
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                }
            }

        }
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
}
