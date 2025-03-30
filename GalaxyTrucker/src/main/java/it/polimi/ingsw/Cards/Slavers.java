package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card slavers
 *
 * @author carlo
 */

public class Slavers extends Card {

    private int daysLost;
    private int gainedCredit;
    private ElementType requirementType;
    private ElementType lossType;
    private int lossNumber;
    private int requirementNumber;

    public Slavers(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.gainedCredit = cardBuilder.gainedCredit;
        this.requirementType = cardBuilder.requirementType;
        this.lossType = cardBuilder.lossType;
        this.requirementNumber = cardBuilder.requirementNumber;
        this.lossNumber = cardBuilder.lossNumber;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }


}
