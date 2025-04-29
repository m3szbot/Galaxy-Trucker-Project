package it.polimi.ingsw.Controller.SetUpPhase;

/**
 * setup controller class (calls the setup methods)
 *
 * @author Ludo
 */

import it.polimi.ingsw.Application.*;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.Startable;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;

public class SetUpPhase implements Startable {

    public SetUpPhase() {
    }

    /**
     * start is the method to be called from main in order to set up the game, it operates differently based on who calls it (the first player or the subsequent ones)
     */
    @Override
    public void start(GameInformation gameInformation) throws IOException {
        //Per lasciare questo thread per ciascun giocatore, ad ogni connessione di un giocatore
        //dobbiamo chiamare SetUpPhase.start(...);
        SetUpView setUpView = new SetUpView();
        Thread thread = new Thread(() -> {
            String message;
            message = "\"Insert your nickname:\"";
            String nickName = null;
            try {
                nickName = setUpView.askNickName(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            message = "\"What type of view do you want to play with? (CLI, GUI)\"";
            ViewType viewType = setUpView.askViewType(message);
            if (gameInformation.getPlayerList().isEmpty()) {
                Player player = new Player(nickName, Color.RED, gameInformation);
                message = "\"How many players do you want the match to be played with?\"";
                int maxNumberOfPlayers = 0;
                try {
                    maxNumberOfPlayers = setUpView.askMaxNumberOfPlayers(player, message);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //gameInformation.setUpPlayers(player, maxNumberOfPlayers);
                message = "\"What type of game do you want to play? (TestGame, Normalgame) \"";
                GameType gameType = setUpView.askGameType(message);
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
        });
        thread.start();
    }
}