package it.polimi.ingsw.Application;

/**
 * setup controller class (calls the setup methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;

import java.io.IOException;

public class SetUpPhase implements Startable {

    public SetUpPhase() {
    }

    /**
     * start is the method to be called from main in order to set up the game, it operates differently based on who calls it (the first player or the subsequent ones)
     *
     * @param gameInformation
     */
    @Override
    public void start(GameInformation gameInformation, SetUpView setUpView) throws IOException {
        //credo che qui vada lanciato un thread per ciascun player
        //e poi un thread per la gestione concorrente degli imput?
        String nickName = setUpView.askNickName();
        ViewType viewType = setUpView.askViewType();
        if (gameInformation.getPlayerList().isEmpty()) {
            Player player = new Player(nickName, Color.RED, gameInformation);
            int maxNumberOfPlayers = setUpView.askMaxNumberOfPlayers();
            gameInformation.setUpPlayers(player, maxNumberOfPlayers);
            GameType gameType = setUpView.askGameType();
            gameInformation.setGameType(gameType);
            gameInformation.setPlayerViewType(player, viewType);
        } else {
            Color color = gameInformation.getPlayerList().getLast().getColor();
            if (color == Color.RED) {
                color = Color.BLUE;
            } else if (color == Color.BLUE) {
                color = Color.YELLOW;
            } else if (color == Color.YELLOW) {
                color = Color.GREEN;
            }
            Player player = new Player(nickName, color, gameInformation);
            gameInformation.addPlayers(player);
            gameInformation.setPlayerViewType(player, viewType);
        }
    }

}