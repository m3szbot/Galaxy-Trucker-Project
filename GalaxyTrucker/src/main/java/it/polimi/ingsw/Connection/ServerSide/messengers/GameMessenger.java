package it.polimi.ingsw.Connection.ServerSide.messengers;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to communicate with players during the game.
 * Contains the specific PLayerMessengers.
 * Used for all-player communications.
 *
 * @author carlo, boti
 */

public class GameMessenger {
    private Map<Player, PlayerMessenger> playerMessengerMap = new ConcurrentHashMap<>();
    private int gameCode;
    private GameInformation gameInformation;

    public GameMessenger(int gameCode, GameInformation gameInformation) {
        this.gameCode = gameCode;
        this.gameInformation = gameInformation;
    }

    /**
     * @param player
     * @return true if the playerMessenger is present, false otherwise
     */

    public boolean checkPlayerMessengerPresence(Player player) {
        if (playerMessengerMap.containsKey(player)) {
            return true;
        } else {
            return false;
        }
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

    public List<Player> getConnectedPlayers() {

        return gameInformation.getPlayerList();

    }

    /**
     * Add RMI player to GameMessenger and create its associated PlayerMessenger.
     */
    public void addPlayer(Player player, ClientRemoteInterface virtualClient) {
        PlayerMessenger playerMessenger = new PlayerMessenger(player, virtualClient, gameCode);
        playerMessengerMap.put(player, playerMessenger);
    }

    /**
     * Attention: always check if null before using playerMessenger, to avoid NullPointerException!
     *
     * @return PlayerMessenger associated to the given player, or null if the player is not connected.
     */
    public PlayerMessenger getPlayerMessenger(Player player) {
        if (!playerMessengerMap.containsKey(player))
            System.err.printf("Attention! PlayerMessenger is null for %s\n", player.getColouredNickName());

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
    public void disconnectPlayer(Player player) {

        if (player.getIsConnected()) {

            gameInformation.disconnectPlayerInGameInformation(player);

            if (gameInformation.getFlightBoard().isInFlight(player)) {
                gameInformation.getFlightBoard().eliminatePlayer(player);
            }

            if (!playerMessengerMap.containsKey(player)) {
                System.err.println("Critical error: player " + player.getNickName() + " is not in playerMessenger map during disconnection method call");
                return;
            }

            playerMessengerMap.get(player).clearPlayerResources();
            playerMessengerMap.remove(player);
            System.out.println(String.format("Player %s disconnected!", player.getColouredNickName()));
            sendMessageToAll(String.format("Player %s disconnected!", player.getColouredNickName()));
            ClientMessenger.getCentralServer().removeNickName(player.getNickName());
            player.disconnect();

        }
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
     * Print the flightBoard to all players.
     */
    public void sendFlightBoardToAll(FlightBoard flightBoard) {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.printFlightBoard(flightBoard);
        }
    }

    /**
     * Print a card to all players.
     */
    public void sendCardToAll(Card card) {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.printCard(card);
        }
    }

    /**
     * Clears resources of all players. To call at server shutdown?
     */

    private void clearResourcesToAll() {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.clearPlayerResources();
        }
    }

    public void exitLobby() {
        for (PlayerMessenger playerMessenger : playerMessengerMap.values()) {
            playerMessenger.exitLobby();
        }
    }

}
