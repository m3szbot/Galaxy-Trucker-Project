package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;

/**
 * Abstract Phase class defining the contract for the specific phase classes.
 * Defines attributes and methods used by all phases.
 *
 * @author Boti
 */
public abstract class Phase {
    // common attributes for all phases
    // protected so that subclasses can inherit and access them
    protected final GameInformation gameInformation;
    protected final GameMessenger gameMessenger;

    /**
     * Subclasses must use Phase constructor by calling:
     * super(gameInformation)
     * Superclass attributes are inherited,
     * but must be set to protected so that subclasses can access them.
     */
    public Phase(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        this.gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
    }

    public abstract void start();

    /**
     * Sets the given gamePhase for both server (gameInformation) and all clients.
     */
    public void setGamePhaseToClientServer(GamePhase gamePhase) {
        GameMessenger gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());
        // server
        gameInformation.setGamePhase(gamePhase);
        System.out.printf("%s phase is starting...\n", gamePhase);
        // clients
        gameMessenger.setGamePhaseToAll(gamePhase);
        gameMessenger.sendMessageToAll(String.format("%s phase is starting...\n", gamePhase));
    }
}
