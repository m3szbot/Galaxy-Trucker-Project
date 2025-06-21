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
 * the client
 *
 * @author carlo
 */

public class ClientMessenger {

    private static Map<Integer, GameMessenger> gameMessengerMap = new HashMap<>();
    private static Server centralServer;
    private static Map<String, PlayerLobbyMessenger> playersInLobbyMessengerMap = new HashMap<>();

    public static void addPlayerInLobby(String nickName, SocketDataExchanger socketDataExchanger){
        playersInLobbyMessengerMap.put(nickName, new PlayerLobbyMessenger(socketDataExchanger, nickName));
    }

    public static void addPlayerInLobby(String nickname, ClientRemoteInterface virtualClient){
        playersInLobbyMessengerMap.put(nickname, new PlayerLobbyMessenger(virtualClient, nickname));
    }

    public static PlayerLobbyMessenger getPlayerLobbyMessenger(String nickname){
        return playersInLobbyMessengerMap.get(nickname);
    }

    public static void removePlayerLobbyMessenger(String nickname){

        playersInLobbyMessengerMap.remove(nickname);
    }

    /**
     * when a new game is started, addGame must be called to add the gameMessenger for
     * the game
     * @param gameCode of the added game
     */

    public static void addGame(int gameCode, GameInformation gameInformation){

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

    public static GameMessenger getGameMessenger(int gameCode){
        return gameMessengerMap.get(gameCode);
    }

    public static void endGame(int gameCode){
        gameMessengerMap.remove(gameCode);
    }

}
