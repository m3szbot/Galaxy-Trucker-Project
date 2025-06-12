package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.NoHumanCrewLeftException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Class representing the card pirates.
 *
 * @author carlo
 */

public class Pirates extends AttackStatesSetting implements SufferBlows, CreditsGain, Movable, FirePowerChoice {

    private int daysLost;
    private int gainedCredits;
    private int requirementNumber;
    private ElementType blowType;
    private Blow[] blows;

    public Pirates(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.gainedCredits = cardBuilder.getGainedCredits();
        this.requirementNumber = cardBuilder.getRequirementNumber();
        this.blowType = cardBuilder.getBlowType();
        this.blows = cardBuilder.getBlows();

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        int i;
        String message;
        PlayerMessenger playerMessenger;
        AttackStates[] results;
        Player player;

        //Cycles through the player list so doesn't need any catches
        results = setAttackStates(requirementNumber, gameInformation);

        //rolling all dices
        for (i = 0; i < blows.length; i++) {
            if (blows[i] != null) {
                blows[i].rollDice();
            }
        }

        for (i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";
                playerMessenger.printMessage(message);

                changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                try {
                    if (playerMessenger.getPlayerBoolean()) {

                        //player decides to collect the reward
                        message = "Player " + player.getNickName() +
                                " has collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                        giveCredits(player, gainedCredits);

                    } else {

                        //player decides not to collect the reward
                        message = "Player " + player.getNickName() +
                                " hasn't collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);

                }
                break;

            } else if (results[i] == AttackStates.PlayerDefeated) {

                message = "Player " + player.getNickName() + " is under pirate fire!!\n";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                try {

                    hit(player, blows, blowType, gameInformation);

                } catch (NoHumanCrewLeftException e) {

                    message = e.getMessage();
                    playerMessenger.printMessage(message);

                    message = "Player " + player.getNickName() + " has no crew members left to continue the voyage and was eliminated!\n";
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    PlayerFlightInputHandler.removePlayer(player);

                    gameInformation.getFlightBoard().eliminatePlayer(player);
                    i--;

                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    i--;

                }

            }


            if (playerMessenger != null) {
                message = "You finished your turn, please wait for the other players.\n";
                playerMessenger.printMessage(message);
            }

            if (player != null) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        try {
            gameInformation.getFlightBoard().updateFlightBoard();

        } catch (LappedPlayersException e) {
            ExceptionsHandler.handleLappedPlayersException(ClientMessenger.getGameMessenger(gameInformation.getGameCode()), e);
        }


    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Gained credit: " + gainedCredits);
        System.out.println("Blow type: " + blowType.toString());
        System.out.println("Requirement number: " + requirementNumber + " (fire power)");
        printBlows(blows);
        System.out.println();

    }

}
