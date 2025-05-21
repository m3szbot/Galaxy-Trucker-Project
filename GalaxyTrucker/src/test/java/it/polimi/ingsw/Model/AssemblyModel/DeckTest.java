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
    Deck normalDeck, testDeck;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // set up cardsLists
        normalDeck = new Deck(gameInformation.getCardsList(), GameType.NORMALGAME);
        testDeck = new Deck(gameInformation.getCardsList(), GameType.TESTGAME);
    }

    @Test
    void checkNumCard() {
        assertEquals(3, normalDeck.getNumCards());
        assertEquals(2, testDeck.getNumCards());
    }

    @Test
    void deckCardsRemovedFromCardList() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        int count = gameInformation.getCardsList().size();
        normalDeck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
        assertNotEquals(count, gameInformation.getCardsList().size());

        count = gameInformation.getCardsList().size();
        testDeck = new Deck(gameInformation.getCardsList(), GameType.TESTGAME);
        assertNotEquals(count, gameInformation.getCardsList().size());
    }


    @Test
    void checkCardLevels() {
        List<Card> tmp = normalDeck.getCards();
        tmp.sort(Comparator.comparing(Card::getCardLevel));
        assertEquals(1, tmp.removeFirst().getCardLevel());
        assertEquals(2, tmp.removeFirst().getCardLevel());
        assertEquals(2, tmp.removeFirst().getCardLevel());
    }

    @Test
    void checkInUse() {
        assertEquals(false, normalDeck.getInUse());
        normalDeck.setInUse(true);
        assertEquals(true, normalDeck.getInUse());
        normalDeck.setInUse(false);
        assertEquals(false, normalDeck.getInUse());
    }

}