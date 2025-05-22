package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * Class used to communicate with players during the game.
 * Contains the specific PLayerMessengers.
 * Used for all-player communications.
 *
 * @author carlo
 */

public class GameMessenger {
    private Map<Player, PlayerMessenger> playerMessengerMap = new HashMap<>();

    /**
     * Add player to the playerMessengerMap with its associated PlayerMessenger.
     */
    public void addPlayer(Player player, ConnectionType connectionType, SocketDataExchanger dataExchanger) {
        PlayerMessenger playerMessenger = new PlayerMessenger(player, connectionType, dataExchanger);
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
    }


    /**
     * TODO
     * Disconnects the player from the game.
     */
    public void disconnectPlayer(GameInformation gameInformation, Player player) {
        gameInformation.getPlayerList().remove(player);
        gameInformation.getDisconnectedPlayerList().add(player);
        playerMessengerMap.get(player).clearPlayerResources();
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
     * Send message to all players.
     */
    public void sendMessageToAll(String message) {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.printMessage(message);
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
