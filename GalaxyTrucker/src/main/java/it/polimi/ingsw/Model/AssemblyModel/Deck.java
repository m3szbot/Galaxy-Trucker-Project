package it.polimi.ingsw.Model.AssemblyModel;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
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
    private boolean inUse; // Indicates if the deck is in use
    private List<Card> cards; // List storing the deck's cards

    /**
     * Construct a deck based on gameType
     * Normal Game: 4 decks of: 2 level 2 + 1 level 1 card (12 tot)
     * Test Game: 4 decks of: 2 level 1 cardsList (8 tot)
     */
    public Deck(GameInformation gameInformation) {
        cards = new ArrayList<>();
        // add cards to the deck
        if (gameInformation.getGameType().equals(GameType.NormalGame)) {
            addCardToDeck(gameInformation, 2);
            addCardToDeck(gameInformation, 2);
            addCardToDeck(gameInformation, 1);
        }
        // TestGame
        else {
            addCardToDeck(gameInformation, 1);
            addCardToDeck(gameInformation, 1);
        }
        Collections.shuffle(cards);
    }

    /**
     * Add card of selected level to the current deck and remove it from gameInformation cardsList
     *
     * @param level level of the card
     */
    private void addCardToDeck(GameInformation gameInformation, int level) {
        int i = 0;
        Collections.shuffle(gameInformation.getCardsList());
        // find card of required level
        do {
            i++;
        } while (gameInformation.getCardsList().get(i).getCardLevel() != level);
        this.cards.add(gameInformation.getCardsList().remove(i));
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
