package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * Class used to communicate with players during the game.
 *
 * @author carlo
 */

public class GameMessenger {

    private Map<Player, Socket> playerSocketMap = new HashMap<>();
    private Map<Player, ObjectOutputStream> playerObjectOutputStreamMap = new HashMap<>();
    private Map<Player, ObjectInputStream> playerObjectInputStreamMap = new HashMap<>();
    private List<Player> playerRMIList = new ArrayList<>();
    private Map<Player, DataContainer> playerDataContainerMap = new HashMap<>();

    public void addPlayerSocket(Player player, Socket socket, ObjectOutputStream outputStream, ObjectInputStream inputStream) {

        this.playerSocketMap.put(player, socket);
        this.playerObjectInputStreamMap.put(player, inputStream);
        this.playerObjectOutputStreamMap.put(player, outputStream);
        this.playerDataContainerMap.put(player, new DataContainer());

    }

    public void addPlayerRMI(Player player) {
        playerRMIList.add(player);
    }

    private void sendErrorMessage(String message, Player player) {
        DataContainer dataContainer = getPlayerContainer(player);
        dataContainer.setCommand("printMessage");
        dataContainer.setMessage(message);
        sendPlayerData(player);

    }

    public String getPlayerString(Player player) throws PlayerDisconnectedException {

        return getPlayerInput(player);

    }

    /**
     * @param player
     * @return the string that the player sent to the server
     */

    private String getPlayerInput(Player player) throws PlayerDisconnectedException {

        if (playerRMIList.contains(player)) {
            //RMI
            //TODO
        } else {
            try {
                return playerObjectInputStreamMap.get(player).readUTF();
            } catch (IOException e){

                System.err.println("Error while reading from client");
                e.printStackTrace();

                throw new PlayerDisconnectedException(player);

            }

        }

        return null;

    }

    public Collection<ObjectOutputStream> getPlayersOutputStreams() {
        return playerObjectOutputStreamMap.values();
    }

    public Collection<ObjectInputStream> getPlayerInputStreams() {
        return playerObjectInputStreamMap.values();
    }

    public int getPlayerInt(Player player) throws PlayerDisconnectedException {

        String input = getPlayerInput(player);
        int value;

        try {

            value = Integer.parseInt(input);

        } catch (NumberFormatException e) {

            sendErrorMessage("You didn't enter an integer! Please reenter it: ", player);
            return getPlayerInt(player);

        }

        return value;

    }

    public int[] getPlayerCoordinates(Player player) throws PlayerDisconnectedException {

        String input = getPlayerInput(player);
        int[] coordinates = new int[2];

        try {

            String[] parts = input.split(" ");

            coordinates[0] = Integer.parseInt(parts[0]);
            coordinates[1] = Integer.parseInt(parts[1]);

        } catch (NumberFormatException e) {

            sendErrorMessage("You didn't enter the coordinates in the correct syntax, please reenter them: ", player);
            return getPlayerCoordinates(player);

        }

        return coordinates;

    }

    public boolean getPlayerBoolean(Player player) throws PlayerDisconnectedException {

        String input = getPlayerInput(player);

        if (input.equalsIgnoreCase("yes")) {
            return true;
        } else if (input.equalsIgnoreCase("no")) {
            return false;
        } else {

            sendErrorMessage("You didn't enter the correct response, please reenter it (yes/no): ", player);
            return getPlayerBoolean(player);
        }
    }

    public Map<Player, Socket> getPlayerSocketMap() {
        return playerSocketMap;
    }

    /**
     * Set gamePhase for all players' client.
     * TODO: rmi
     */
    public void setGamePhaseToAll(GamePhase gamePhase) {
        // socket
        for (Player player : playerSocketMap.keySet()) {
            setPlayerGamePhase(player, gamePhase);
        }
        // RMI
    }

    /**
     * Sets gamePhase of player's client.
     * <p>
     * TODO: rmi
     *
     * @author Boti
     */
    public void setPlayerGamePhase(Player player, GamePhase gamePhase) {
        // socket
        if (playerSocketMap.containsKey(player)) {
            DataContainer dataContainer = getPlayerContainer(player);
            dataContainer.clearContainer();
            dataContainer.setCommand("setGamePhase");
            dataContainer.setGamePhase(gamePhase);
            sendPlayerData(player);
        }
        // RMI
    }

    /**
     * @param player
     * @return the player dataContainer
     */

    public DataContainer getPlayerContainer(Player player) {

        return playerDataContainerMap.get(player);

    }

    /**
     * Sends to the player his dataContainer, then clears the container.
     *
     * @param player
     */

    public void sendPlayerData(Player player) {

        if (playerRMIList.contains(player)) {
            //RMI
            //TODO
        } else {
            //socket

            try {

                playerObjectOutputStreamMap.get(player).writeObject(getPlayerContainer(player));
                playerObjectOutputStreamMap.get(player).flush();
                playerObjectOutputStreamMap.get(player).reset();

            } catch (IOException e) {
                System.err.println();
            } finally {
                playerDataContainerMap.get(player).clearContainer();
            }

        }
    }

    /**
     * End game for all players.
     * TODO rmi
     *
     * @author Boti
     */
    public void endGame() {
        // socket
        for (Player player : playerSocketMap.keySet()) {
            DataContainer dataContainer = getPlayerContainer(player);
            dataContainer.clearContainer();
            dataContainer.setCommand("endGame");
            sendPlayerData(player);
        }
        clearAllResources();

        // RMI

    }

    /**
     * Clears all players resources. Need to be used only at the end of the game, i.e,
     * not for a simple disconnection.
     */

    private void clearAllResources() {

        try {

            for (ObjectOutputStream outputStream : playerObjectOutputStreamMap.values()) {
                outputStream.close();
            }

            for (ObjectInputStream inputStream : playerObjectInputStreamMap.values()) {
                inputStream.close();
            }

            for (Socket socket : playerSocketMap.values()) {
                socket.close();
            }

        } catch (IOException e) {
            System.err.println("Error while closing all players resources");
        }

    }

    /**
     * Disconnects the player from the game.
     */
    public void disconnectPlayer(GameInformation gameInformation, Player player) {
        clearPlayerResources(player);
        gameInformation.getPlayerList().remove(player);
        gameInformation.getDisconnectedPlayerList().add(player);
    }

    /**
     * Clears the player resources. Used at the end of the game or when a player is
     * disconnected
     *
     * @param player
     */

    private void clearPlayerResources(Player player) {

        try {
            playerObjectOutputStreamMap.get(player).close();
            playerObjectInputStreamMap.get(player).close();
            playerSocketMap.get(player).close();
            playerObjectOutputStreamMap.remove(player);
            playerObjectInputStreamMap.remove(player);
            playerSocketMap.remove(player);

        } catch (IOException e) {
            System.err.println("Error while closing " + player.getNickName() + " resources");
        }

    }

    /**
     * Send message to all players.
     * Command is set to "printMessage".ú
     * TODO: rmi
     *
     * @author Boti
     */
    public void sendMessageToALl(String message) {
        // socket
        for (Player player : playerSocketMap.keySet()) {
            sendPlayerMessage(player, message);
        }

        // rmi
    }

    /**
     * Send a message to print to the given player's client.
     * Command is set to "printMessage".
     * <p>
     * TODO: rmi
     *
     * @author Boti
     */
    public void sendPlayerMessage(Player player, String message) {
        // socket
        if (playerSocketMap.containsKey(player)) {
            DataContainer dataContainer = getPlayerContainer(player);
            dataContainer.clearContainer();
            dataContainer.setCommand("printMessage");
            dataContainer.setMessage(message);
            sendPlayerData(player);
        }
        // RMI
    }

    /**
     * Reconnects player to the game.
     */
    public void reconnectPlayer(GameInformation gameInformation, Player player) {
        gameInformation.getDisconnectedPlayerList().remove(player);
        gameInformation.getPlayerList().add(player);
        sendMessageToALl(String.format("%s has been reconnected", player));
    }

}
