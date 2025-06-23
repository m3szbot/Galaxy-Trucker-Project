package it.polimi.ingsw;

import it.polimi.ingsw.Connection.ClientSide.RMI.VirtualClient;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.rmi.RemoteException;

public abstract class mocker {

    /**
     * Set up a Game with gameType NORMALGAME, 1 player, GameMessenger and PlayerMessenger.
     *
     * @return
     */
    public static Game mockNormalGame1Player() {
        // find and keep correct setup order
        // setup Game
        int gameCode = 0;
        Game game = new Game(gameCode);
        game.setGameType(GameType.NORMALGAME);
        game.setNumberOfPlayers(1);

        // add 1 player
        Player player1 = new Player("Player1", Color.BLUE, game.getGameInformation());
        game.addPlayer(player1, true);


        // setup messengers
        ClientMessenger.addGame(gameCode, game.getGameInformation());
        ClientInfo clientInfo = new ClientInfo();
        clientInfo.setViewType(ViewType.TUI);
        clientInfo.setGameCode(gameCode);
        try {
            VirtualClient virtualClient = new VirtualClient(clientInfo);
            ClientMessenger.getGameMessenger(gameCode).addPlayer(player1, virtualClient);
        } catch (RemoteException ex) {
        }

        game.setUpPhases();

        return game;
    }
}
