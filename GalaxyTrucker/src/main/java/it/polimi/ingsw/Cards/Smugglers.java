package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card smugglers
 *
 * @author carlo
 */

public class Smugglers extends Card {

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

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }
}
