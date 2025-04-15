package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Components.Battery;
import it.polimi.ingsw.Components.Cabin;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Components.Storage;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

/**
 * Interface that define a default method which handles a player being
 * inflicted a loss in terms of crew members or goods.
 *
 * @author carlo
 */

//Player can lose the game if the inhabitants are 0.


public interface TokenLoss {

    /**
     *
     * @param player target player
     * @param lossType can be either inhabitants or goods
     * @param quantity quantity of the loss
     * @param flightView class to comunicate with the player
     *
     * @author Carlo
     */

    default void inflictLoss(Player player, ElementType lossType, int quantity, FlightBoard flightBoard, FlightView flightView) {

        int coordinates[];
        String message;
        Component component;

        if(lossType == ElementType.CrewMember){
            //removing crew members or aliens
            int availableCrew = player.getShipBoard().getShipBoardAttributes().getCrewMembers();
            int numberOfCrewToRemove;

            if(quantity >= availableCrew){
                numberOfCrewToRemove = availableCrew;
            }
            else{

                numberOfCrewToRemove = quantity;
            }

            while(numberOfCrewToRemove > 0){

                message = "You must remove " + numberOfCrewToRemove + "inhabitants." +
                        "Enter coordinates of cabin: ";
                coordinates = flightView.askPlayerCoordinates(player, message);
                component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                if(component.getComponentName().equals("Cabin")){
                    if(((Cabin)component).getCrewMembers() == 1){
                       //immediately remove crew member
                        numberOfCrewToRemove--;
                        ((Cabin)component).removeInhabitant();
                        player.getShipBoard().getShipBoardAttributes().updateCrewMembers(-1);

                    }
                    else if(((Cabin)component).getCrewMembers() == 2){
                        //let the player choose if he wants to remove 2 or 1 crew member (or 0)

                        message = "Enter number of inhabitants to remove: ";
                        int numberOfRemovedCrew = flightView.askPlayerValue(player, message);

                        if(numberOfRemovedCrew <= 2 && numberOfRemovedCrew <= numberOfCrewToRemove){

                            numberOfCrewToRemove -= numberOfRemovedCrew;

                            for(int i = 0; i < numberOfRemovedCrew; i++){
                                ((Cabin)component).removeInhabitant();
                            }
                            player.getShipBoard().getShipBoardAttributes().updateCrewMembers(-numberOfRemovedCrew);
                        }

                    }
                }

            }

        }
        else{
           //removing goods
            int goodsOnShip[] = player.getShipBoard().getShipBoardAttributes().getGoods();
            int numberOfGoodsToRemove = 0, numberOfBatteriesToRemove = 0;
            String goodColor;

            if(goodsOnShip[0] + goodsOnShip[1] + goodsOnShip[2] + goodsOnShip[3] > 0){
                //there are some goods that can be removed

                for(int i = 0; i < 4 && quantity > 0; i++){

                    if(goodsOnShip[i] > 0){

                        if(i == 0){

                            goodColor = "red";

                        }
                        else if(i == 1){

                            goodColor = "yellow";

                        }
                        else if(i == 2){

                            goodColor = "green";

                        }
                        else{

                            goodColor = "blue";

                        }

                        if(goodsOnShip[i] >= quantity){

                            quantity = 0;
                            numberOfGoodsToRemove = quantity;
                        }
                        else{

                            quantity -= goodsOnShip[i];
                            numberOfGoodsToRemove = goodsOnShip[i];
                        }



                        if(i == 0){
                            player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(1, numberOfGoodsToRemove);
                        }
                        else{
                            player.getShipBoard().getShipBoardAttributes().updateAvailableSlots(0, numberOfGoodsToRemove);
                        }

                        while(numberOfGoodsToRemove > 0) {
                            //if only one error occur, the loop is repeated, can be improved.

                            int[] availableGoods;
                            int numberOfRemovedGoods;
                            int[] goodsRemoved = {0, 0, 0, 0};

                            message = "You must remove " + numberOfGoodsToRemove + goodColor + " goods, " +
                                    "enter coordinate of storage component: ";

                            coordinates = flightView.askPlayerCoordinates(player, message);
                            component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                            if(component.getComponentName().equals("Storage")){

                                availableGoods = ((Storage)component).getGoods();

                                if(availableGoods[i] > 0){

                                   message = "Enter number of " + goodColor + " that you want to remove: ";
                                   numberOfRemovedGoods = flightView.askPlayerValue(player, message);
                                   goodsRemoved[i] = numberOfRemovedGoods;

                                   if(numberOfRemovedGoods <= availableGoods[i] && numberOfRemovedGoods <= numberOfGoodsToRemove){

                                       numberOfGoodsToRemove -= numberOfRemovedGoods;
                                       flightBoard.addGoods(goodsRemoved);
                                       ((Storage)component).removeGoods(goodsRemoved);

                                   }

                                }

                            }

                        }

                    }

                }

            }

            if(quantity > 0){
                //need to remove batteries

                int batteriesAvailable = player.getShipBoard().getShipBoardAttributes().getBatteryPower();

                if(batteriesAvailable > 0){
                    //there are batteries that can be removed

                    if(batteriesAvailable >= quantity){
                        numberOfBatteriesToRemove = quantity;
                    }
                    else{
                        numberOfBatteriesToRemove = batteriesAvailable;
                    }

                    player.getShipBoard().getShipBoardAttributes().updateBatteryPower(numberOfBatteriesToRemove);

                    while(numberOfBatteriesToRemove > 0){
                        //the loop is repeated if only one error occur

                        int numberOfRemovedBatteries = 0;
                        message = "Enter coordinate of the battery station: ";
                        coordinates = flightView.askPlayerCoordinates(player, message);
                        component = player.getShipBoard().getComponent(coordinates[0], coordinates[1]);

                        if(component.getComponentName().equals("Battery")){
                            if(((Battery)component).getBatteryPower() > 0){

                               message = "Enter number of batteries you want to remove: ";
                               numberOfRemovedBatteries = flightView.askPlayerValue(player, message);

                               if(numberOfRemovedBatteries <= ((Battery)component).getBatteryPower() && numberOfRemovedBatteries <= numberOfBatteriesToRemove){

                                   numberOfBatteriesToRemove -= numberOfRemovedBatteries;

                                   for(int i = 0; i < numberOfRemovedBatteries; i++){
                                       ((Battery)component).removeBattery();
                                   }
                               }

                            }
                        }
                    }

                }

            }


        }

    }
}
