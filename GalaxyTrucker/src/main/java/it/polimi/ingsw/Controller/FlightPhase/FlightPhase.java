package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.FlightView.FlightView;

public class FlightPhase implements Startable {

    public void start(GameInformation gameInformation) {

        FlightView flightView = new FlightView(gameInformation);
        FlightBoard flightBoard = gameInformation.getFlightBoard();
        int numberOfCards;

        if (gameInformation.getGameType() == GameType.TestGame) {
            numberOfCards = 8;
        } else {
            numberOfCards = 12;
        }

        for (int i = 0; i < numberOfCards; i++) {

            flightBoard.getNewCard().resolve(flightBoard, flightView);

        }

    }

}
