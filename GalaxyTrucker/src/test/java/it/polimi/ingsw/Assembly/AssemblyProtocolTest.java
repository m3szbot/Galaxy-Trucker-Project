package it.polimi.ingsw.Assembly;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyProtocolTest {
    AssemblyProtocol assemblyProtocol;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        try {
            gameInformation.setUpCards(GameType.NormalGame);
            gameInformation.setUpComponents();
        } catch (IOException e) {
        }
        // set up players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        gameInformation.setUpPlayers(playerA, 4);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

        // set up assemblyProtocol
        assemblyProtocol = new AssemblyProtocol(gameInformation);
    }

    @Test
    void checkSetup() {
        // check initial values
        assertNotNull(assemblyProtocol.getHourGlass());
        assertThrows(IllegalArgumentException.class, () -> {
            assemblyProtocol.showDeck(0);
        });
        assertNotNull(assemblyProtocol.showDeck(1));
        assertNotNull(assemblyProtocol.showDeck(2));
        assertNotNull(assemblyProtocol.showDeck(3));
        assertEquals(0, assemblyProtocol.getUncoveredList().size());
        assertNull(assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(0, assemblyProtocol.getBookedMap().get(playerA).size());
    }

    @Test
    void drawTwoNewComponents() {
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
    void chooseOneComponentFromUncoveredList() {
        // put 1 component in uncoveredList
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        Component inHand = assemblyProtocol.getInHandMap().get(playerA);
        // choose from uncoveredList
        assertThrows(IndexOutOfBoundsException.class, () -> {
            assemblyProtocol.chooseUncoveredComponent(playerA, 1);
        });
        assemblyProtocol.chooseUncoveredComponent(playerA, 0);
        assertNotEquals(inHand, assemblyProtocol.getInHandMap().get(playerA));
        assertEquals(inHand, assemblyProtocol.getUncoveredList().getFirst());
    }

    @Test
    void deckInUse() {
        assemblyProtocol.showDeck(1);
        assertThrows(IllegalStateException.class, () -> {
            assemblyProtocol.showDeck(1);
        });
    }

    @Test
    void mergeAllDecks() {
        assertEquals(12, assemblyProtocol.mergeDecks().size());
    }

    @Test
    void exhaustCoveredList() {
        while (!assemblyProtocol.getCoveredList().isEmpty()) {
            assemblyProtocol.newComponent(playerA);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> {
            assemblyProtocol.newComponent(playerA);
        });
    }

    @Test
    void bookOneComponent() {
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
    void bookTooManyComponents() {
        // book 3 components
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assemblyProtocol.bookComponent(playerA);
        assemblyProtocol.newComponent(playerA);
        assertThrows(IllegalStateException.class, () -> {
            assemblyProtocol.bookComponent(playerA);
        });
    }

    @Test
    void chooseBookedComponent() {
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