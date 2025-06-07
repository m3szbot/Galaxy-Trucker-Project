package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Class that represent the card meteorswarm
 *
 * @author carlo
 */

public class MeteorSwarm extends Card implements SufferBlows {

    private Blow[] blows;
    private ElementType blowType;

    public MeteorSwarm(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.blows = cardBuilder.getBlows();
        this.blowType = cardBuilder.getBlowType();

    }

    public void showCard() {
        System.out.println("Card name: " + getCardName());
        System.out.println("Card level: " + getCardLevel());
        System.out.println("Blow type: " + blowType.toString());

        printBlows(blows);
    }

    @Override

    public void resolve(GameInformation gameInformation) {

        String message;
        PlayerMessenger playerMessenger;

        //rolling all dices
        for (int i = 0; i < blows.length; i++) {
            if (blows[i] != null) {
                blows[i].rollDice();
            }
        }

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {

            message = "Player " + player.getNickName() + " is in a meteor swarm!!\n";
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendMessageToAll(message);

            hit(player, blows, blowType, gameInformation);

        }

        gameInformation.getFlightBoard().updateFlightBoard();

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            playerMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerMessenger(player);
            playerMessenger.printFlightBoard(gameInformation.getFlightBoard());
        }
    }
}
