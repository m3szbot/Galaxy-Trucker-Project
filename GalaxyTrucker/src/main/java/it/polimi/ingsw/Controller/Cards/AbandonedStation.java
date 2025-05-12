package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.FlightView.FlightView;

/**
 * class that represent the card abbandonedStation
 *
 * @author carlo
 */

//check that the adding of requirementNumber doesn't compromise json file

public class AbandonedStation extends Card implements Movable, GoodsGain {

    private int daysLost, requirementNumber;
    private int[] goods;

    public AbandonedStation(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.goods = cardBuilder.getGoods();
        this.requirementNumber = cardBuilder.getRequirementNumber();

    }

    @Override

    public void resolve(FlightBoard flightBoard, int gameCode) {

        DataContainer dataContainer;

        for (Player player : flightBoard.getPlayerOrderList()) {

            if (isCrewSatisfying(player, requirementNumber)) {
                //player has the possibility to solve the card

                message = "Do you want to solve the card ?";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                    //player decides to solve the card

                    giveGoods(player, goods, flightBoard, gameCode);
                    changePlayerPosition(player, daysLost, flightBoard);

                    message = player.getNickName() + "has solved the card!";
                    for (Player player1 : flightBoard.getPlayerOrderList()) {
                        dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player1);
                        dataContainer.setMessage(message);
                        dataContainer.setCommand("printMessage");
                        ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player1);
                    }
                    break;
                }
            }
        }

        message = "Nobody solved the card!";
        for (Player player1 : flightBoard.getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player1);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player1);
        }
        flightBoard.updateFlightBoard();
        for (Player player : flightBoard.getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setFlightBoard(flightBoard);
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);
        }
    }

    /**
     * @param player   target player
     * @param quantity quantity of the specified requirement that you want to verify
     * @return true if the player has met the requirement in the specified quantity,
     * false otherwise
     * @author Carlo
     */

    private boolean isCrewSatisfying(Player player, int quantity) {

        if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= quantity) {
            return true;
        }

        return false;

    }
}
