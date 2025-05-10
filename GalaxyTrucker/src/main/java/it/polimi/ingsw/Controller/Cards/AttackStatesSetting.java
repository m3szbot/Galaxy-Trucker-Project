package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.View.FlightView.FlightView;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;

/**
 * Abstract class that extends cards and implements a method used
 * by the cards that attack the player one after the other in sequence
 *
 * @author carlo
 */

public abstract class AttackStatesSetting extends Card implements FirePowerChoice {

    /**
     * Sets the attack state of the various players
     *
     * @param flightView
     * @param flightBoard
     * @param requirementNumber
     * @return attackstates of the various player. It may be null for some player if a player
     * in front of them in the flightBoard has defeated the enemies.
     */

    public AttackStates[] setAttackStates(FlightView flightView, FlightBoard flightBoard, int requirementNumber) {

        String message;
        int i;
        AttackStates[] results = new AttackStates[flightBoard.getPlayerOrderList().size()];
        Float chosenFirePower;

        for (i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            chosenFirePower = chooseFirePower(flightBoard.getPlayerOrderList().get(i), flightView);

            if (chosenFirePower > requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                        "enemies!";
                flightView.sendMessageToAll(message);
                results[i] = AttackStates.EnemyDefeated;
                break;

            } else if (chosenFirePower == requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " equalized the" +
                        "enemies!";
                results[i] = AttackStates.Equalized;
                flightView.sendMessageToAll(message);

            } else {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has been" +
                        " defeated by the enemies!";
                results[i] = AttackStates.PlayerDefeated;
                flightView.sendMessageToAll(message);

            }

        }

        return results;
    }

}
