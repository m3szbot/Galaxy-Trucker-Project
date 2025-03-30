package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card openspace
 *
 * @author carlo
 */

public class OpenSpace extends Card {

    public OpenSpace(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }
}
