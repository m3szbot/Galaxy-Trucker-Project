package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

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

    public void resolve(GameInformation gameInformation) {

        int numberOfPlayers = gameInformation.getFlightBoard().getPlayerOrderList().size(), i;
        String message;
        DataContainer dataContainer;
        AttackStates[] results;

        results = setAttackStates(gameInformation.getFlightBoard(), gameInformation.getGameCode(), requirementNumber);

        //rolling all dices
        for (i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        for (i = 0; i < numberOfPlayers; i++) {

            Player player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";
                dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                try {
                    if ("Yes".equalsIgnoreCase(ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerInput(player))) {
                        //player decides to collect the reward

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                "has collected the reward!";
                        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                        }

                        giveCredits(gameInformation.getFlightBoard().getPlayerOrderList().get(i), gainedCredit);
                        changePlayerPosition(gameInformation.getFlightBoard().getPlayerOrderList().get(i), daysLost, gameInformation.getFlightBoard());

                    } else {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                "hasn't collected the reward!";
                        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                        }

                    }
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    for (Player player1 : gameInformation.getPlayerList()) {
                        dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                        dataContainer.setMessage(message);
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                    }
                }

                break;
            } else if (results[i] == AttackStates.PlayerDefeated) {

                hit(gameInformation.getFlightBoard().getPlayerOrderList().get(i), blows, blowType, gameInformation.getFlightBoard(), gameInformation.getGameCode());

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
