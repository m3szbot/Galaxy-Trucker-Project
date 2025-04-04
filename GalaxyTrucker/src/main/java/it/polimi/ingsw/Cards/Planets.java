package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card planets
 *
 * @author carlo
 */

public class Planets extends Card {

    public int daysLost;
    private boolean[] occupied = {false, false, false, false};

    private int[] planet1;
    private int[] planet2;
    private int[] planet3;
    private int[] planet4;

    public Planets(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.planet1 = cardBuilder.planet1;
        this.planet2 = cardBuilder.planet2;
        this.planet3 = cardBuilder.planet3;
        this.planet4 = cardBuilder.planet4;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }
}
