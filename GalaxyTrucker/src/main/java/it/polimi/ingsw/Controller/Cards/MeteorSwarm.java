package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
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

        DataContainer dataContainer;

        //leader rolling the dices for each blow
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {

            hit(player, blows, blowType, gameInformation);
        }

        gameInformation.getFlightBoard().updateFlightBoard();
        for (Player player : gameInformation.getFlightBoard().getPlayerOrderList()) {
            dataContainer = ClientMessenger.getGameMessenger(gameInformation.getGameCode()).getPlayerContainer(player);
            dataContainer.setFlightBoard(gameInformation.getFlightBoard());
            dataContainer.setCommand("printFlightBoard");
            ClientMessenger.getGameMessenger(gameInformation.getGameCode()).sendPlayerData(player);
            dataContainer.clearContainer();
        }
    }
}
