package it.polimi.ingsw;

import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MockerTest {
    // prints some text
    Game game = Mocker.mockNormalGame1Player();
    Player player = game.getGameInformation().getPlayerList().getFirst();
    PlayerMessenger playerMessenger = ClientMessenger.getGameMessenger(game.getGameCode()).getPlayerMessenger(player);

    @Test
    void checkSetup() {
        assertEquals(1, game.getGameInformation().getPlayerList().size());
        assertNotNull(player);
        assertNotNull(playerMessenger);
    }

    @Test
    void testRun() {
        game.run();
    }

    @Test
    void testInput() throws PlayerDisconnectedException {
        Mocker.simulateClientInput("banana\napple\ncake\n");

        assertEquals("banana", playerMessenger.getPlayerString());
        assertEquals("apple", playerMessenger.getPlayerString());
        assertEquals("cake", playerMessenger.getPlayerString());
    }
}
