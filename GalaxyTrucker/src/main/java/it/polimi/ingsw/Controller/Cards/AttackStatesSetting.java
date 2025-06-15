package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

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

        int i;
        AttackStates[] results = new AttackStates[gameInformation.getFlightBoard().getPlayerOrderList().size()];
        float chosenFirePower;
        Player player;
        PlayerMessenger playerMessenger;

        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            message = "It's " + player.getColouredNickName() + "'s turn.\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            try {
                chosenFirePower = chooseFirePower(player, gameInformation);

                if (chosenFirePower > requirementNumber) {

                    message = "Player " + player.getColouredNickName() + " has defeated the " +
                            "enemies!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    results[i] = AttackStates.EnemyDefeated;
                    PlayerFlightInputHandler.endPlayerTurn(player);

                    break;

                } else if (chosenFirePower == requirementNumber) {

                    message = "Player " + player.getColouredNickName() + " equalized the " +
                            "enemies!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    results[i] = AttackStates.Equalized;

                } else {

                    message = "Player " + player.getColouredNickName() + " has been" +
                            " defeated by the enemies!";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    results[i] = AttackStates.PlayerDefeated;

                }

            } catch (PlayerDisconnectedException e) {
                PlayerFlightInputHandler.removePlayer(player);

                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                i--;

            }

            if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).checkPlayerMessengerPresence(player)) {
                message = "Please wait for the other players.\n";
                playerMessenger.printMessage(message);
            }

            if (PlayerFlightInputHandler.checkInputThreadActivity(player)) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        return results;
    }

}
