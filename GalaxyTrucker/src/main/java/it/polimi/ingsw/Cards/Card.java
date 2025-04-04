package it.polimi.ingsw.Cards;

/**
 * Abstract class that is the superclass of every card class.
 *
 * @author carlo
 */

public abstract class Card {

    protected int cardLevel;
    protected String cardName;
    protected boolean solved = false;
    protected String informationString;

    /**
     * Abstract method that must be defined for every card class. It is
     * the method which is responsible for resolving the card.
     *
     * @return nothing
     * @parameters array of players, the flightBoard to move the player for the
     * lost days, the flightview which is responsible for the view part of the
     * MVC.
     */

    /*

    public abstract void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView);


     */
    protected boolean isSolved() {
        return solved;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public String getCardName() {
        return cardName;
    }
}

