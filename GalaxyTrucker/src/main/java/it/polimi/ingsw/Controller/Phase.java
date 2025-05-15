package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.GameMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Abstract Phase class defining the contract for the specific phase classes.
 * Defines methods used by all phases.
 *
 * @author Boti
 */
public abstract class Phase {
    // necessary field for all subclasses
    private final GameInformation gameInformation;
    private final GameMessenger gameMessenger;

    public Phase(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        this.gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
    }

    public abstract void start();

    /**
     * Sets the given gamePhase for both server (gameInformation) and all clients.
     */
    public void setGamePhaseToAll(GamePhase gamePhase) {
        GameMessenger gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
        // server
        gameInformation.setGamePhase(gamePhase);
        System.out.printf("%s phase is starting...\n", gamePhase);
        // clients
        gameMessenger.setGamePhaseToAll(gamePhase);
        gameMessenger.sendMessageToALl(String.format("%s phase is starting...\n", gamePhase));
    }

    /**
     * Disconnect player from the server.
     */
    public void disconnectPlayer(Player player) {
        gameInformation.getPlayerList().remove(player);
        gameInformation.getDisconnectedPlayerList().add(player);
        ClientMessenger.getGameMessenger(gameInformation.getGameCode()).clearPlayerResources(player);
    }

    /**
     * Reconnect player to the server.
     */
    public void reconnectPlayer(Player player) {
        gameInformation.getDisconnectedPlayerList().remove(player);
        gameInformation.getPlayerList().add(player);
    }

}
