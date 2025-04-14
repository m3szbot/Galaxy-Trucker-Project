package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightPhase.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;

/**
 * Abstract class that is the superclass of every card class.
 *
 * @author carlo
 */

public abstract class Card {

    protected int cardLevel;
    protected String cardName;
    protected String message;

    /**
     * Abstract method that must be defined for every card class. It is
     * the method which is responsible for resolving the card.
     *
     * @return nothing
     * @parameters array of players, the flightBoard to move the player for the
     * lost days, the flightview which is responsible for the view part of the
     * MVC.
     */

    public abstract void resolve(FlightBoard flightBoard, FlightView flightView);


    public int getCardLevel() {
        return cardLevel;
    }

    public String getCardName() {
        return cardName;
    }
}

