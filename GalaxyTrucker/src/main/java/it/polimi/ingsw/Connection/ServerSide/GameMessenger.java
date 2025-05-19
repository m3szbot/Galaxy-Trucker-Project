package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to communicate with players during the game.
 *
 * @author carlo
 */

public class GameMessenger {

    private ConcurrentHashMap<Player, DataExchanger> dataExchangerMap = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Player, DataContainer> playerDataContainerMap = new ConcurrentHashMap<>();

    public List<Player> getPlayersSocket(){

        List<Player> players = new ArrayList<>();

        for(Player player: dataExchangerMap.keySet()){
            if(dataExchangerMap.get(player).getConnectionType() == ConnectionType.SOCKET){
                players.add(player);
            }
        }

        return players;

    }

    public void addPlayer(Player player, DataExchanger dataExchanger){
        playerDataContainerMap.put(player, new DataContainer());
        dataExchangerMap.put(player, dataExchanger);
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

        try {

            return dataExchangerMap.get(player).receiveMessage(true);
        } catch (IOException e) {

            System.err.println("Error while obtaining data from client");
            throw new PlayerDisconnectedException(player);

        }

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

    /**
     * Set gamePhase for all players' client.
     */
    public void setGamePhaseToAll(GamePhase gamePhase) {

        for (Player player : dataExchangerMap.keySet()) {
            setPlayerGamePhase(player, gamePhase);
        }

    }

    /**
     * Sets gamePhase of player's client.
     * <p>
     */
    public void setPlayerGamePhase(Player player, GamePhase gamePhase) {

        DataContainer dataContainer = getPlayerContainer(player);
        dataContainer.setCommand("setGamePhase");
        dataContainer.setGamePhase(gamePhase);
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

        try {

            dataExchangerMap.get(player).sendDataContainer(getPlayerContainer(player));

        } catch (IOException e) {
            System.err.println("Error while sending dataContainer to " + player.getNickName());
        }

    }

    /**
     * End game for all players.
     */
    public void endGame() {
        // socket
        for (Player player : dataExchangerMap.keySet()) {

            DataContainer dataContainer = getPlayerContainer(player);
            dataContainer.setCommand("endGame");
            sendPlayerData(player);
        }
        clearAllResources();


    }

    /**
     * Clears all players resources. Need to be used only at the end of the game, i.e,
     * not for a simple disconnection.
     */

    private void clearAllResources() {

        try {

            for(DataExchanger dataExchanger: dataExchangerMap.values()){
                dataExchanger.closeResources();
            }

        } catch (IOException e) {
            System.err.println("Error while closing all players resources");
        }

    }

    /**
     * Disconnects the player from the game.
     */
    public void disconnectPlayer(GameInformation gameInformation, Player player) {
        gameInformation.getPlayerList().remove(player);
        gameInformation.getDisconnectedPlayerList().add(player);
        clearPlayerResources(player);
    }

    /**
     * Clears the player resources. Used at the end of the game or when a player is
     * disconnected
     *
     * @param player
     */

    private void clearPlayerResources(Player player) {

        try {

            dataExchangerMap.get(player).closeResources();

        } catch (IOException e) {
            System.err.println("Error while closing " + player.getNickName() + " resources");
        }

    }

    /**
     * Send message to all players.
     * Command is set to "printMessage".ú
     */
    public void sendMessageToAll(String message) {
        for (Player player : dataExchangerMap.keySet()) {
            sendPlayerMessage(player, message);
        }

    }

    /**
     * Send a message to print to the given player's client.
     * Command is set to "printMessage".
     */
    public void sendPlayerMessage(Player player, String message) {

        DataContainer dataContainer = getPlayerContainer(player);
        dataContainer.setCommand("printMessage");
        dataContainer.setMessage(message);
        sendPlayerData(player);

    }

    /**
     * Reconnects player to the game.
     */
    public void reconnectPlayer(GameInformation gameInformation, Player player) {
        gameInformation.getDisconnectedPlayerList().remove(player);
        gameInformation.getPlayerList().add(player);
        sendMessageToAll(String.format("%s has been reconnected", player));
    }

}
