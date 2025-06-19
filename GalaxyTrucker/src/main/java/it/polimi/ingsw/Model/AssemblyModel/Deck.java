package it.polimi.ingsw.Model.AssemblyModel;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one of the decks of cards used in the assembly phase of the game.
 * The deck is initialized based on the game type.
 * It selects specific cards of level 2 and 1 from the provided list.
 *
 * @author Giacomo, Boti
 */
public class Deck {
    // constants
    public static final int NORMAL_LEVEL_ONE_CARD_COUNT = 1;
    public static final int NORMAL_LEVEL_TWO_CARD_COUNT = 2;
    public static final int TEST_LEVEL_ONE_CARD_COUNT = 2;
    public static final int TEST_LEVEL_TWO_CARD_COUNT = 0;

    private final List<Card> cards; // List storing the deck's cards, effectively final
    private boolean inUse; // Indicates if the deck is in use, mutable

    /**
     * Construct a deck based on gameType.
     */
    public Deck(List<Card> cardsList, GameType gameType) {
        inUse = false;
        cards = new ArrayList<>();
        // add cards to the deck
        if (gameType.equals(GameType.NORMALGAME)) {
            addCardToDeck(cardsList, 2);
            addCardToDeck(cardsList, 2);
            addCardToDeck(cardsList, 1);
        }
        // TestGame
        else {
            addCardToDeck(cardsList, 1);
            addCardToDeck(cardsList, 1);
        }
        Collections.shuffle(cards);
    }

    /**
     * Add card of selected level to the current deck and remove it from gameInformation cardsList
     *
     * @param level level of the card
     */
    private void addCardToDeck(List<Card> cardsList, int level) {
        int i = 0;
        Collections.shuffle(cardsList);
        // find card of required level
        while (cardsList.get(i).getCardLevel() != level) {
            i++;
        }
        this.cards.add(cardsList.remove(i));
    }


    /**
     * Returns the number of cards currently in the deck.
     *
     * @return Number of cards in the deck.
     */
    public int getNumCards() {
        return cards.size();
    }

    /**
     * Checks whether the deck is currently in use.
     *
     * @return True if the deck is in use, false otherwise.
     */
    public synchronized boolean getInUse() {
        return inUse;
    }

    /**
     * Sets inUsed field of current deck
     *
     * @param used true if deck is used
     */
    public synchronized void setInUse(boolean used) {
        this.inUse = used;
    }

    /**
     * Returns the list of cards in the deck.
     *
     * @return The deck's card list.
     */
    public List<Card> getCards() {
        return cards;
    }
}
