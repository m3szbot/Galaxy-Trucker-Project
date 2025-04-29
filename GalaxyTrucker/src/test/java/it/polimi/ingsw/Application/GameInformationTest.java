package it.polimi.ingsw.Application;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.GameInformation.ViewType;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameInformationTest {
    GameInformation gameInformation;

    @BeforeEach
    void setUp() {
        this.gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
    }

    @AfterEach
    void tearDown() {
        this.gameInformation = null;
    }

    @Test
    void testSetUpPlayers() {
        Player player = new Player("Ludo", Color.RED, gameInformation);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayers(player);

        assertEquals(3, gameInformation.getMaxNumberOfPlayers());
        assertEquals(player, gameInformation.getPlayerList().getFirst());
    }

    @Test
    void testSetPlayerViewMap() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        ViewType viewType1 = ViewType.CLI;
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        ViewType viewType2 = ViewType.GUI;

        gameInformation.setPlayerViewType(player1, viewType1);
        gameInformation.setPlayerViewType(player2, viewType2);

        assertEquals(viewType1, gameInformation.getPlayerViewType(player1));
        assertEquals(viewType2, gameInformation.getPlayerViewType(player2));
    }

    @Test
    void testSetUpCards() throws IOException {
        GameType gameType1 = GameType.TestGame;
        GameType gameType2 = GameType.NormalGame;

        gameInformation.setUpCards(gameType1);
        assertNotNull(gameInformation.getCardsList());
        for (int i = 0; i < gameInformation.getCardsList().size(); i++) {
            Card card = gameInformation.getCardsList().get(i);
            System.out.println(card.getCardName());
            System.out.println(card.getCardLevel());
        }
        gameInformation.setUpCards(gameType2);
        assertNotNull(gameInformation.getCardsList());
        for (int i = 0; i < gameInformation.getCardsList().size(); i++) {
            assertNotNull(gameInformation.getCardsList().get(i));
            Card card = gameInformation.getCardsList().get(i);
            assertNotNull(card.getCardName());
            System.out.println(card.getCardName());
        }
    }

    @Test
    void test2SetUpCards() throws IOException {
        // Test with TestGame type
        gameInformation.setUpCards(GameType.TestGame);
        List<Card> cards = gameInformation.getCardsList();
        assertNotNull(cards, "Cards list should not be null");
        assertFalse(cards.isEmpty(), "Cards list should not be empty");

        // Verify NormalGame contains mixed levels
        gameInformation.getCardsList().clear();
        gameInformation.setUpCards(GameType.NormalGame);
        cards = gameInformation.getCardsList();
        assertNotNull(cards, "Cards list should not be null");
        assertFalse(cards.isEmpty(), "Cards list should not be empty");
    }

    @Test
    void testSetUpComponents() throws IOException {
        gameInformation.setUpComponents();
        assertNotNull(gameInformation.getComponentList());
        for (int i = 0; i < gameInformation.getComponentList().size(); i++) {
            assertNotNull(gameInformation.getComponentList().get(i));
            Component component = gameInformation.getComponentList().get(i);
            assertNotNull(component.getComponentName());
            System.out.println(component.getComponentName());
            System.out.println(component.getFront());
            System.out.println(component.getLeft());
            System.out.println(component.getBack());
            System.out.println(component.getRight());
            System.out.println(i);
        }
    }

    @Test
    void testAddPlayers() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        Player player3 = new Player("Carlo", Color.YELLOW, gameInformation);
        Player player4 = new Player("Gecky", Color.GREEN, gameInformation);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayers(player1);

        gameInformation.addPlayers(player2);
        gameInformation.addPlayers(player3);
        gameInformation.addPlayers(player4);
        assertNotNull(gameInformation.getPlayerList());
        assertEquals(3, gameInformation.getPlayerList().size());
        assertEquals(player1, gameInformation.getPlayerList().getFirst());
        assertEquals(player2, gameInformation.getPlayerList().get(1));
        assertEquals(player3, gameInformation.getPlayerList().getLast());
    }

    @Test
    void testRemovePlayers() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        gameInformation.setGameType(GameType.NormalGame);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayers(player1);
        gameInformation.addPlayers(player2);

        gameInformation.removePlayers(player1);
        assertNotNull(gameInformation.getPlayerList());
        assertNotNull(gameInformation.getPlayerList().get(0));
        assertEquals(player2, gameInformation.getPlayerList().get(0));
        gameInformation.removePlayers(player2);
        assertEquals(0, gameInformation.getPlayerList().size());
    }

    @Test
    void testSetUpFlightBoard() throws IOException {
        GameType gameType1 = GameType.NormalGame;
        gameInformation.setUpCards(gameType1);
        assertNotNull(gameInformation.getCardsList());

        gameInformation.setUpFlightBoard();
        assertNotNull(gameInformation.getFlightBoard());
    }

    @Test
    void testSetGameType() {
        GameType gameType1 = GameType.TestGame;
        GameType gameType2 = GameType.NormalGame;

        gameInformation.setGameType(gameType1);
        assertEquals(gameType1, gameInformation.getGameType());
        gameInformation.setGameType(gameType2);
        assertEquals(gameType2, gameInformation.getGameType());
    }
}
