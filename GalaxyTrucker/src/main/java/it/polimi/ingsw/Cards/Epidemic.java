package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card epidemic
 *
 * @author carlo
 */

public class Epidemic extends Card {

    public Epidemic(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {


    }

    private void removeAdjacentAstronauts(Player player) {


    }


}
