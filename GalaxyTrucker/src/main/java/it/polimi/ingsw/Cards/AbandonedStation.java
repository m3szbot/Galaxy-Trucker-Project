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

public class AbandonedStation extends Card implements Movable, GoodsGain{

    private int daysLost, requirementNumber;
    private int[] goods;

    public AbandonedStation(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.goods = cardBuilder.goods;
        this.requirementNumber = cardBuilder.requirementNumber;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for(Player player : flightBoard.getPlayerOrderList()){

            if(isCrewSatisfying(player, requirementNumber, flightView)){
                //player has the possibility to solve the card

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

    /**
     *
     * @param player target player
     * @param quantity quantity of the specified requirement that you want to verify
     * @return true if the player has met the requirement in the specified quantity,
     * false otherwise
     *
     * @author Carlo
     */

    private boolean isCrewSatisfying(Player player, int quantity, FlightView flightView) {
        int addedPower = 0;
        String message;

        if (player.getShipBoard().getShipBoardAttributes().getCrewMembers() >= quantity) {
            return true;
        }

        return false;

    }
}
