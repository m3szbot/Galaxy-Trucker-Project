package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * class that represent the card abbandonedShip
 *
 * @author carlo
 */

public class AbandonedShip extends Card implements Movable, TokenLoss, CreditsGain {

    private int daysLost;
    private ElementType lossType;
    private int lossNumber;
    private int gainedCredit;

    public AbandonedShip(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.daysLost = cardBuilder.getDaysLost();
        this.lossType = cardBuilder.getLossType();
        this.lossNumber = cardBuilder.getLossNumber();
        this.gainedCredit = cardBuilder.getGainedCredit();

    }

    @Override

    public void resolve(FlightBoard flightBoard, int gameCode) {

        DataContainer dataContainer;

        for (Player player : flightBoard.getPlayerOrderList()) {

            //player can afford to lose some crew members to solve the card if he wants to

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= lossNumber) {

                message = "Do you want to solve the card ? ";
                dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

                if (ClientMessenger.getGameMessenger(gameCode).getPlayerInput(player).equalsIgnoreCase("Yes")) {
                    //player decide to solve the card

                    inflictLoss(player, lossType, lossNumber, flightBoard, gameCode);
                    giveCredits(player, gainedCredit);
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
        for (Player player : flightBoard.getPlayerOrderList()) {

            dataContainer = ClientMessenger.getGameMessenger(gameCode).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(gameCode).sendPlayerData(player);

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
