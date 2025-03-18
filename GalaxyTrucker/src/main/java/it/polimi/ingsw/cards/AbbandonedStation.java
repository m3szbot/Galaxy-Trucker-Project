package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

/**
 * class that represent the card abbandonedStation
 * @author carlo
 */

public class AbbandonedStation extends Card{

    private int daysLost;
    private ElementType requirementType;
    private int[] goods;

    public AbbandonedStation(CardBuilder cardBuilder){

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.requirementType = cardBuilder.requirementType;
        this.goods = cardBuilder.goods;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView){

    }



}
