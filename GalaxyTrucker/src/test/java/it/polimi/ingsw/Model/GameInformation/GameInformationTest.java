package it.polimi.ingsw.Model.GameInformation;

import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.AlienSupport;
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
        gameInformation.setGameType(GameType.NORMALGAME);
    }

    @AfterEach
    void tearDown() {
        this.gameInformation = null;
    }

    @Test
    void testSetUpPlayers() {
        Player player = new Player("Ludo", Color.RED, gameInformation);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayer(player);

        assertEquals(3, gameInformation.getMaxNumberOfPlayers());
        assertEquals(player, gameInformation.getPlayerList().getFirst());
        assertTrue(gameInformation.getDisconnectedPlayerList().isEmpty());
        assertTrue(gameInformation.checkPlayerConnectivity(player));
    }


    @Test
    void testSetUpCards() {
        GameType testgame = GameType.TESTGAME;
        GameType normalgame = GameType.NORMALGAME;

        gameInformation.setUpGameInformation(testgame, 4);
        assertNotNull(gameInformation.getCardsList());
        for (int i = 0; i < gameInformation.getCardsList().size(); i++) {
            Card card = gameInformation.getCardsList().get(i);
            System.out.println(card.getCardName());
            System.out.println(card.getCardLevel());
        }
        assertEquals(8, gameInformation.getCardsList().size());

        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(normalgame, 4);
        assertNotNull(gameInformation.getCardsList());
        for (int i = 0; i < gameInformation.getCardsList().size(); i++) {
            assertNotNull(gameInformation.getCardsList().get(i));
            Card card = gameInformation.getCardsList().get(i);
            assertNotNull(card.getCardName());
            System.out.println(card.getCardName());
        }
        assertEquals(12, gameInformation.getCardsList().size());
    }

    @Test
    void test2SetUpCards() throws IOException {
        // Test with TestGame type
        gameInformation.setUpGameInformation(GameType.TESTGAME, 4);
        List<Card> cards = gameInformation.getCardsList();
        assertNotNull(cards, "Cards list should not be null");
        assertFalse(cards.isEmpty(), "Cards list should not be empty");

        // Verify NormalGame contains mixed levels
        gameInformation.getCardsList().clear();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        cards = gameInformation.getCardsList();
        assertNotNull(cards, "Cards list should not be null");
        assertFalse(cards.isEmpty(), "Cards list should not be empty");
    }

    @Test
    void testNormalGameSetUpComponents() throws IOException {
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        assertNotNull(gameInformation.getComponentList());
        assertEquals(152, gameInformation.getComponentList().size());

        // alien supports
        assertTrue(0 < gameInformation.getComponentList().stream().filter(c -> c instanceof AlienSupport).count());
        assertEquals(12, gameInformation.getComponentList().stream().filter(c -> c instanceof AlienSupport).count());

    }

    @Test
    void testTestGameSetUpComponents() throws IOException {
        gameInformation.setUpGameInformation(GameType.TESTGAME, 4);
        assertNotNull(gameInformation.getComponentList());
        assertEquals(140, gameInformation.getComponentList().size());
        // no aliens supports (-12 cards)
        assertEquals(0, gameInformation.getComponentList().stream().filter(c -> c instanceof AlienSupport).count());
    }

    @Test
    void testAddPlayers() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        Player player3 = new Player("Carlo", Color.YELLOW, gameInformation);
        Player player4 = new Player("Gecky", Color.GREEN, gameInformation);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayer(player1);

        gameInformation.addPlayer(player2);
        gameInformation.addPlayer(player3);
        gameInformation.addPlayer(player4);
        assertNotNull(gameInformation.getPlayerList());
        assertEquals(3, gameInformation.getPlayerList().size());
        assertEquals(player1, gameInformation.getPlayerList().getFirst());
        assertEquals(player2, gameInformation.getPlayerList().get(1));
        assertEquals(player3, gameInformation.getPlayerList().getLast());
    }

    @Test
    void testDisconnectPlayers() {
        Player player1 = new Player("Ludo", Color.RED, gameInformation);
        Player player2 = new Player("Boti", Color.BLUE, gameInformation);
        gameInformation.setGameType(GameType.NORMALGAME);
        gameInformation.setMaxNumberOfPlayers(3);
        gameInformation.addPlayer(player1);
        gameInformation.addPlayer(player2);

        gameInformation.disconnectPlayerInGameInformation(player1);
        assertNotNull(gameInformation.getPlayerList());
        assertNotNull(gameInformation.getPlayerList().get(0));
        assertEquals(player2, gameInformation.getPlayerList().get(0));
        assertEquals(1, gameInformation.getDisconnectedPlayerList().size());
        assertTrue(gameInformation.checkPlayerConnectivity(player2));
        assertFalse(gameInformation.checkPlayerConnectivity(player1));


        gameInformation.disconnectPlayerInGameInformation(player2);
        assertEquals(0, gameInformation.getPlayerList().size());
        assertEquals(2, gameInformation.getDisconnectedPlayerList().size());
        assertFalse(gameInformation.checkPlayerConnectivity(player1));
        assertFalse(gameInformation.checkPlayerConnectivity(player1));
    }

    @Test
    void testSetUpFlightBoard() throws IOException {
        GameType gameType1 = GameType.NORMALGAME;
        gameInformation.setUpGameInformation(gameType1, 4);
        assertNotNull(gameInformation.getCardsList());
        assertNotNull(gameInformation.getFlightBoard());
    }

    @Test
    void testSetGameType() {
        GameType gameType1 = GameType.TESTGAME;
        GameType gameType2 = GameType.NORMALGAME;

        gameInformation.setGameType(gameType1);
        assertEquals(gameType1, gameInformation.getGameType());
        gameInformation.setGameType(gameType2);
        assertEquals(gameType2, gameInformation.getGameType());
    }

    @Test
    void setGamePhase() {
        gameInformation.setGamePhase(GamePhase.Assembly);
        assertEquals(GamePhase.Assembly, gameInformation.getGamePhase());
    }
}
