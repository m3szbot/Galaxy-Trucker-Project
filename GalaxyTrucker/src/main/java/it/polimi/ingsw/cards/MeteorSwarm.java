package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

/**
 * Class that represent the card meteorswarm
 * @author carlo
 */

public class MeteorSwarm extends Card{

    private Blow[] blows;
    private ElementType blowType;

    public MeteorSwarm(CardBuilder cardBuilder){

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.blows = cardBuilder.blows;
        this.blowType = cardBuilder.blowType;
        
    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView){

    }
}
