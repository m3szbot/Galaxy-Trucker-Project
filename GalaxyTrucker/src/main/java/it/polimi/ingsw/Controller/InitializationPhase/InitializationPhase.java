package it.polimi.ingsw.Controller.InitializationPhase;

/**
 * initialization controller class (calls the initialization methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Controller.Phase;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;

public class InitializationPhase extends Phase {



    public InitializationPhase(GameInformation gameInformation) {
        super(gameInformation);
    }

    /**
     * start is to be called in the main in order to initialize the objects of the game
     */
    @Override
    public void start() {

        gameInformation.setUpGameInformation(gameInformation.getGameType(), gameInformation.getMaxNumberOfPlayers());
        setGamePhaseToClientServer(GamePhase.Initialization);

        System.out.println("Initialization phase ended");
    }
}