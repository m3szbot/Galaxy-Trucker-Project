package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    GameInformation gameInformation;
    Player player;

    @BeforeEach
    void setup() {
        // set up game information
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        player = new Player("A", Color.RED, gameInformation);
    }

    @Test
    void newPlayer() {
        assertEquals("A", player.getNickName());
        assertEquals(Color.RED, player.getColor());
        assertNotNull(player.getShipBoard());
        System.out.println(player.getColouredNickName());
    }

    @Test
    void disconnectPlayer() {
        assertTrue(player.getIsConnected());
        player.disconnect();
        assertFalse(player.getIsConnected());
    }

    @Test
    void printColouredNicknames() {
        System.out.println(player.getColouredNickName());

        player = new Player("A", Color.GREEN, gameInformation);
        System.out.println(player.getColouredNickName());

        player = new Player("A", Color.BLUE, gameInformation);
        System.out.println(player.getColouredNickName());

        player = new Player("A", Color.YELLOW, gameInformation);
        System.out.println(player.getColouredNickName());

        player = new Player("A", null, gameInformation);
        System.out.println(player.getColouredNickName());
    }
}