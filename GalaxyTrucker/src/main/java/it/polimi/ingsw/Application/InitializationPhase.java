package it.polimi.ingsw.Application;

/**
 * initialization controller class (calls the initialization methods)
 *
 * @author Ludo
 */

import java.io.IOException;

public class InitializationPhase {

    /**
     * start is to be called in the main in order to initialize the objects of the game
     *
     * @param gameInformation
     * @throws IOException
     */
    public void start(GameInformation gameInformation) throws IOException {
        gameInformation.setUpCards(gameInformation.getGameType());
        gameInformation.setUpBank();
        gameInformation.setUpComponents();
        gameInformation.setUpFlightBoard();
    }

}
