package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class used to communicate with players during the game.
 *
 * @author carlo
 */

public class GameMessenger {

    private Map<Player, Socket> playerSocketMap = new HashMap<>();
    private List<Player> playerRMIList = new ArrayList<>();
    private Map<Player, DataContainer> playerDataContainerMap = new HashMap<>();

    /**
     * To call when a player is added to the game
     *
     * @param player
     * @param socket null if the player chose RMI
     */

    public void addPlayerSocket(Player player, Socket socket) {

        this.playerSocketMap.put(player, socket);
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

        String input = getPlayerInput(player);
        return input;

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
                return (new DataInputStream((playerSocketMap.get(player)).getInputStream())).readUTF();
            } catch (IOException e) {

                System.err.println("Error while reading from client");
                e.printStackTrace();

                //closing player socket

                try {
                    playerSocketMap.get(player).close();
                } catch (IOException ex) {
                    System.err.println("Error while closing disconnected player socket");
                }

                playerSocketMap.remove(player);
                playerDataContainerMap.remove(player);

                throw new PlayerDisconnectedException(player);

            }

        }

        return null;

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
     * Send a message to print to the given player's client.
     * Command is set to "printMessage".
     *
     * @author Boti
     */
    public void sendPlayerMessage(Player player, String message) {
        DataContainer dataContainer = getPlayerContainer(player);
        dataContainer.clearContainer();
        dataContainer.setCommand("printMessage");
        dataContainer.setMessage(message);
        sendPlayerData(player);
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

        Socket playerSocket = playerSocketMap.get(player);

        if (playerRMIList.contains(player)) {
            //RMI
            //TODO
        } else {
            //socket

            try {

                (new ObjectOutputStream(playerSocket.getOutputStream())).writeObject(playerDataContainerMap.get(player));

            } catch (IOException e) {
                System.err.println();
            } finally {
                playerDataContainerMap.get(player).clearContainer();
            }

        }
    }

}
