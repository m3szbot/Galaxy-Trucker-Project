package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * Abstract class representing a game state in the assembly phase.
 * It follows the Game State pattern and defines the interface for all concrete states.
 * Each state handles user input and may update itself based on game logic.
 *
 * @author Giacomo
 */
public abstract class GameState {
    protected final AssemblyProtocol assemblyProtocol;
    protected final PlayerMessenger playerMessenger;
    protected final Player player;

    /**
     * Constructs a new GameState.
     *
     * @param assemblyProtocol the assembly protocol managing the phase
     * @param playerMessenger  the messenger to communicate with the player
     * @param player           the current player
     */
    public GameState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        this.assemblyProtocol = assemblyProtocol;
        this.playerMessenger = playerMessenger;
        this.player = player;
    }

    /**
     * Called when this state becomes active.
     * Used to send messages or initialize state-specific logic.
     *
     * @param assemblyThread the thread managing this player's assembly phase
     */
    abstract void enter(AssemblyThread assemblyThread);

    /**
     * Handles user input during this state.
     *
     * @param input           the string input received from the player
     * @param assemblyThread  the thread managing this player's assembly phase
     */
    abstract void handleInput(String input, AssemblyThread assemblyThread);

    /**
     * Optional update method for state logic that needs to evolve over time.
     * Can be overridden by subclasses when needed.
     *
     * @param assemblyThread the thread managing this player's assembly phase
     */
    void update(AssemblyThread assemblyThread) {
    }
}