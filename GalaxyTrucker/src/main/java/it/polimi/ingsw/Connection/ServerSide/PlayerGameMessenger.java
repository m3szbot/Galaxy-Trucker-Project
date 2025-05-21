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
 * A Game Messenger service associated to the player.
 * Used by Phases to call methods on Clients.
 * Implements both socket and RMI method calls.
 *
 * @author Boti
 */
public class PlayerGameMessenger implements ViewServerInvokableMethods, ClientServerInvokableMethods {
    Player player;
    ConnectionType connectionType;
    // socket
    DataContainer dataContainer;
    SocketDataExchanger socketDataExchanger;
    // RMI
    // TODO

    public PlayerGameMessenger(Player player, ConnectionType connectionType, SocketDataExchanger socketDataExchanger) {
        this.player = player;
        this.connectionType = connectionType;
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer = new DataContainer();
            this.socketDataExchanger = socketDataExchanger;
        }
        // RMI
        else {
        }
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
            e.printStackTrace();
        } finally {
            dataContainer.clearContainer();
        }
    }

    @Override
    public void endGame() {
        if (connectionType.equals(ConnectionType.SOCKET)) {
            dataContainer.clearContainer();
            dataContainer.setCommand("endGame");
            sendDataContainer();
        } else {

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
}
