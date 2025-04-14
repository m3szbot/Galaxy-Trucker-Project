package it.polimi.ingsw.Application.FlightPhase;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.FlightBoard.FlightBoard;

public class FlightPhase {

    public void start(GameInformation gameInformation, FlightView flightView){

        FlightBoard flightBoard = gameInformation.getFlightBoard();
        int numberOfCards;

        if(gameInformation.getGameType() == GameType.TestGame){
            numberOfCards = 8;
        }
        else{
            numberOfCards = 12;
        }

        for(int i = 0; i < numberOfCards; i++){

            flightBoard.getNewCard().resolve(flightBoard, flightView);

        }

    }

}
