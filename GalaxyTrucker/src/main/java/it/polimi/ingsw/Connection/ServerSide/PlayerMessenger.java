package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ClientSide.ClientServerInvokableMethods;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.ViewServerInvokableMethods;

import java.io.IOException;

/**
 * Messenger service associated to the player.
 * Used for player specific controller communications.
 * Implements both socket and RMI method calls.
 *
 * @author Boti
 */
public class PlayerMessenger implements ViewServerInvokableMethods, ClientServerInvokableMethods {

    private Player player;
    private ConnectionType connectionType;
    // socket
    private DataContainer dataContainer;
    private SocketDataExchanger socketDataExchanger;
    // RMI
    // TODO

    public PlayerMessenger(Player player, ConnectionType connectionType, SocketDataExchanger socketDataExchanger) {
        this.player = player;
        this.connectionType = connectionType;
        if (connectionType.equals(ConnectionType.SOCKET)) {
            this.dataContainer = new DataContainer();
            this.socketDataExchanger = socketDataExchanger;
        }
        // RMI
        else {
        }
    }

    public SocketDataExchanger getSocketDataExchanger() {
        return socketDataExchanger;
    }

    @Override
    public void setGamePhase(GamePhase gamePhase) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("setGamePhase");
            dataContainer.setGamePhase(gamePhase);
            sendDataContainer();
        } else {
        }
    }

    /**
     * Helper method to send and then clear the player dataContainer.
     *
     * @author Boti
     */
    private void sendDataContainer() {
        try {
            socketDataExchanger.sendContainer(dataContainer);
        } catch (IOException e) {
            System.out.println("Error while sending dataContainer.");
        } finally {
            dataContainer.clearContainer();
        }
    }

    /**
     * Ends game for Client and clears all client resources in Server.
     */
    @Override
    public void endGame() {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("endGame");
            sendDataContainer();
            clearPlayerResources();
        } else {

        }

    }

    /**
     * Clears all players resources.
     *
     * @author carlo
     */
    void clearPlayerResources() {
        try {
            socketDataExchanger.closeResources();
        } catch (IOException e) {
            System.err.println("Error while closing all players resources");
        }
    }

    @Override
    public void printMessage(String message) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("printMessage");
            dataContainer.setMessage(message);
            sendDataContainer();
        } else {

        }

    }

    @Override
    public void printComponent(Component component) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("printComponent");
            dataContainer.setComponent(component);
            sendDataContainer();
        } else {

        }

    }

    @Override
    public void printShipboard(ShipBoard shipBoard) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("printShipboard");
            dataContainer.setShipBoard(shipBoard);
            sendDataContainer();
        } else {

        }

    }

    @Override
    public void printCard(Card card) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("printCard");
            dataContainer.setCard(card);
            sendDataContainer();
        } else {

        }

    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("printFlightBoard");
            dataContainer.setFlightBoard(flightBoard);
            sendDataContainer();
        } else {

        }

    }

    /**
     * @return the string that the player sent to the server
     * @author carlo
     */
    public String getPlayerString() throws PlayerDisconnectedException {
        return getPlayerInput();
    }

    /**
     * @return the string that the player sent to the server
     * @author carlo
     */
    private String getPlayerInput() throws PlayerDisconnectedException {
        try {
            return socketDataExchanger.getString();
        } catch (IOException e) {
            System.err.println("Error while obtaining data from client");
            throw new PlayerDisconnectedException(player);
        }
    }

    /**
     * @return integer that the player sent to the server
     * @author carlo
     */
    public int getPlayerInt() throws PlayerDisconnectedException {
        String input = getPlayerInput();
        int value;
        try {
            value = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            printMessage("You didn't enter an integer! Please reenter it: ");
            return getPlayerInt();
        }
        return value;
    }

    /**
     * @return array of coordinates that the player sent to the server
     * @author carlo
     */
    public int[] getPlayerCoordinates() throws PlayerDisconnectedException {
        String input = getPlayerInput();
        int[] coordinates = new int[2];
        try {
            String[] parts = input.split(" ");
            coordinates[0] = Integer.parseInt(parts[0]);
            coordinates[1] = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            printMessage("You didn't enter the coordinates in the correct syntax (X Y), please reenter them: ");
            return getPlayerCoordinates();
        }
        return coordinates;
    }

    /**
     * @return boolean that the player sent to the server
     * @author carlo
     */
    public boolean getPlayerBoolean() throws PlayerDisconnectedException {
        String input = getPlayerInput();
        if (input.equalsIgnoreCase("yes")) {
            return true;
        } else if (input.equalsIgnoreCase("no")) {
            return false;
        } else {
            printMessage("You didn't enter the correct response, please reenter it (yes/no): ");
            return getPlayerBoolean();
        }
    }
}
