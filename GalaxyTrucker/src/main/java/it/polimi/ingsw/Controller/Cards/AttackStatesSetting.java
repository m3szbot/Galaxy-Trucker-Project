package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
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
     * @param flightBoard
     * @param requirementNumber
     * @return attackstates of the various player. It may be null for some player if a player
     * in front of them in the flightBoard has defeated the enemies.
     */

    public AttackStates[] setAttackStates(FlightBoard flightBoard, int requirementNumber, int gameCode) {

        String message;
        DataContainer dataContainer;
        int i;
        AttackStates[] results = new AttackStates[flightBoard.getPlayerOrderList().size()];
        Float chosenFirePower;

        for (i = 0; i < flightBoard.getPlayerOrderList().size(); i++) {

            chosenFirePower = chooseFirePower(flightBoard.getPlayerOrderList().get(i), gameCode);

            if (chosenFirePower > requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has defeated the" +
                        "enemies!";
                for (Player player : flightBoard.getPlayerOrderList()) {
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                }
                results[i] = AttackStates.EnemyDefeated;
                break;

            } else if (chosenFirePower == requirementNumber) {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " equalized the" +
                        "enemies!";
                for (Player player : flightBoard.getPlayerOrderList()) {
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                }
                results[i] = AttackStates.Equalized;

            } else {

                message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() + " has been" +
                        " defeated by the enemies!";
                for (Player player : flightBoard.getPlayerOrderList()) {
                    dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
                }
                results[i] = AttackStates.PlayerDefeated;

            }

        }

        return results;
    }

}
