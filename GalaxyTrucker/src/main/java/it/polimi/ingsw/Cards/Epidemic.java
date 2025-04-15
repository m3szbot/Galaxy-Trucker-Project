package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.Components.Cabin;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card epidemic
 *
 * @author carlo
 */

public class Epidemic extends Card {

    public Epidemic(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for(int i = 0; i < flightBoard.getPlayerOrderList().size(); i++){

            removeAdjacentAstronauts(flightBoard.getPlayerOrderList().get(i), flightView);
        }


    }

    /**
     * method that handles infected cabins when the epidemic
     * adventure card is being solved
     *
     * @param player target player
     * @param flightView class to comunicate with the player
     * @author Carlo
     *
     */

    private void removeAdjacentAstronauts(Player player, FlightView flightView) {

        String message;
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatrixCols();
        int i, j, numberOfRemovedInhabitants = 0;
        Component centralComponent, backComponent, frontComponent, leftComponent, rightComponent;

        //all false by default
        boolean[][] infectedCabins = new boolean[rows][cols];

        for(i = 0; i < rows; i++){

            for(j = 0; j < cols; j++){

                centralComponent = player.getShipBoard().getComponent(i, j);

                //component is not null
                if(centralComponent != null){
                    //component is a cabin
                    if(centralComponent.getComponentName().equals("Cabin")){
                        //cabin is not empty
                        if(((Cabin)centralComponent).getCrewMembers() > 0){
                            //the cabin can be potentially infected if some conditions are verified

                            frontComponent = player.getShipBoard().getComponent(i + 1, j);
                            backComponent = player.getShipBoard().getComponent(i - 1, j);
                            rightComponent = player.getShipBoard().getComponent(i, j + 1);
                            leftComponent = player.getShipBoard().getComponent(i, j - 1);

                            //checking if there is a cabin on the front

                            if(frontComponent != null && !infectedCabins[i][j]){

                                if(frontComponent.getComponentName().equals("Cabin")){

                                    if(((Cabin)frontComponent).getCrewMembers() > 0 || infectedCabins[i + 1][j]){

                                        infectedCabins[i][j] = true;
                                        numberOfRemovedInhabitants++;
                                        ((Cabin)centralComponent).removeInhabitant();

                                    }
                                }
                            }

                            //checking if there is a cabin on the back

                            if(backComponent != null && !infectedCabins[i][j]){

                                if(backComponent.getComponentName().equals("Cabin")){

                                    if(((Cabin)backComponent).getCrewMembers() > 0 || infectedCabins[i - 1][j]){

                                        infectedCabins[i][j] = true;
                                        numberOfRemovedInhabitants++;
                                        ((Cabin)centralComponent).removeInhabitant();
                                    }
                                }
                            }

                            //checking if there is a cabin on the right

                            if(rightComponent != null && !infectedCabins[i][j]){

                                if(rightComponent.getComponentName().equals("Cabin")){

                                    if(((Cabin)rightComponent).getCrewMembers() > 0 || infectedCabins[i][j + 1]){

                                        infectedCabins[i][j] = true;
                                        numberOfRemovedInhabitants++;
                                        ((Cabin)centralComponent).removeInhabitant();

                                    }
                                }
                            }

                            //cheking if there is a cabin on the left

                            if(leftComponent != null && !infectedCabins[i][j]){

                                if(leftComponent.getComponentName().equals("Cabin")){

                                    if(((Cabin)leftComponent).getCrewMembers() > 0 || infectedCabins[i][j - 1]){

                                        infectedCabins[i][j] = true;
                                        numberOfRemovedInhabitants++;
                                        ((Cabin)centralComponent).removeInhabitant();
                                    }
                                }
                            }

                        }

                    }
                }

            }
        }

        player.getShipBoard().getShipBoardAttributes().updateCrewMembers(-numberOfRemovedInhabitants);
        message = "Player " + player.getNickName() + "lost " + numberOfRemovedInhabitants +
                " inhabitants from the epidemic!";
        flightView.sendMessageToAll(message);

    }


}
