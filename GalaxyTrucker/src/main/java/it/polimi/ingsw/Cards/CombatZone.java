package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

/**
 * Class that represent the card combat
 *
 * @author carlo
 */

public class CombatZone extends Card {

    private int daysLost;
    private int lossNumber;
    private Blow[] blows;
    private ElementType blowType;
    private ElementType lossType;

    public CombatZone(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.daysLost = cardBuilder.daysLost;
        this.lossNumber = cardBuilder.lossNumber;
        this.blows = cardBuilder.blows;
        this.blowType = cardBuilder.blowType;
        this.lossType = cardBuilder.lossType;

    }

    @Override

    public void resolve(Player[] players, FlightBoard flightBoard, FlightView flightView) {

    }

    /**
     * private method that is used in the resolve method.
     *
     * @return the player with the weakest engine power or fire power.
     */

    private Player calculateWeakestEnginePower() {


    }

    private Player calculateWeakestFirePower() {


    }


}
