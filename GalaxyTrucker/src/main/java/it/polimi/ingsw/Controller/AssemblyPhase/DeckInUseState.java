package it.polimi.ingsw.Controller.AssemblyPhase;


import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * {@code DeckInUseState} manages the situation where a player is consulting a component deck.
 * Once the player confirms they are done, the deck is marked as no longer in use
 * and the player is returned to the main assembly state.
 */
public class DeckInUseState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    private Integer index;

    /**
     * Constructs a new {@code DeckInUseState}.
     *
     * @param assemblyProtocol the shared assembly protocol logic
     * @param playerMessenger  the messenger used to communicate with the player
     * @param player           the player currently consulting the deck
     * @param index            the index of the deck being consulted (1-based)
     */
    public DeckInUseState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player, Integer index) {
        super(assemblyProtocol, playerMessenger, player);
        this.index = index;
    }

    /**
     * Called when the player enters this state.
     * Prompts the player to confirm when they are finished consulting the deck.
     *
     * @param assemblyThread the thread managing the current player's assembly phase
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        String message = "Write yes if you have finished consulting the deck";
        playerMessenger.printMessage(message);
    }

    /**
     * Handles the player's input. If the input is "yes", marks the deck as no longer in use
     * and returns to the main assembly state. Otherwise, re-prompts the player.
     *
     * @param input          the player's input string
     * @param assemblyThread the thread managing the current player's assembly phase
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        if (input.equalsIgnoreCase("yes")) {
            assemblyProtocol.getDeck(index - 1).setInUse(false);
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        } else {
            String message = "Invalid Input";
            playerMessenger.printMessage(message);
            assemblyThread.setState(new DeckInUseState(assemblyProtocol, playerMessenger, player, index));
        }
    }


}
