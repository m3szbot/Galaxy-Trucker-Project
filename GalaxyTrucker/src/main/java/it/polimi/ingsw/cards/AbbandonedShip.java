package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

/**
 * class that represent the card abbandonedShip
 * @author carlo
 */

public class AbbandonedShip extends Card{

    private int daysLost;
    private ElementType lossType;
    private int lossNumber;
    private int gainedCredit;

    public AbbandonedShip(CardBuilder cardBuilder){

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.lossType = cardBuilder.lossType;
        this.lossNumber = cardBuilder.lossNumber;
        this.gainedCredit = cardBuilder.gainedCredit;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView){

    }

}
