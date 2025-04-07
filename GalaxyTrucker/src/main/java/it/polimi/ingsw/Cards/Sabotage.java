package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

/**
 * class that represent the card sabotage
 *
 * @author carlo
 */

public class Sabotage extends Card implements SmallestCrew{

    public Sabotage(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

    }

    private void destroyRandomComponent(Player player) {


    }
}
