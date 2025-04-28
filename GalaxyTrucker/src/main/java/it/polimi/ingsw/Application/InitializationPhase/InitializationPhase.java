package it.polimi.ingsw.Application.InitializationPhase;

/**
 * initialization controller class (calls the initialization methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.Startable;

import java.io.IOException;

public class InitializationPhase implements Startable {

    public InitializationPhase() {
    }

    /**
     * start is to be called in the main in order to initialize the objects of the game
     *
     * @param gameInformation
     */
    @Override
    public void start(GameInformation gameInformation) {
        try {
            gameInformation.setUpCards(gameInformation.getGameType());
            gameInformation.setUpComponents();
        } catch (Exception e) {
            System.out.println("Setup error");
        }
        gameInformation.setUpFlightBoard();
    }

}