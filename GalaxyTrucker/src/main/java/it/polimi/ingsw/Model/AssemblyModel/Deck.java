package it.polimi.ingsw.Model.AssemblyModel;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.polimi.ingsw.Cards.*;
import it.polimi.ingsw.Application.*;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameType;

/**
 * Represents one of the decks of cards used in the assembly phase of the game.
 * The deck is initialized based on the game type.
 * It selects specific cards of level 2 and 1 from the provided list.
 *
 * @author Giacomo
 */
public class Deck {
    private int numCards; // Number of cards currently in the deck
    private boolean inUse; // Indicates if the deck is in use
    private List<Card> cards; // List storing the deck's cards

    /**
     * Constructs a deck by selecting specific cards based on the game type.
     * It picks two level 2 cards and one level 1 card for a normal game.
     *
     * @param allCards The complete list of available cards.
     * @param gameType The type of game being played.
     */
    public Deck(List<Card> allCards, GameType gameType) {
        numCards = 0;
        inUse = false;
        cards = new ArrayList<>();
        if (gameType.equals(GameType.NormalGame)) {
            addCardToDeck(allCards, 2);
            addCardToDeck(allCards, 2);
            addCardToDeck(allCards, 1);
            Collections.shuffle(cards);
        }
        // TestGame
        else {
        }
    }

    private void addCardToDeck(List<Card> allCards, int level) {
        int i = 0;
        Collections.shuffle(allCards);
        do {
            i++;
        } while (allCards.get(i).getCardLevel() != level);
        this.cards.add(allCards.remove(i));
        this.numCards++;
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
