package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * Class representing the card pirates.
 *
 * @author carlo
 */

public class Pirates extends Card implements SufferBlows, CreditsGain, Movable, Requirement{

    private int daysLost;
    private int gainedCredit;
    private ElementType requirementType;
    private int requirementNumber;
    private ElementType blowType;
    private Blow[] blows;

    public Pirates(CardBuilder cardBuilder) {

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

    public void resolve(FlightBoard flightBoard, FlightView flightView) {



    }

}
