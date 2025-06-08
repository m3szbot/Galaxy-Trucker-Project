package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.Player;

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

    /**
     * Forces the player to give up during the flight.
     * Used by controllers and controller methods, threads.
     * For example: to handle exceptions: NoHumanCrewLeftException, etc...
     *
     * @author Boti
     */
    public static void forcePlayerToGiveUp(GameInformation gameInformation, Player player, GameMessenger gameMessenger, String messageToAll) {
        gameMessenger.sendMessageToAll(java.lang.String.format("Player %s has been forced to give up.", player.getNickName()));
        gameMessenger.sendMessageToAll(messageToAll);
        gameInformation.getFlightBoard().eliminatePlayer(player);
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
