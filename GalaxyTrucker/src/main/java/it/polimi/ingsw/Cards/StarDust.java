package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Application.FlightView;
import it.polimi.ingsw.FlightBoard.FlightBoard;
import it.polimi.ingsw.Shipboard.Player;

import java.util.List;

//check that the count external junctions method in shipBoard does what it is supposed to do

/**
 * class that represent the card StarDust.
 *
 * @author carlo
 */

public class StarDust extends Card implements Movable{


    public StarDust(CardBuilder cardBuilder) {

        this.cardLevel = cardBuilder.cardLevel;
        this.cardName = cardBuilder.cardName;

    }

    @Override

    public void resolve(FlightBoard flightBoard, FlightView flightView) {

        List<Player> players = flightBoard.getPlayerOrderList();

        for(int i = flightBoard.getPlayerOrderList().size() - 1; i >= 0; i--){

            changePlayerPosition(players.get(i), -players.get(i).getShipBoard().countExternalJunctions(), flightBoard);

        }

    }

}
