package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.Shipboard.Player;

public interface SufferBlows {

    default void hit(Player player, Blow[] blows, ElementType blowType, FlightView flightView) {

        int randomNumber, i, leftCoord = 0, rightCoord = 0, blowNumber = 0;
        int rows = player.getShipBoard().getMatrixRows();
        int cols = player.getShipBoard().getMatricCols();
        boolean componentFlag;
        String direction, message;

        for(Blow blow: blows){
            blowNumber++;

            componentFlag = false; //true if there is a component on the trajectory of the blow

            randomNumber = (int) (Math.random() * 13); //Generating random number from 0 to 12.


            //blow coming from front
            if(blow.getDirection() == 0){

                direction = new String("front");

                for(i = 0;i < rows; i++){

                    if(player.getShipBoard().getComponent(i, randomNumber) != null){
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }

                }

            }
            //blow coming from right
            else if(blow.getDirection() == 1){


                direction = new String("right");

                for(i = cols - 1;i >= 0; i--){

                    if(player.getShipBoard().getComponent(randomNumber, i) != null){
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }

            }
            //blow coming from back
            else if(blow.getDirection() == 2){


                direction = new String("back");

                for(i = rows - 1; i >= 0; i--){

                    if(player.getShipBoard().getComponent(i, randomNumber) != null){
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }
                }

            }
            //blow coming from left
            else{

                direction = new String("left");

                for(i = 0; i < cols; i++){

                    if(player.getShipBoard().getComponent(randomNumber, i) != null){
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }


            }

            //miss
            if(!componentFlag){
                flightView.sendMessageToAll("Blow number " + blowNumber + "has missed " + player.getNickName());
                continue;
            }


            if(blowType == ElementType.CannonBlow){

                if(blow.isBig()){

                    player.getShipBoard().removeComponent(leftCoord, rightCoord);
                }
                else{
                    if(player.getShipBoard().shipBoardAttributes.checkSide(blow.getDirection())) {


                        message = new String("The small cannon blow is directed on position ["
                                + leftCoord + "," + rightCoord + "] from the " +
                                direction + "!\n Do you want to defend yourself with shields ?");

                        if (flightView.askPlayerGenericQuestion(player, message)){

                            int[] coordinates = new int[2];

                            message = "Enter coordinate: ";

                            while(true) {

                                coordinates = flightView.askPlayerCoordinates(player, message);

                                if((player.getShipBoard().getComponent(coordinates[0], coordinates[1]).getComponentName().equals("Battery"))){
                                    break;
                                }

                                message = "Invalid coordinate, reenter coordinate: ";

                            }




                        }

                    }

                }

            }
            else{


            }

        }

    }

}
