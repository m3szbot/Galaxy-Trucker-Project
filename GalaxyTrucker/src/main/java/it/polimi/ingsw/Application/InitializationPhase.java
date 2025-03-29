package it.polimi.ingsw.Application;

import java.io.IOException;

public class InitializationPhase {

    public void start(GameInformation gameInformation) throws IOException {
        gameInformation.setUpCards(gameInformation.getGameType());
        gameInformation.setUpBank();
        gameInformation.setUpComponents();
        gameInformation.setUpFlightBoard();
    }

}
