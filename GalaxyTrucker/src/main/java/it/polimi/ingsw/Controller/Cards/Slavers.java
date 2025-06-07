package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card slavers
 *
 * @author carlo
 */

public class Slavers extends AttackStatesSetting implements CreditsGain, Movable, TokenLoss, FirePowerChoice {

    private int daysLost;
    private int gainedCredits;
    private ElementType lossType;
    private int lossNumber;
    private int requirementNumber;

    public Slavers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.gainedCredits = cardBuilder.getGainedCredits();
        this.lossType = cardBuilder.getLossType();
        this.requirementNumber = cardBuilder.getRequirementNumber();
        this.lossNumber = cardBuilder.getLossNumber();

    }

    public void showCard() {

        System.out.println("\nCard name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Gained credit: " + gainedCredits);
        System.out.println("Requirement number: " + requirementNumber + " (fire power)\n");


    }

    @Override

    public void resolve(GameInformation gameInformation) {


        int i;
        String message;
        PlayerMessenger playerMessenger;
        AttackStates[] results;

        //Resolves the attack (win or defeat) of each in-game player
        results = setAttackStates(requirementNumber, gameInformation);

        //Cycles through the in-game players to give the reward or to inflict the losses
        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            Player player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            if (results[i] == AttackStates.EnemyDefeated) {

                //If the player defeats the enemy they lost a certain number of days to do so
                changePlayerPosition(gameInformation.getFlightBoard().getPlayerOrderList().get(i), -daysLost, gameInformation.getFlightBoard());

                message = "Would you like to collect the reward for defeating the enemies ?";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                "has collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                        giveCredits(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gainedCredits);

                    } else {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                " hasn't collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }
                break;

            } else if (results[i] == AttackStates.PlayerDefeated) {

                try {

                    inflictLoss(gameInformation.getFlightBoard().getPlayerOrderList().get(i), lossType, lossNumber, gameInformation);

                } catch (NoHumanCrewLeftException e) {

                    message = e.getMessage();
                    playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                    playerMessenger.printMessage(message);

                    gameInformation.getFlightBoard().eliminatePlayer(player);
                    i--;

                }

            }

            if (playerMessenger != null) {
                message = "You finished your turn, wait for the other players.\n";
                playerMessenger.printMessage(message);
            }
        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }


}
