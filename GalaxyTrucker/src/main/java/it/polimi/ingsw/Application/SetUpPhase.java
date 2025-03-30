package it.polimi.ingsw.Application;

/**
 * setup controller class (calls the setup methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.shipboard.Player;

public class SetUpPhase {
    Player player;

    /**
     * start is the method to be called from main in order to set up the game, it operates differently based on who calls it (the first player or the subsequent ones)
     *
     * @param gameInformation
     */
    public void start(GameInformation gameInformation) {
        if (gameInformation.getPlayerList().isEmpty()) {
            int maxNumberOfPlayers = askMaxNumberOfPlayers(setUpView);
            gameInformation.setUpPlayers(player, maxNumberOfPlayers);
            GameType gameType = askGameType(setUpView);
            gameInformation.setGameType(gameType);
        } else {
            gameInformation.addPlayers(player);
        }
        ViewType viewType = askViewType(setUpView);
        gameInformation.setViewType(viewType);

    }

    /**
     * calls a method in setUpView that asks the first user the max # players
     *
     * @param setUpView
     * @return int
     */
    public int askMaxNumberOfPlayers(SetUpView setUpView) {
        return setUpView.askMaxNumberOfPlayers();
    }

    /**
     * calls a method in setUpView that asks the first user the game type
     *
     * @param setUpView
     * @return GameType
     */
    public GameType askGameType(SetUpView setUpView) {
        return setUpView.askGameType();
    }

    /**
     * calls a method in setUpView that asks every user the view Type
     *
     * @param setUpView
     * @return viewType
     */
    public ViewType askViewType(SetUpView setUpView) {
        return setUpView.askViewType();
    }

}
