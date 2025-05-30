package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Model.GameInformation.GameInformation;

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
     * @param requirementNumber
     * @return attack states of the various player. It may be null for some player if a player
     * in front of them in the flightBoard has defeated the enemies.
     */

    public AttackStates[] setAttackStates(int requirementNumber, GameInformation gameInformation) {

        String message;
        int i;
        AttackStates[] results = new AttackStates[gameInformation.getFlightBoard().getPlayerOrderList().size()];
        float chosenFirePower;

        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            chosenFirePower = chooseFirePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

            if (chosenFirePower > requirementNumber) {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                        "enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                results[i] = AttackStates.EnemyDefeated;
                break;

            } else if (chosenFirePower == requirementNumber) {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " equalized the" +
                        "enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                results[i] = AttackStates.Equalized;

            } else {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " has been" +
                        " defeated by the enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                results[i] = AttackStates.PlayerDefeated;

            }

        }

        return results;
    }

}
