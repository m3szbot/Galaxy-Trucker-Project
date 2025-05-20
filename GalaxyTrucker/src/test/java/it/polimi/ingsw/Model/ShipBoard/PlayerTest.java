package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayerTest {
    GameInformation gameInformation;
    Player player;

    @BeforeEach
    void setup() {
        // set up game information
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
    }

    @Test
    void newPlayer() {
        player = new Player("A", Color.RED, gameInformation);
        assertEquals("A", player.getNickName());
        assertEquals(Color.RED, player.getColor());
        assertNotNull(player.getShipBoard());
    }

}