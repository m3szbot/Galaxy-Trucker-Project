package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMIListener;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketListener;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private ReentrantLock lock = new ReentrantLock();
    private Color currentColor;
    private int portNumber;

    public Server() {
        this.gameCode = 0;
        this.currentStartingGame = new Game(gameCode);
        ClientMessenger.addGame(gameCode);
        this.socketListener = new SocketListener(this);
        this.rmiListener = new RMIListener(this);
        this.currentColor = Color.RED;
        this.portNumber = 5200;
    }

    public static void main(String[] args) {

        Server server = new Server();
        server.start();


    }

    public void start() {
        try {
            System.out.printf("Server started with IP:\n%s\n", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        new Thread(socketListener).start();
        new Thread(rmiListener).start();

    }

    public Game getGame(int gameCode) throws IndexOutOfBoundsException {

        return games.get(gameCode);

    }

    public int getPort() {
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

    public Game getCurrentStartingGame() {
        return currentStartingGame;
    }

    //Method overloading, the second one is used to add the first player to a

    public ReentrantLock getLock() {
        return lock;
    }

    public void addPlayerToCurrentStartingGame(Player player) {

        currentStartingGame.addPlayer(player, false);

    }

    public void addPlayerToCurrentStartingGame(Player player, GameType gameType, int numberOfPlayers) {

        currentStartingGame.setNumberOfPlayers(numberOfPlayers);
        currentStartingGame.setGameType(gameType);
        currentStartingGame.addPlayer(player, true);
        currentStartingGame.changeGameState(GameState.Starting);

    }

    public void startCurrentGame() {

        currentStartingGame.changeGameState(GameState.Playing);
        currentStartingGame.setUpPhases();
        currentStartingGame.run();
        gameCode++;
        addGame(currentStartingGame);
        //new game
        currentStartingGame = new Game(gameCode);
        ClientMessenger.addGame(gameCode);
    }

    private void addGame(Game game) {
        games.add(game);
    }

}
