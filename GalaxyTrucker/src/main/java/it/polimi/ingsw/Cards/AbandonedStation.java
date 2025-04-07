package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * class that represent the card abbandonedStation
 *
 * @author carlo
 */

//check that the adding of requirementNumber doesn't compromise json file

public class AbandonedStation extends Card implements Movable, Requirement, GoodsGain{

    private int daysLost, requirementNumber;
    private ElementType requirementType;
    private int[] goods;

    public AbandonedStation(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.requirementType = cardBuilder.requirementType;
        this.goods = cardBuilder.goods;
        this.requirementNumber = cardBuilder.requirementNumber;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for(Player player : flightBoard.getPlayerOrderList()){

            if(isSatisfying(player, requirementType, requirementNumber)){

                message = "Do you want to solve the card ?";

                if(flightView.askPlayerGenericQuestion(player, message)){
                    //player decides to solve the card

                    giveGoods(player, goods, flightView);
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
