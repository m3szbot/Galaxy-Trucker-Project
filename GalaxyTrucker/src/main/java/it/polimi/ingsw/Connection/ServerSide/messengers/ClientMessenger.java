package it.polimi.ingsw.Connection.ServerSide.messengers;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains all the gameMessenger. For each game there is
 * a gameMessenger associated with it, which is used to communicate with
 * the players.
 *
 * @author carlo
 */

public abstract class ClientMessenger {

    private static Map<Integer, GameMessenger> gameMessengerMap = new HashMap<>();
    private static Server centralServer;
    private static Map<String, PlayerLobbyMessenger> playersInLobbyMessengerMap = new HashMap<>();

    /**
     * Creates a new playerLobbyMessenger for the nickname passed as parameter. The playerLobbyMessenger
     * is then used by the server to communicate with that specific player while in lobby.
     * @param nickName
     * @param socketDataExchanger
     */

    public static void addPlayerInLobby(String nickName, SocketDataExchanger socketDataExchanger) {
        playersInLobbyMessengerMap.put(nickName, new PlayerLobbyMessenger(socketDataExchanger, nickName));
    }

    public static void addPlayerInLobby(String nickname, ClientRemoteInterface virtualClient) {
        playersInLobbyMessengerMap.put(nickname, new PlayerLobbyMessenger(virtualClient, nickname));
    }

    /**
     *
     * @param nickname
     * @return playerLobbyMessenger of the player with the nickname passed as parameter
     */

    public static PlayerLobbyMessenger getPlayerLobbyMessenger(String nickname) {
        return playersInLobbyMessengerMap.get(nickname);
    }

    /**
     * To use when the player enters a game, this because during the game the server uses another
     * messenger to communicate with the player.
     * @param nickname
     */

    public static void removePlayerLobbyMessenger(String nickname) {

        playersInLobbyMessengerMap.remove(nickname);
    }

    /**
     * Create the gameMessenger associated with the game passed as parameter. The
     * gameMessenger contains all the messengers of the players in game.
     *
     * @param gameCode of the added game
     */

    public static void addGame(int gameCode, GameInformation gameInformation) {

        gameMessengerMap.put(gameCode, new GameMessenger(gameCode, gameInformation));

    }

    public static Server getCentralServer() {
        return centralServer;
    }

    public static void setCentralServer(Server centralServer) {
        ClientMessenger.centralServer = centralServer;
    }

    /**
     * @param gameCode
     * @return gameMessenger of the current game
     */

    public static GameMessenger getGameMessenger(int gameCode) {
        return gameMessengerMap.get(gameCode);
    }

    /**
     * Removes the gameMessenger associated with the gameCode passed as parameters
     * @param gameCode
     */

    public static void endGame(int gameCode) {
        gameMessengerMap.remove(gameCode);
    }

}
