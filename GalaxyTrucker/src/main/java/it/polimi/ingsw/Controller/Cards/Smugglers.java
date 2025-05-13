package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card smugglers
 *
 * @author carlo
 */

public class Smugglers extends AttackStatesSetting implements Movable, GoodsGain, TokenLoss, FirePowerChoice {

    private int daysLost;
    private ElementType lossType;
    private int requirementNumber;
    private int lossNumber;
    private int[] goods;

    public Smugglers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.goods = cardBuilder.getGoods();
        this.requirementNumber = cardBuilder.getRequirementNumber();


    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Loss type: " + lossType.toString());
        System.out.println("Loss number: " + lossNumber);
        System.out.println("Requirement number: " + requirementNumber + " (fire power)");

        printGoods(goods);


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
                dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);

                try {
                    if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {

                        message = "Player " + gameInformation.getFlightBoard().getPlayerOrderList().get(i).getNickName() +
                                "has collected the reward!";
                        for (Player player1 : gameInformation.getFlightBoard().getPlayerOrderList()) {
                            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player1);
                            dataContainer.setMessage(message);
                            dataContainer.setCommand("printMessage");
                            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player1);
                        }

                        giveGoods(gameInformation.getFlightBoard().getPlayerOrderList().get(i), goods, gameInformation.getFlightBoard(), gameInformation);
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
