package it.polimi.ingsw.Application;

/**
 * setup controller class (calls the setup methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Shipboard.Player;

import java.io.IOException;

public class SetUpPhase implements Startable {

    /**
     * start is the method to be called from main in order to set up the game, it operates differently based on who calls it (the first player or the subsequent ones)
     *
     * @param gameInformation
     */
    public void start(GameInformation gameInformation, SetUpView setUpView) throws IOException {
        if (gameInformation.getPlayerList().isEmpty()) {
            int maxNumberOfPlayers = setUpView.askMaxNumberOfPlayers();
            gameInformation.setUpPlayers(player, maxNumberOfPlayers);
            GameType gameType = setUpView.askGameType();
            gameInformation.setGameType(gameType);
        } else {
            gameInformation.addPlayers(player);
        }
        ViewType viewType = setUpView.askViewType();
        gameInformation.setViewType(viewType);
    }

}