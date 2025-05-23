package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientServerInvokableMethods;
import it.polimi.ingsw.Connection.ClientSide.RMI.VirtualClient;
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
import java.rmi.RemoteException;

/**
 * Messenger service associated to the player.
 * Used for player specific controller communications.
 * Implements both socket and RMI method calls.
 *
 * @author Boti, carlo
 */
public class PlayerMessenger implements ViewServerInvokableMethods, ClientServerInvokableMethods{

    private Player player;
    private ConnectionType connectionType;
    // socket
    private DataContainer dataContainer;
    private SocketDataExchanger socketDataExchanger;
    private VirtualClient virtualClient;
    // RMI
    // TODO

    /**
     * Add socket player.
     */
    public PlayerMessenger(Player player, SocketDataExchanger socketDataExchanger) {
        this.player = player;
        this.connectionType = ConnectionType.SOCKET;
        this.dataContainer = new DataContainer();
        this.socketDataExchanger = socketDataExchanger;
    }

    /**
     * Add RMI player.
     */
    public PlayerMessenger(Player player, VirtualClient virtualClient) {
        this.player = player;
        this.connectionType = ConnectionType.RMI;
        this.virtualClient = virtualClient;
    }


    @Override
    public void setGamePhase(GamePhase gamePhase) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("setGamePhase");
            dataContainer.setGamePhase(gamePhase);
            sendDataContainer();
        } else {
            try{
                virtualClient.setGamePhase(gamePhase);
            } catch (RemoteException e) {

                System.err.println("An error occurred while setting the gamePhase of " + player.getNickName() +
                        " through rmi protocol");
            }
        }
    }

    /**
     * Helper method to send and then clear the player dataContainer.
     *
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

            try {

                virtualClient.endGame();
            } catch (RemoteException e) {

                System.err.println("RMI error while terminating " + player.getNickName() + " game");

            }

        }

    }

    /**
     * Clears all players resources.
     *
     */
    void clearPlayerResources() {
        try {
            socketDataExchanger.closeResources();
        } catch (IOException e) {
            System.err.println("Error while closing all players resources");
        }
    }

    /**
     * WARNING!! TO USE ONLY IN JOINING PHASE (FOR NOW)
     *
     * @param message
     */

    public void sendShortCutMessage(String message) {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            try {
                socketDataExchanger.sendString(message);
            } catch (IOException e) {
                System.err.println("Error while sending string shortcut to the player");
            }
        } else {

            try {

                virtualClient.printMessage(message);
            } catch (RemoteException e) {
                System.err.println("Error while communicating with the client with RMI protocol: shortCutMessage method");
            }

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

        if(connectionType == ConnectionType.SOCKET) {

            try {
                return socketDataExchanger.getString();
            } catch (IOException e) {
                System.err.println("Error while obtaining data from client");
                throw new PlayerDisconnectedException(player);
            }
        }
        else{

            try{
                return virtualClient.getString();
            } catch (RemoteException e) {

                System.err.println("An error was encountered while obtaining the input of " + player.getNickName());
                throw new PlayerDisconnectedException(player);

            }

        }
    }

    /**
     * @return integer that the player sent to the server
     * @author carlo
     */
    public int getPlayerInt() throws PlayerDisconnectedException {

        while (true) {
            String input = getPlayerInput();
            try {

                return Integer.parseInt(input);

            } catch (NumberFormatException e) {
                printMessage("You didn't enter an integer! Please reenter it: ");
            }

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

            try {

                virtualClient.printMessage(message);

            } catch (RemoteException e) {
                System.err.println("An error occurred while sending a message to " + player.getNickName() +
                        " through rmi protocol");
            }

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

            try{
                virtualClient.printComponent(component);
            } catch (RemoteException e) {

                System.err.println("An error occurred while sending a component to " + player.getNickName() +
                        " through rmi protocol");
            }

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

            try{
                virtualClient.printShipboard(shipBoard);
            } catch (RemoteException e) {

                System.err.println("An error occurred while sending a shipboard to " + player.getNickName() +
                        " through rmi protocol");
            }

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

            try{
                virtualClient.printCard(card);
            } catch (RemoteException e) {

                System.err.println("An error occurred while sending a card to " + player.getNickName() +
                        " through rmi protocol");
            }

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

            try{
                virtualClient.printFlightBoard(flightBoard);
            } catch (RemoteException e) {

                System.err.println("An error occurred while sending a flightboard to " + player.getNickName() +
                        " through rmi protocol");
            }

        }

    }

    /**
     * @return array of coordinates that the player sent to the server
     * @author carlo
     */
    public int[] getPlayerCoordinates() throws PlayerDisconnectedException {
        int[] coordinates = new int[2];
        while (true) {
            String input = getPlayerInput();
            try {
                String[] parts = input.split(" ");
                coordinates[0] = Integer.parseInt(parts[0]);
                coordinates[1] = Integer.parseInt(parts[1]);
                return coordinates;
            } catch (NumberFormatException e) {
                printMessage("You didn't enter the coordinates in the correct syntax (X Y), please reenter them: ");
            }
        }
    }

    /**
     * @return boolean that the player sent to the server
     * @author carlo
     */
    public boolean getPlayerBoolean() throws PlayerDisconnectedException {
        while (true) {
            String input = getPlayerInput();

            if (input.equalsIgnoreCase("yes")) {
                return true;
            } else if (input.equalsIgnoreCase("no")) {
                return false;
            } else {
                printMessage("You didn't enter the correct response, please reenter it (yes/no): ");
            }
        }
    }
}
