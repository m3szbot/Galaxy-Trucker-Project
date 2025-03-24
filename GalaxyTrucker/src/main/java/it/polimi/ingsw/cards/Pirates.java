package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

/**
 * Class representing the card pirates.
 * @author carlo
 */

public class Pirates extends Card{

    private int daysLost;
    private int gainedCredit;
    private ElementType requirementType;
    private int requirementNumber;
    private ElementType blowType;
    private Blow[] blows;

    public Pirates(CardBuilder cardBuilder){

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.gainedCredit = cardBuilder.gainedCredit;
        this.requirementType = cardBuilder.requirementType;
        this.requirementNumber = cardBuilder.requirementNumber;
        this.blowType = cardBuilder.blowType;
        this.blows = cardBuilder.blows;

    }

    @Override
    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView){

    }

}
