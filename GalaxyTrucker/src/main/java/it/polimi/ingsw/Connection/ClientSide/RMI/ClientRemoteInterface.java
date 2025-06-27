package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface which defines the methods that can be called by the server
 * and which run on the client. This of course happens with rmi protocol
 *
 * @author carlo
 */

public interface ClientRemoteInterface extends Remote {

    /*
    All these methods are used remotely
     */

    /**
     * Shows a message to the player
     * @param message
     * @throws RemoteException
     */

    public void printMessage(String message) throws RemoteException;

    /**
     * Used by the server to obtain the player's input
     * @return
     * @throws RemoteException
     */

    public String getString() throws RemoteException;

    /**
     * Shows a component to the player
     * @param component
     * @throws RemoteException
     */

    public void printComponent(Component component) throws RemoteException;

    /**
     * Shows a shipboard to the player
     * @param shipBoard
     * @throws RemoteException
     */

    public void printShipboard(ShipBoard shipBoard) throws RemoteException;

    /**
     * Shows a card to the player
     * @param card
     * @throws RemoteException
     */

    public void printCard(Card card) throws RemoteException;

    /**
     * Shows a flightboard to the player
     *
     * @param flightBoard
     * @throws RemoteException
     */

    public void printFlightBoard(FlightBoard flightBoard) throws RemoteException;

    /**
     * Sets the gamePhase. The game phase is fundamental for the GUI, as the screen
     * view of the player will change
     * @param gamePhase
     * @throws RemoteException
     */

    public void setGamePhase(GamePhase gamePhase) throws RemoteException;

    /**
     * Ends the game of the player
     * @throws RemoteException
     */

    public void endGame() throws RemoteException;

    /**
     *
     * @return true if the player is currently in game, false otherwise
     * @throws RemoteException
     */

    public boolean isInGame() throws RemoteException;

    /**
     * Sets the inGame attribute of virtualClient.
     * @param inGame
     * @throws RemoteException
     */

    public void setInGame(boolean inGame) throws RemoteException;

    /**
     * Used to unblock the user input. This because the call to a method which gets the user
     * input is a blocking call
     * @throws RemoteException
     */

    public void unblockUserInput() throws RemoteException;

    /**
     * Used to print a message before the lobby starts, i.e, when the player is accessing the game
     * and sending his nickname to the server
     * @param message
     * @throws RemoteException
     */

    public void printPreJoinMessage(String message) throws RemoteException;

    /**
     * Sets the game type
     * @param gameType
     * @throws RemoteException
     */

    public void setGameType(String gameType) throws RemoteException;

    /**
     * Used to send a command to the player
     * @param command
     * @throws RemoteException
     */

    public void sendCommand(String command) throws RemoteException;

    public String getCommand() throws RemoteException;
}
