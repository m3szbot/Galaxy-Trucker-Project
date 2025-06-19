package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.AssemblyModel.Deck;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * ShowDeckState prompts the player to select a deck to reveal.
 * After choosing, the game returns to the AssemblyState.
 *
 * @author Giacomo
 */
public class ShowDeckState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player

    /**
     * Constructor inherited from GameState.
     */
    public ShowDeckState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }


    /**
     * Displays the message asking the player to choose a deck.
     *
     * @param assemblyThread the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        String message = "Choose a deck from 1 to 3 writing the number:";
        playerMessenger.printMessage(message);
    }

    /**
     * Handles the user's input to select a deck, validates the index,
     * and triggers the protocol logic to show the deck.
     *
     * @param input          the user's typed input
     * @param assemblyThread the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        try {
            int index = Integer.parseInt(input);
            if (index > 0 && index <= 3) {
                try {Deck deck = assemblyProtocol.showDeck(index);
                    for (Card card : deck.getCards()) {
                        playerMessenger.printCard(card);
                    }
                }catch (IllegalSelectionException e){
                    playerMessenger.printMessage(e.getMessage());
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                }
                assemblyThread.setState(new DeckInUseState(assemblyProtocol, playerMessenger, player, index));
            } else {
                String message = "Invalid deck number";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }
        }catch (NumberFormatException e){
            playerMessenger.printMessage("Invalid Input, please write a number");
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        }
    }
}
