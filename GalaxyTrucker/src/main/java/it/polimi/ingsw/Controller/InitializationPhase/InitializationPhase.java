package it.polimi.ingsw.Controller.InitializationPhase;

/**
 * initialization controller class (calls the initialization methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Controller.Game.Startable;
import it.polimi.ingsw.Model.GameInformation.GameInformation;

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