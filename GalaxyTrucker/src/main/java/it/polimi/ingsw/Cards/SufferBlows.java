package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

public interface SufferBlows {

    default void hit(Player player, Blow[] blows, ElementType blowType) {

        int randomNumber, i, leftCoord = 0, rightCoord = 0;
        int rows = player.shipStructure.structureMatrix.length;
        int cols = player.shipStructure.structureMatrix[0].length;
        boolean componentFlag;

        for(Blow blow: blows){

            componentFlag = false;
            randomNumber = (int) (Math.random() * 13); //Generating random number from 0 to 12.


            //blow coming from front
            if(blow.getDirection() == 0){

                for(i = 0;i < rows; i++){

                    if(player.shipStructure.structureMatrix[i][randomNumber] != null){
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }

                }

            }
            //blow coming from right
            else if(blow.getDirection() == 1){

                for(i = cols - 1;i >= 0; i--){

                    if(player.shipStructure.structureMatrix[randomNumber][i] != null){
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }

            }
            //blow coming from back
            else if(blow.getDirection() == 2){

                for(i = rows - 1; i >= 0; i--){

                    if(player.shipStructure.structureMatrix[i][randomNumber] != null){
                        componentFlag = true;
                        leftCoord = i;
                        rightCoord = randomNumber;
                        break;
                    }
                }

            }
            //blow coming from left
            else{

                for(i = 0; i < cols; i++){

                    if(player.shipStructure.structureMatrix[randomNumber][i] != null){
                        componentFlag = true;
                        leftCoord = randomNumber;
                        rightCoord = i;
                        break;
                    }
                }


            }

            //miss
            if(!componentFlag){
                System.out.println("Miss");
                continue;
            }


            if(blowType == ElementType.CannonBlow){

                if(blow.isBig()){

                    player.shipStructure.removeComponent(leftCoord, rightCoord);
                }
                else{



                }

            }
            else{


            }

        }

    }

}
