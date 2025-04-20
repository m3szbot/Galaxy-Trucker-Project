package it.polimi.ingsw.Assembly;

import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardBuilder;
import it.polimi.ingsw.Cards.Epidemic;
import it.polimi.ingsw.Cards.Sabotage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    Deck deck;

    @BeforeEach
    void setUp() {
        // set up cards
        List<Card> cardList = new ArrayList<>();
        CardBuilder cardBuilder = new CardBuilder();
        // add level 2 cards to cardList
        cardBuilder.buildCardLevel(2);
        Card card = new Sabotage(cardBuilder);
        for (int i = 0; i < 10; i++) {
            cardList.add(card);
        }
        // add level 1 cards to cardList
        cardBuilder.buildCardLevel(1);
        card = new Epidemic(cardBuilder);
        for (int i = 0; i < 10; i++) {
            cardList.add(card);
        }
        deck = new Deck(cardList, GameType.NormalGame);
    }

    @Test
    void checkNumCard() {
        assertEquals(3, deck.getNumCards());
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