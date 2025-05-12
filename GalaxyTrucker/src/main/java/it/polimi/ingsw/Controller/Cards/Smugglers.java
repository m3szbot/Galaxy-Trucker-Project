package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
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

    @Override

    public void resolve(FlightBoard flightBoard, int gameCode) {

        int numberOfPlayers = flightBoard.getPlayerOrderList().size(), i;
        String message;
        DataContainer dataContainer;
        AttackStates[] results;

        results = setAttackStates(flightBoard, requirementNumber, gameCode);

        for (i = 0; i < numberOfPlayers; i++) {

            Player player = flightBoard.getPlayerOrderList().get(i);

            if (results[i] == AttackStates.EnemyDefeated) {

                message = "Would you like to collect the reward for defeating the enemies ?";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "has collected the reward!";
                    for (Player player1 : flightBoard.getPlayerOrderList()) {
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player1);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player1);
                    }

                    giveGoods(flightBoard.getPlayerOrderList().get(i), goods, flightBoard, gameCode);
                    changePlayerPosition(flightBoard.getPlayerOrderList().get(i), daysLost, flightBoard);

                } else {

                    message = "Player " + flightBoard.getPlayerOrderList().get(i).getNickName() +
                            "hasn't collected the reward!";
                    for (Player player1 : flightBoard.getPlayerOrderList()) {
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player1);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player1);
                    }

                }

                break;
            } else if (results[i] == AttackStates.PlayerDefeated) {

                inflictLoss(flightBoard.getPlayerOrderList().get(i), lossType, lossNumber, flightBoard, gameCode);

            }

        }

        flightBoard.updateFlightBoard();
        for (Player player : flightBoard.getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setFlightBoard(flightBoard);
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
        }

    }
}
