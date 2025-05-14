package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
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
     * @param requirementNumber
     * @return attackstates of the various player. It may be null for some player if a player
     * in front of them in the flightBoard has defeated the enemies.
     */

    public AttackStates[] setAttackStates(int requirementNumber, GameInformation gameInformation) {

        String message;
        DataContainer dataContainer;
        int i;
        AttackStates[] results = new AttackStates[gameInformation.getFlightBoard().getPlayerOrderList().size()];
        Float chosenFirePower;

        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            chosenFirePower = chooseFirePower(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gameInformation);

            if (chosenFirePower > requirementNumber) {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                        "enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

                results[i] = AttackStates.EnemyDefeated;
                break;

            } else if (chosenFirePower == requirementNumber) {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " equalized the" +
                        "enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

                results[i] = AttackStates.Equalized;

            } else {

                message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() + " has been" +
                        " defeated by the enemies!";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

                results[i] = AttackStates.PlayerDefeated;

            }

        }

        return results;
    }

}
