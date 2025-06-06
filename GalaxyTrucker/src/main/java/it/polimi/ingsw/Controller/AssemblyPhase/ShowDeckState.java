package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.AssemblyModel.Deck;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * ShowDeckState prompts the player to select a deck to reveal.
 * After choosing, the game returns to the AssemblyState.
 *
 * @author Giacomo
 */
public class ShowDeckState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;

    /**
     * Constructs a ShowDeckState with the necessary protocol, view, and player.
     *
     * @param protocol the game logic handler
     * @param player   the current player
     */
    public ShowDeckState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    /**
     * Displays the message asking the player to choose a deck.
     *
     * @param assemblyPhase the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        String message = "Choose a deck from 1 to 3 writing the number:";
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);}

    /**
     * Handles the user's input to select a deck, validates the index,
     * and triggers the protocol logic to show the deck.
     *
     * @param input         the user's typed input
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        int index = Integer.parseInt(input);

        if (index > 0 && index <= 3) {
            if(assemblyProtocol.getDeck(index-1).getInUse() == false) {
                synchronized (assemblyProtocol.lockDecksList) {
                    Deck deck = assemblyPhase.getAssemblyProtocol().showDeck(index);
                    for (Card card : deck.getCards()) {
                        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printCard(card);
                    }
                }
                assemblyPhase.setState(new DeckInUseState(assemblyProtocol, player, index));
            }
            else{
                String message = "Deck already in use!";
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
                assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
            }
        } else {
            String message = "Invalid deck number";
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
            assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
        }
    }
}
