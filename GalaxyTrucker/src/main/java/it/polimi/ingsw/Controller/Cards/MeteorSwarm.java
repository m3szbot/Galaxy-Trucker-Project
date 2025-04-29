package it.polimi.ingsw.Controller.Cards;

import it.polimi.ingsw.Controller.FlightPhase.FlightView;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Class that represent the card meteorswarm
 *
 * @author carlo
 */

public class MeteorSwarm extends Card implements SufferBlows {

    private Blow[] blows;
    private ElementType blowType;

    public MeteorSwarm(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.getCardLevel();
        this.cardName = cardBuilder.getCardName();
        this.blows = cardBuilder.getBlows();
        this.blowType = cardBuilder.getBlowType();

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        //leader rolling the dices for each blow
        for (int i = 0; i < blows.length; i++) {
            blows[i].rollDice();
        }

        for (Player player : flightBoard.getPlayerOrderList()) {

            hit(player, blows, blowType, flightBoard, flightView);
        }

    }
}
