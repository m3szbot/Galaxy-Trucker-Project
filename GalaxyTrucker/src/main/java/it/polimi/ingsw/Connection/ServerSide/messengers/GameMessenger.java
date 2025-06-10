package it.polimi.ingsw.Connection.ServerSide.messengers;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Class used to communicate with players during the game.
 * Contains the specific PLayerMessengers.
 * Used for all-player communications.
 *
 * @author carlo, boti
 */

public class GameMessenger {
    private Map<Player, PlayerMessenger> playerMessengerMap = new HashMap<>();
    private int gameCode;

    public GameMessenger(int gameCode) {
        this.gameCode = gameCode;
    }

    /**
     * Add socket player to GameMessenger and create its associated PlayerMessenger.
     */
    public void addPlayer(Player player, SocketDataExchanger dataExchanger) {
        PlayerMessenger playerMessenger = new PlayerMessenger(player, dataExchanger, gameCode);
        playerMessengerMap.put(player, playerMessenger);
    }

    /**
     * @return the players that are connected to the game
     */

    public Set<Player> getConnectedPlayers() {

        return playerMessengerMap.keySet();

    }

    /**
     * Add RMI player to GameMessenger and create its associated PlayerMessenger.
     */
    public void addPlayer(Player player, ClientRemoteInterface virtualClient) {
        PlayerMessenger playerMessenger = new PlayerMessenger(player, virtualClient, gameCode);
        playerMessengerMap.put(player, playerMessenger);
    }

    /**
     * @return PlayerMessenger associated to the given player.
     */
    public PlayerMessenger getPlayerMessenger(Player player) {
        return playerMessengerMap.get(player);
    }


    /**
     * Set gamePhase for all players' client.
     */
    public void setGamePhaseToAll(GamePhase gamePhase) {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values())
            playerMessenger.setGamePhase(gamePhase);
    }

    /**
     * End game for all players.
     */
    public void endGameToAll() {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.endGame();
        }
        for (Player player : playerMessengerMap.keySet()) {
            ClientMessenger.getCentralServer().removeNickName(player.getNickName());
        }

    }


    /**
     * Disconnects the player from the game.
     */
    public void disconnectPlayer(GameInformation gameInformation, Player player) {

        gameInformation.disconnectPlayerInGameInformation(player);

        if (gameInformation.getFlightBoard().getPlayerOrderList().contains(player)) {
            gameInformation.getFlightBoard().eliminatePlayer(player);
        }
        playerMessengerMap.get(player).clearPlayerResources();
        playerMessengerMap.remove(player);
        ClientMessenger.getCentralServer().removeNickName(player.getNickName());
        System.out.println(String.format("Player %s disconnected!", player.getNickName()));
        sendMessageToAll(String.format("Player %s disconnected!", player.getNickName()));
    }

    /**
     * Send message to all players.
     */
    public void sendMessageToAll(String message) {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.printMessage(message);
        }
    }

    /**
     * TODO
     * Clears resources of all players. To call at server shutdown?
     */

    private void clearResourcesToAll() {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.clearPlayerResources();
        }
    }

    /**
     * TODO reopen resources
     * Reconnects player to the game.
     */
    public void reconnectPlayer(GameInformation gameInformation, Player player) {
        gameInformation.getDisconnectedPlayerList().remove(player);
        gameInformation.getPlayerList().add(player);
        sendMessageToAll(String.format("%s has been reconnected", player));
    }

    /**
     * Sends directly the message in a shortCut manner.
     *
     * @param message
     * @author carlo
     */

    public void sendShortCutMessageToAll(String message, boolean onlySocket) {

        if (onlySocket) {

            for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
                if (playerMessenger.getConnectionType() == ConnectionType.SOCKET) {
                    playerMessenger.sendShortCutMessage(message);
                }
            }

        } else {
            for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
                playerMessenger.sendShortCutMessage(message);
            }
        }
    }

    public Boolean isPlayerConnected(Player player, GameInformation gameInformation) {
        if (gameInformation.getDisconnectedPlayerList().contains(player)) {
            return false;
        } else {
            return true;
        }
    }
}
