package it.polimi.ingsw.Application;

import it.polimi.ingsw.Application.GameInformation.*;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardBuilder;
import it.polimi.ingsw.Cards.Sabotage;
import it.polimi.ingsw.Components.Component;
import it.polimi.ingsw.Components.SideType;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class GameInformationTest {
    GameInformation gameInformation;

    @Test
    void testSetUpPlayers() {
        Player player = new Player("Ludo", Color.RED, gameInformation);

        gameInformation.setUpPlayers(player, 3);

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
    void testSetUpComponents() throws IOException {
        gameInformation.setUpComponents();
        assertNotNull(gameInformation.getComponentList());
        for (int i = 0; i < gameInformation.getComponentList().size(); i++) {
            assertNotNull(gameInformation.getComponentList().get(i));
            Component component = gameInformation.getComponentList().get(i);
            assertNotNull(component.getComponentName());
            System.out.println(component.getComponentName());
            System.out.println(component.getFront());
        }
    }

    @Test
    void testAddPlayers() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        Player player3 = new Player("Carlo", Color.YELLOW, gameInformation);
        Player player4 = new Player("Gecky", Color.GREEN, gameInformation);
        gameInformation.setUpPlayers(player1, 3);

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
        gameInformation.setUpPlayers(player1, 3);
        gameInformation.addPlayers(player2);

        gameInformation.removePlayers(player1);
        assertNotNull(gameInformation.getPlayerList());
        assertEquals(player2, gameInformation.getPlayerList().getFirst());
        gameInformation.removePlayers(player2);
        assertNull(gameInformation.getPlayerList());
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
