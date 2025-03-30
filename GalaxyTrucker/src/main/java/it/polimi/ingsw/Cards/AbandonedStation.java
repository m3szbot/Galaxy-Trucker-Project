package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card abbandonedStation
 *
 * @author carlo
 */

public class AbandonedStation extends Card {

    private int daysLost;
    private ElementType requirementType;
    private int[] goods;

    public AbandonedStation(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.requirementType = cardBuilder.requirementType;
        this.goods = cardBuilder.goods;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }


}
