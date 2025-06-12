package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.ExceptionsHandler;
import it.polimi.ingsw.Controller.FlightPhase.IndexChecker;
import it.polimi.ingsw.Controller.FlightPhase.PlayerFlightInputHandler;
import it.polimi.ingsw.Model.FlightBoard.LappedPlayersException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card abandonedStation
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
        Player player;
        PlayerMessenger playerMessenger;

        for (int i = 0; i < gameInformation.getFlightBoard().getPlayerOrderList().size(); i++) {

            //Checks the validity of the current index (precaution for disconnection)
            i = IndexChecker.checkIndex(gameInformation, i);

            player = gameInformation.getFlightBoard().getPlayerOrderList().get(i);
            PlayerFlightInputHandler.startPlayerTurn(player);

            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);

            if (isCrewSatisfying(player, requirementNumber)) {
                //player has the possibility to solve the card

                message = "Do you want to solve the card ?";
                playerMessenger.printMessage(message);

                try {
                    if (playerMessenger.getPlayerBoolean()) {
                        //player decides to solve the card

                        giveGoods(player, goods, gameInformation);
                        changePlayerPosition(player, -daysLost, gameInformation.getFlightBoard());

                        message = player.getNickName() + " has solved the card!";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
                        solved = true;
                        PlayerFlightInputHandler.endPlayerTurn(player);
                        break;

                    } else {

                        message = player.getNickName() + " hasn't solved the card.\n";
                        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

                    }
                } catch (PlayerDisconnectedException e) {
                    PlayerFlightInputHandler.removePlayer(player);

                    ClientMessenger.getGameMessenger(gameInformation.getGameCode()).disconnectPlayer(gameInformation, player);
                    i--;

                }
            } else {

                message = "Not enough crew members.\n";
                playerMessenger.printMessage(message);

            }

            if (playerMessenger != null) {
                message = "You finished your turn, wait for the other players.\n";
                playerMessenger.printMessage(message);
            }

            if (player != null) {
                PlayerFlightInputHandler.endPlayerTurn(player);
            }

        }

        if (!solved) {
            message = "Nobody solved the card!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);
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
        System.out.println("Crew members required:" + requirementNumber);
        printGoods(goods);
        System.out.println();

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
