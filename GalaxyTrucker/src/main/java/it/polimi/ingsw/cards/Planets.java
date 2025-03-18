package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

/**
 * class that represent the card planets
 * @author carlo
 */

public class Planets extends Card{

    public int daysLost;

    private int[] planet1;
    private int[] planet2;
    private int[] planet3;

    public Planets(CardBuilder cardBuilder){

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.planet1 = cardBuilder.planet1;
        this.planet2 = cardBuilder.planet2;
        this.planet3 = cardBuilder.planet3;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView){

    }
}
