package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card slavers
 *
 * @author carlo
 */

public class Slavers extends AttackStatesSetting implements CreditsGain, Movable, TokenLoss, FirePowerChoice {

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

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Gained credit: " + gainedCredit);
        System.out.println("Requirement number: " + requirementNumber + " (fire power)");


    }

    @Override

    public void resolve(GameInformation gameInformation) {


        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size(), i;
        String message;
        DataContainer dataContainer;
        AttackStates[] results;

        results = setAttackStates(requirementNumber, gameInformation);

        for (i = 0; i < numberOfPlayers; i++) {

            Player player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                "has collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

                        giveCredits(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gainedCredit);
                        changePlayerPosition(gameInformation.getFlightBoard().getPlayerOrderList().get(i), daysLost, gameInformation.getFlightBoard());

                    } else {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                " hasn't collected the reward!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    gameInformation.getGamePhase().disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }

                break;
            } else if (results[i] == AttackStates.PlayerDefeated) {

                inflictLoss(gameInformation.getFlightBoard().getPlayerOrderList().get(i), lossType, lossNumber, gameInformation);

            }

        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
        }

    }


}
