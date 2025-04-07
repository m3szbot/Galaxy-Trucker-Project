package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * Class that represent the card meteorswarm
 *
 * @author carlo
 */

public class MeteorSwarm extends Card implements SufferBlows{

    private Blow[] blows;
    private ElementType blowType;

    public MeteorSwarm(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;
        this.blows = cardBuilder.blows;
        this.blowType = cardBuilder.blowType;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        for(Player player : flightBoard.getPlayerOrderList()){

            hit(player, blows, blowType, flightView);
        }

    }
}
