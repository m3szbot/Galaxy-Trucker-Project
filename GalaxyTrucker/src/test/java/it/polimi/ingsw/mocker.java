package it.polimi.ingsw;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class mocker {

    /**
     * Set up a Game with gameType NORMALGAME, 1 player, GameMessenger and PlayerMessenger.
     *
     * @return
     */
    public static Game mockNormalGame1Player() {
        // find and keep correct setup order
        int gameCode = 0;
        Game game = new Game(gameCode);
        game.setGameType(GameType.NORMALGAME);
        Player player1 = new Player("Player1", Color.BLUE, game.getGameInformation());
        game.addPlayer(player1, true);
        game.setNumberOfPlayers(1);


        // setup messengers
        ClientMessenger.addGame(gameCode, game.getGameInformation());

        Socket clientSocket = new Socket();

        // TODO mock connection
        try {
            ObjectInputStream inputStream = new ObjectInputStream(clientSocket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            outputStream.flush();

            SocketDataExchanger sde = new SocketDataExchanger(clientSocket, inputStream, outputStream);
            ClientMessenger.getGameMessenger(gameCode).addPlayer(player1, sde);
        } catch (Exception e) {

        }

        game.setUpPhases();

        return game;
    }
}
