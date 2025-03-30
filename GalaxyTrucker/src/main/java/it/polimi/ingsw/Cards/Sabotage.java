package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * class that represent the card sabotage
 *
 * @author carlo
 */

public class Sabotage extends Card {

    public Sabotage(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }

    private void destroyRandomComponent(Player player) {


    }
}
