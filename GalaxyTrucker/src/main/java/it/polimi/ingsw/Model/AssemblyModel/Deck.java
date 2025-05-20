package it.polimi.ingsw.Model.AssemblyModel;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents one of the decks of cards used in the assembly phase of the game.
 *
 * @author Giacomo, Boti
 */
public class Deck {
    private boolean inUse; // Indicates if the deck is in use
    private List<Card> cards; // List storing the deck's cards

    /**
     * Construct a deck from the cards passed (only pass the needed cards!).
     * Throws error if gameType requirements are not respected.
     *
     * @param deckCards only the necessary cards for the deck.
     * @param gameType  to check gameType requirements.
     */
    public Deck(List<Card> deckCards, GameType gameType) {
        cards = new ArrayList<>();
        Collections.shuffle(cards);
        checkGameTypeRequirements(gameType);
    }

    /**
     * Check for gameType specific requirements.
     */
    private void checkGameTypeRequirements(GameType gameType) {
        // NORMAL GAME
        int levelOneCardCount = 1;
        int levelTwoCardCount = 2;
        // TEST GAME
        if (gameType.equals(GameType.TESTGAME)) {
            levelOneCardCount = 2;
            levelTwoCardCount = 0;
        }
        for (Card card : cards) {
            if (card.getCardLevel() == 1) {
                levelOneCardCount--;
            } else {
                levelTwoCardCount--;
            }
        }
        if (levelOneCardCount != 0 || levelTwoCardCount != 0) {
            throw new IllegalStateException("GameType requirements not respected for Deck cards.");
        }
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
    public boolean getInUse() {
        return inUse;
    }

    /**
     * Sets inUsed field of current deck
     *
     * @param used true if deck is used
     */
    public void setInUse(boolean used) {
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
