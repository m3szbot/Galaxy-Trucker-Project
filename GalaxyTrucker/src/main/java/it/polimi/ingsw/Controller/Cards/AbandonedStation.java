package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.Messengers.PlayerMessenger;
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

    }

    @Override

    public void resolve(GameInformation gameInformation) {

        boolean solved = false;

        PlayerMessenger playerMessenger;

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {

            if (isCrewSatisfying(player, requirementNumber)) {
                //player has the possibility to solve the card

                message = "Do you want to solve the card ?";
                playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) {
                        //player decides to solve the card

                        giveGoods(player, goods, gameInformation);
                        changePlayerPosition(player, daysLost, gameInformation.getFlightBoard());

                        message = player.getNickName() + "has solved the card!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        break;
                    } else {
                        message = player.getNickName() + "hasn't solved the card.\n";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        gameInformation.getFlightBoard().updateFlightBoard();
                    }
                } catch (PlayerDisconnectedException e) {
                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                }
            }

            message = "You finished your turn, wait for the other players.\n";
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printMessage(message);

        }

        if (!solved) {
            message = "Nobody solved the card!";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
            gameInformation.getFlightBoard().updateFlightBoard();
        }

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }

    }

    public void showCard() {

        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Days lost: " + daysLost);
        System.out.println("Crew members required:" + requirementNumber);

        printGoods(goods);

    }

    /**
     * @param player   target player
     * @param quantity quantity of the specified requirement that you want to verify
     * @return true if the player has met the requirement in the specified quantity,
     * false otherwise
     * @author Carlo
     */

    private boolean isCrewSatisfying(Player player, int quantity) {

        return player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= quantity;

    }
}
