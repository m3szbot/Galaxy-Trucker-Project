package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * class that represent the card abbandonedShip
 *
 * @author carlo
 */

public class AbandonedShip extends Card implements Movable, TokenLoss, CreditsGain{

    private int daysLost;
    private ElementType lossType;
    private int lossNumber;
    private int gainedCredit;

    public AbandonedShip(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.lossType = cardBuilder.lossType;
        this.lossNumber = cardBuilder.lossNumber;
        this.gainedCredit = cardBuilder.gainedCredit;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for(Player player : flightBoard.getPlayerOrderList()){

            //player can afford to lose some crew members to solve the card if he wants to

            if(player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= lossNumber){

                message = "Do you want to solve the card ? ";

                if(flightView.askPlayerGenericQuestion(player, message)){
                    //player decide to solve the card

                    inflictLoss(player, lossType, lossNumber, flightView);
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
