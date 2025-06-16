package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyProtocolTest {
    GameInformation gameInformation;
    AssemblyProtocol assemblyProtocol;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // set up players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        gameInformation.setMaxNumberOfPlayers(4);
        gameInformation.addPlayer(playerA);
        gameInformation.addPlayer(playerB);
        gameInformation.addPlayer(playerC);
        gameInformation.addPlayer(playerD);

        // set up assemblyProtocol
        assemblyProtocol = new AssemblyProtocol(gameInformation);
    }

    // decks remove cards from a copy of cardsList, but not cardsList itself
    // because it cancels flightBoard cards
    @Test
    void deckCardsNotRemovedFromGameInformation() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        int cardCount = gameInformation.getCardsList().size();
        assemblyProtocol = new AssemblyProtocol(gameInformation);
        assertEquals(cardCount, gameInformation.getCardsList().size());
    }

    @Test
    void checkSetup() throws IllegalSelectionException {
        // check initial values
        assertNotNull(assemblyProtocol.getHourGlass());
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(0);
        });
        assertEquals(3, assemblyProtocol.showDeck(1).getNumCards());
        assertEquals(3, assemblyProtocol.showDeck(2).getNumCards());
        assertEquals(3, assemblyProtocol.showDeck(3).getNumCards());
        assertEquals(0, assemblyProtocol.getUncoveredList().size());
        assertNull(assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(0, assemblyProtocol.getBookedMap().get(playerA).size());
    }

    @Test
    void drawTwoNewComponents() throws IllegalSelectionException {
        // draw first component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getInHandMap().get(playerA));
        Component inHand = assemblyProtocol.getInHandMap().get(playerA);
        // draw second component
        assemblyProtocol.newComponent(playerA);
        assertNotNull(assemblyProtocol.getInHandMap().get(playerA));
        assertNotEquals(inHand, assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredList().getFirst());
    }

    @Test
    void chooseOneComponentFromUncoveredList() throws IllegalSelectionException {
        // put 1 component in uncoveredList
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getInHandMap().get(playerA);
        // choose from uncoveredList
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.chooseUncoveredComponent(playerA, 1);
        });
        assemblyProtocol.chooseUncoveredComponent(playerA, 0);
        assertNotEquals(inHand, assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredList().getFirst());
    }

    @Test
    void deckInUse() throws IllegalSelectionException {
        assemblyProtocol.showDeck(1);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.showDeck(1);
        });
    }


    @Test
    void exhaustCoveredList() throws IllegalSelectionException {
        while (!assemblyProtocol.getCoveredList().isEmpty()) {
            assemblyProtocol.newComponent(playerA);
        }
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.newComponent(playerA);
        });
    }

    @Test
    void bookOneComponent() throws IllegalSelectionException {
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getInHandMap().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getBookedMap().get(playerA).getFirst());
    }

    @Test
    void bookWithEmptyHand() {
        assertThrows(NoSuchElementException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });
    }

    @Test
    void bookTooManyComponents() throws IllegalSelectionException {
        // book 3 components
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assertThrows(IllegalSelectionException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });
    }

    @Test
    void chooseBookedComponent() throws IllegalSelectionException {
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getInHandMap().get(playerA);
        assemblyProtocol.bookComponent(playerA);
        assertNull(assemblyProtocol.getInHandMap().get(playerA));
        assemblyProtocol.chooseBookedComponent(playerA, 0);
        assertEquals(inHand, assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(0, assemblyProtocol.getBookedMap().get(playerA).size());
    }

    @Test
    void chooseBookedComponentFromEmptyList() {
        assertThrows(IndexOutOfBoundsException.class, () -> {
            assemblyProtocol.chooseBookedComponent(playerA, 0);
        });
    }
}