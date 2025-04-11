package it.polimi.ingsw.Assembly;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Application.*;

/**
 * Represents one of the decks of cards used in the assembly phase of the game.
 * The deck is initialized based on the game type.
 * It selects specific cards of level 2 and 1 from the provided list.
 *
 * @author Giacomo
 */
public class Deck {
    private int numCards; // Number of cards currently in the deck
    private boolean inUse = false; // Indicates if the deck is in use
    private List<Card> cards; // List storing the deck's cards

    /**
     * Constructs a deck by selecting specific cards based on the game type.
     * It picks two level 2 cards and one level 1 card for a normal game.
     *
     * @param allCards The complete list of available cards.
     * @param gameType The type of game being played.
     */
    public Deck(List<Card> allCards, GameType gameType) {
        cards = new ArrayList<Card>();
        if (gameType.equals(GameType.NormalGame)) {
            // Attempt to add the first level 2 card
            do {
                Collections.shuffle(allCards); // Shuffle the deck randomly
            } while (allCards.getFirst().getCardLevel() != 2); // Ensure the first card is level 2
            cards.add(allCards.getFirst()); // Add the selected card to the deck
            numCards++; // Increase the card count
            allCards.removeFirst(); // Remove the selected card from the available list

            // Attempt to add the second level 2 card
            do {
                Collections.shuffle(allCards); // Shuffle again to randomize selection
            } while (allCards.getFirst().getCardLevel() != 2); // Ensure the first card is level 2
            cards.add(allCards.getFirst()); // Add the second level 2 card
            numCards++;
            allCards.removeFirst();

            // Attempt to add the level 1 card
            do {
                Collections.shuffle(allCards); // Shuffle once more
            } while (allCards.getFirst().getCardLevel() != 1); // Ensure the first card is level 1
            cards.add(allCards.getFirst()); // Add the selected level 1 card
            numCards++;
            allCards.removeFirst();
        }
    }

    /**
     * Returns the number of cards currently in the deck.
     *
     * @return Number of cards in the deck.
     */
    public int getNumCards() {
        return numCards;
    }

    /**
     * Checks whether the deck is currently in use.
     *
     * @return True if the deck is in use, false otherwise.
     */
    public boolean isInUse() {
        return inUse;
    }

    /**
     * Returns the list of cards in the deck.
     *
     * @return The deck's card list.
     */
    public List<Card> getCards() {
        return cards;
    }

    //da implementare ma devo ancora capire come poichè questo metodo sarà legato principalmente all'azione del controllore e della view
    public List<Card> showCards() {
        inUse = true;
        return cards;
    }
}
