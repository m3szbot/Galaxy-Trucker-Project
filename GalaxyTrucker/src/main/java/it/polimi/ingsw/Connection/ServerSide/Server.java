package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMIListener;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketListener;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main server. It starts 2 concurrent threads which listens
 * for clients on both RMI and Socket.
 *
 * @author carlo
 */

public class Server {

    List<Game> games = new ArrayList<>();

    //TODO (interrupted functionality)
    //File interruptedGames = new File("/interruptedGames");

    private int gameCode;
    private Game currentStartingGame;
    private SocketListener socketListener;
    private RMIListener rmiListener;
    private ReentrantLock lock;
    private Color currentColor;
    private int portNumber;

    public Server(){
        this.gameCode = 0;
        this.currentStartingGame = new Game(gameCode);
        ClientMessenger.addGame(gameCode);
        this.socketListener = new SocketListener(this);
        this.rmiListener = new RMIListener(this);
        this.currentColor = Color.RED;
        this.portNumber = 5000;
    }

    public int getPort(){
        return portNumber;
    }

    public int getCurrentGameCode() {
        return gameCode;
    }

    public Color getCurrentColor() {

        switch (currentColor) {

            case Color.BLUE -> {
                currentColor = Color.GREEN;
                return Color.BLUE;
            }

            case Color.GREEN -> {
                currentColor = Color.YELLOW;
                return Color.GREEN;
            }
            case YELLOW -> {
                currentColor = Color.RED;
                return Color.YELLOW;
            }
            default -> {
                currentColor = Color.BLUE;
                return Color.RED;
            }
        }
    }

    public Game getCurrentStartingGame(){
        return currentStartingGame;
    }

    private void addGame(Game game) {
        games.add(game);
    }

    public ReentrantLock getLock() {
        return lock;
    }

    //Method overloading, the second one is used to add the first player to a

    public void addPlayerToCurrentStartingGame(Player player, ViewType viewType, ConnectionType connectionType) {

        currentStartingGame.addPlayer(player, viewType, connectionType, false);

    }

    public void addPlayerToCurrentStartingGame(Player player, ViewType viewType, ConnectionType connectionType, GameType gameType, int numberOfPlayers) {

        currentStartingGame.setNumberOfPlayers(numberOfPlayers);
        currentStartingGame.setGameType(gameType);
        currentStartingGame.addPlayer(player, viewType, connectionType, true);
        currentStartingGame.changeGameState(GameState.Starting);

    }

    public void startCurrentGame() {

        currentStartingGame.changeGameState(GameState.Playing);
        currentStartingGame.run();
        gameCode++;
        addGame(currentStartingGame);
        //new game
        currentStartingGame = new Game(gameCode);
        ClientMessenger.addGame(gameCode);
    }

    public static void main(String[] args){

        Server server = new Server();
        server.start();


    }

    public void start() {

        socketListener.run();
        rmiListener.run();

    }

}
