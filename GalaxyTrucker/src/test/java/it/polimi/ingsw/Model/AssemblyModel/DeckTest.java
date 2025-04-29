package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {
    Deck deck;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        try {
            gameInformation.setUpCards(GameType.NormalGame);
        } catch (IOException e) {
        }
        // set up deck
        deck = new Deck(gameInformation.getCardsList(), gameInformation.getGameType());
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