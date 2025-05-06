package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Connection.ClientSide.View.FlightView.FlightView;

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

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for (Player player : flightBoard.getPlayerOrderList()) {

            //player can afford to lose some crew members to solve the card if he wants to

            if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= lossNumber) {

                message = "Do you want to solve the card ? ";

                if (flightView.askPlayerGenericQuestion(player, message)) {
                    //player decide to solve the card

                    inflictLoss(player, lossType, lossNumber, flightBoard, flightView);
                    giveCredits(player, gainedCredit);
                    changePlayerPosition(player, daysLost, flightBoard);

                    message = player.getNickName() + "has solved the card!";
                    flightView.sendMessageToAll(message);

                    break;
                }
            }
        }

        message = "Nobody solved the card!";
        flightView.sendMessageToAll(message);

    }

}
