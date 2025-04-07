package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;

import java.util.List;

/**
 * class that represent the card smugglers
 *
 * @author carlo
 */

public class Smugglers extends Card implements Movable, Requirement, GoodsGain, TokenLoss{

    private int daysLost;
    private ElementType requirementType;
    private ElementType lossType;
    private int lossNumber;
    private int[] goods;

    public Smugglers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.requirementType = cardBuilder.requirementType;
        this.lossType = cardBuilder.lossType;
        this.lossNumber = cardBuilder.lossNumber;
        this.goods = cardBuilder.goods;


    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

    }
}
