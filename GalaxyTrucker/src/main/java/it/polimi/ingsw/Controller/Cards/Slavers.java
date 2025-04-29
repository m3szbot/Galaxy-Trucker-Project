package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Controller.FlightPhase.FlightView;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;

/**
 * class that represent the card slavers
 *
 * @author carlo
 */

public class Slavers extends Card implements CreditsGain, Movable, TokenLoss, FirePowerChoice {

    private int daysLost;
    private int gainedCredit;
    private ElementType lossType;
    private int lossNumber;
    private int requirementNumber;

    public Slavers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.gainedCredit = cardBuilder.getGainedCredit();
        this.lossType = cardBuilder.getLossType();
        this.requirementNumber = cardBuilder.getRequirementNumber();
        this.lossNumber = cardBuilder.getLossNumber();

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

        for(i = 0; i < numberOfPlayers; i++){

            if(results[i] == AttackStates.EnemyDefeated){

                message = "Would you like to collect the reward for defeating the enemies ?";

                if(flightView.askPlayerGenericQuestion(flightBoard.getPlayerOrderList().get(i), message)){

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "has collected the reward!";

                    giveCredits(flightBoard.getPlayerOrderList().get(i), gainedCredit);
                    changePlayerPosition(flightBoard.getPlayerOrderList().get(i), daysLost, flightBoard);
                    flightView.sendMessageToAll(message);

                }
                else{

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            " hasn't collected the reward!";

                    flightView.sendMessageToAll(message);
                }

                break;
            }
            else if(results[i] == AttackStates.PlayerDefeated){

                inflictLoss(flightBoard.getPlayerOrderList().get(i), lossType, lossNumber, flightBoard, flightView);

            }

        }
    }


}
