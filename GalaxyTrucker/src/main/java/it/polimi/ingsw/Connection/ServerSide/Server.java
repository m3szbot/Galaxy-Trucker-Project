package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMIListener;
import it.polimi.ingsw.Connection.ServerSide.RMI.VirtualServer;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketListener;
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
    private List<String> bannedIPs;

    public Server() {
        this.gameCode = 0;
        this.currentStartingGame = new Game(gameCode);
        VirtualServer virtualServer;
        ClientMessenger.addGame(gameCode, currentStartingGame.getGameInformation());
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

    /**
     * Starts the two listening threads on RMI or Socket.
     */

    public void start() {
        try {
            System.out.printf("Server started with IP:\n%s\n", InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        new Thread(socketListener).start();
        new Thread(rmiListener).start();

    }

    /**
     *
     * @param gameCode
     * @return the game with the game code passed as parameter
     * @throws IndexOutOfBoundsException
     */

    public Game getGame(int gameCode) throws IndexOutOfBoundsException {

        return games.get(gameCode);

    }

    /**
     *
     * @return the port number of the server
     */

    public int getPort() {
        return portNumber;
    }

    /**
     *
     * @return the current game code.
     */

    public int getCurrentGameCode() {
        return gameCode;
    }

    /**
     *
     * @return the current color. The colors are looped.
     */

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

    /**
     *
     * @return the current starting game
     */

    public Game getCurrentStartingGame() {
        return currentStartingGame;
    }

    /**
     *
     * @return the lock which is used to join a game. The lock forbid a player
     * to join a game if another one is already doing so.
     */

    public ReentrantLock getLock() {
        return lock;
    }

    /**
     * Adds the player passed as parameter to the current starting game
     * @param player
     */

    public void addPlayerToCurrentStartingGame(Player player) {

        currentStartingGame.addPlayer(player, false);

    }

    /**
     *
     * @param nickname
     * @return true if the nickName is already present
     */

    public boolean checkNickname(String nickname){
       if(nicknameList.contains(nickname)){
           return true;
       }
       else{
           return false;
       }
    }

    /**
     * Adds the nickname passed as parameter to the nickname list of the server. The nickname
     * list contains the nickname of all the players currently connected.
     * @param nickName
     */

    public void addNickName(String nickName){
        synchronized (nicknameList) {
            nicknameList.add(nickName);
        }
    }

    /**
     * Removes a nickname from the nickname list
     * @param nickname
     * @throws IllegalArgumentException
     */

    public void removeNickName(String nickname) throws IllegalArgumentException{
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

    /**
     * Starts the current starting game. To be called only if the current starting game is full
     */

    public void startCurrentGame() {

        currentStartingGame.changeGameState(GameState.Playing);
        currentStartingGame.setUpPhases();
        new Thread(currentStartingGame).start();
        gameCode++;
        addGame(currentStartingGame);
        //new game
        currentStartingGame = new Game(gameCode);

        ClientMessenger.addGame(gameCode, currentStartingGame.getGameInformation());
        System.out.println("New game created");
    }

    private void addGame(Game game) {
        games.add(game);
    }

}
