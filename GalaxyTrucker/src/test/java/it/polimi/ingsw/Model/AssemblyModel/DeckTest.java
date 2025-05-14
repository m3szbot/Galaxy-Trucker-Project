package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DeckTest {
    GameInformation gameInformation;
    Deck deck;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // set up deck
        deck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
    }

    @Test
    void checkNumCard() {
        assertEquals(3, deck.getNumCards());
    }

    @Test
    void deckCardsRemovedFromGameInformation() {
        int count = gameInformation.getCardsList().size();
        deck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
        assertNotEquals(count, gameInformation.getCardsList().size());
    }

    @Test
    void checkCardLevels() {
        List<Card> tmp = deck.getCards();
        tmp.sort(Comparator.comparing(Card::getCardLevel));
        assertEquals(1, tmp.removeFirst().getCardLevel());
        assertEquals(2, tmp.removeFirst().getCardLevel());
        assertEquals(2, tmp.removeFirst().getCardLevel());
    }

    @Test
    void checkInUse() {
        assertEquals(false, deck.getInUse());
        deck.setInUse(true);
        assertEquals(true, deck.getInUse());
        deck.setInUse(false);
        assertEquals(false, deck.getInUse());
    }

}