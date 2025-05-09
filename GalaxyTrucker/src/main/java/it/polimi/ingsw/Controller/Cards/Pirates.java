package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

/**
 * Class representing the card pirates.
 *
 * @author carlo
 */

public class Pirates extends AttackStatesSetting implements SufferBlows, CreditsGain, Movable, FirePowerChoice {

    private int daysLost;
    private int gainedCredit;
    private int requirementNumber;
    private ElementType blowType;
    private Blow[] blows;

    public Pirates(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.gainedCredit = cardBuilder.getGainedCredit();
        this.requirementNumber = cardBuilder.getRequirementNumber();
        this.blowType = cardBuilder.getBlowType();
        this.blows = cardBuilder.getBlows();

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size(), i;
        String message;
        AttackStates[] results;

        results = setAttackStates(flightView, flightBoard, requirementNumber);

        //rolling all dices
        for (i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        for (i = 0; i < numberOfPlayers; i++) {

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";

                if (flightView.askPlayerGenericQuestion(flightBoard.getPlayerOrderList().get(i), message)) {
                    //player decides to collect the reward

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "has collected the reward!";

                    giveCredits(flightBoard.getPlayerOrderList().get(i), gainedCredit);
                    changePlayerPosition(flightBoard.getPlayerOrderList().get(i), daysLost, flightBoard);
                    flightView.sendMessageToAll(message);

                } else {

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "hasn't collected the reward!";

                    flightView.sendMessageToAll(message);
                }

                break;
            } else if (results[i] == AttackStates.PlayerDefeated) {

                hit(flightBoard.getPlayerOrderList().get(i), blows, blowType, flightBoard, flightView);

            }

        }

    }

}
