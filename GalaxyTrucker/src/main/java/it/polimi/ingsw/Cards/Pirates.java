package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;

/**
 * Class representing the card pirates.
 *
 * @author carlo
 */

public class Pirates extends Card implements SufferBlows, CreditsGain, Movable, FirePowerChoice {

    private int daysLost;
    private int gainedCredit;
    private int requirementNumber;
    private ElementType blowType;
    private Blow[] blows;

    public Pirates(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.gainedCredit = cardBuilder.gainedCredit;
        this.requirementNumber = cardBuilder.requirementNumber;
        this.blowType = cardBuilder.blowType;
        this.blows = cardBuilder.blows;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size(), i;
        float chosenFirePower;
        String message;
        AttackStates[] results = new AttackStates[numberOfPlayers];

        for(i = 0; i < numberOfPlayers; i++){

           chosenFirePower = chooseFirePower(flightBoard.getPlayerOrderList().get(i), flightView);

           if(chosenFirePower > requirementNumber){

               message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                       "enemies!";
                flightView.sendMessageToAll(message);
                results[i] = AttackStates.EnemyDefeated;
                break;

           }
           else if(chosenFirePower == requirementNumber){

               message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " equalized the" +
                       "enemies!";
               results[i] = AttackStates.Equalized;
               flightView.sendMessageToAll(message);

           }
           else{

               message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has been" +
                       " defeated by the enemies!";
               results[i] = AttackStates.PlayerDefeated;
               flightView.sendMessageToAll(message);

           }

        }

        //rolling all dices
        for(i = 0; i < blows.length; i++){
            blows[i].rollDice();
        }

        for(i = 0; i < numberOfPlayers; i++){

            if(results[i] == AttackStates.EnemyDefeated){

                message = "Would you like to collect the reward for defeating the enemies ?";

                if(flightView.askPlayerGenericQuestion(flightBoard.getPlayerOrderList().get(i), message)){
                    //player decides to collect the reward

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "has collected the reward!";

                    giveCredits(flightBoard.getPlayerOrderList().get(i), gainedCredit);
                    changePlayerPosition(flightBoard.getPlayerOrderList().get(i), daysLost, flightBoard);
                    flightView.sendMessageToAll(message);

                }
                else{

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "hasn't collected the reward!";

                    flightView.sendMessageToAll(message);
                }

                break;
            }
            else if(results[i] == AttackStates.PlayerDefeated){

                hit(flightBoard.getPlayerOrderList().get(i), blows, blowType, flightView);

            }

        }

    }

}
