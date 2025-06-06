package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMIListener;
import it.polimi.ingsw.Connection.ServerSide.RMI.VirtualServer;
import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.Socket.SocketListener;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Game.GameState;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
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
    private List<String> nicknameList;

    public Server() {
        this.gameCode = 0;
        this.currentStartingGame = new Game(gameCode);
        VirtualServer virtualServer;
        ClientMessenger.addGame(gameCode);
        ClientMessenger.setCentralServer(this);

        try {

            virtualServer = new VirtualServer(this);
            this.rmiListener = new RMIListener(virtualServer);

        } catch (RemoteException e) {
            System.err.println("Error while setting up virtual server: rmi protocol unusable");
        }
        this.socketListener = new SocketListener(this);
        this.currentColor = Color.RED;
        this.portNumber = 5200;
        this.nicknameList = new ArrayList<>();
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

    /**
     * @param nickname
     * @return true if the nickName is already present
     */

    public boolean checkNickname(String nickname) {
        if (nicknameList.contains(nickname)) {
            return true;
        } else {
            return false;
        }
    }

    public void addNickName(String nickName) {
        synchronized (nicknameList) {
            nicknameList.add(nickName);
        }
    }

    public void removeNickName(String nickname) throws IllegalArgumentException {
        synchronized (nicknameList) {

            if (nicknameList.contains(nickname)) {
                nicknameList.remove(nickname);
            } else {
                throw new IllegalArgumentException("Nickname not existent");
            }
        }
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
        new Thread(currentStartingGame).start();
        gameCode++;
        addGame(currentStartingGame);
        //new game
        currentStartingGame = new Game(gameCode);

        ClientMessenger.addGame(gameCode);
        System.out.println("New game created");
    }

    private void addGame(Game game) {
        games.add(game);
    }

}
