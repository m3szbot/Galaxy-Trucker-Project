package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.View.FlightView.FlightView;

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

    public abstract void resolve(GameInformation gameInformation);

    public int getCardLevel() {
        return cardLevel;
    }

    public String getCardName() {
        return cardName;
    }
}

