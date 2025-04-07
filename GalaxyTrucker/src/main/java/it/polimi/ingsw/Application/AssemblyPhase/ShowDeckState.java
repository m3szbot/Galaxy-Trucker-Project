package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

/**
 * ShowDeckState prompts the player to select a deck to reveal.
 * After choosing, the game returns to the AssemblyState.
 *
 * @author Giacomo
 */
public class ShowDeckState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;

    /**
     * Constructs a ShowDeckState with the necessary protocol, view, and player.
     *
     * @param view the game view for user interaction
     * @param protocol the game logic handler
     * @param player the current player
     */
    public ShowDeckState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    /**
     * Displays the message asking the player to choose a deck.
     *
     * @param assemblyGame the current game instance
     * @param assemblyView the view used to print the prompt
     */
    @Override
    public void enter(AssemblyGame assemblyGame, AssemblyView assemblyView) {
        assemblyView.printChooseDeckMessage();
    }

    /**
     * Handles the user's input to select a deck, validates the index,
     * and triggers the protocol logic to show the deck.
     *
     * @param input the user's typed input
     * @param assemblyGame the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyGame assemblyGame) {
        int index = Integer.parseInt(input);
        if (index >= 0 && index <4){
            assemblyGame.getAssemblyProtocol().showDeck(index);
        }
        else{
            view.printNotValidDeckNumberMessage();
        }
        assemblyGame.setState(new AssemblyState(view, assemblyProtocol, player));
    }
}
