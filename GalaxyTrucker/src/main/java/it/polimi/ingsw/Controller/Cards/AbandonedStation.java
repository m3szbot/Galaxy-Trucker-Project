package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

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

        printGoods(goods);

    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);


    }

    @Override

    public void resolve(GameInformation gameInformation) {

        DataContainer dataContainer;

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {

            if (isCrewSatisfying(player, requirementNumber)) {
                //player has the possibility to solve the card

                message = "Do you want to solve the card ?";
                ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerMessage(player, message);

                try {
                    if (ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerBoolean(player)) {
                        //player decides to solve the card

                        giveGoods(player, goods, gameInformation.getFlightBoard(), gameInformation);
                        changePlayerPosition(player, daysLost, gameInformation.getFlightBoard());

                        message = player.getNickName() + "has solved the card!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                        break;
                    }
                } catch (PlayerDisconnectedException e) {
                    gameInformation.disconnectPlayer(player);
                    message = e.getMessage();
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
                }
            }
        }

        message = "Nobody solved the card!";
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToALl(message);
        
        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
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
