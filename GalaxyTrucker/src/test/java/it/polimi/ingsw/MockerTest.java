package it.polimi.ingsw;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MockerTest {
    // prints some text
    Game game1 = Mocker.mockNormalGame1Player();
    Player player1 = game1.getGameInformation().getPlayerList().getFirst();
    PlayerMessenger playerMessenger1 = ClientMessenger.getGameMessenger(game1.getGameCode()).getPlayerMessenger(player1);

    Game game2 = Mocker.mockNormalGame2Players();
    Player player2 = game2.getGameInformation().getPlayerList().get(1);
    PlayerMessenger playerMessenger2 = ClientMessenger.getGameMessenger(game2.getGameCode()).getPlayerMessenger(player2);

    @Test
    void checkSetup() {
        assertEquals(1, game1.getGameInformation().getPlayerList().size());
        assertNotNull(player1);
        assertNotNull(playerMessenger1);

        assertEquals(2, game2.getGameInformation().getPlayerList().size());
        assertNotNull(player2);
        assertNotNull(playerMessenger2);
    }

    @Test
    void testRun() {
        game1.run();
    }

    @Test
    void testShutDownInput() {
        Mocker.simulateClientInput("a\n");
        Mocker.endInputThread();
    }

    @Test
    void testInput() throws PlayerDisconnectedException {
        Mocker.simulateClientInput("banana\napple\ncake\n");

        assertEquals("banana", playerMessenger1.getPlayerString());
        assertEquals("apple", playerMessenger1.getPlayerString());
        assertEquals("cake", playerMessenger1.getPlayerString());
    }

    @Test
    void testInputReset() throws PlayerDisconnectedException {
        Mocker.simulateClientInput("a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n");
        Sleeper.sleepXSeconds(1);
        assertTrue(ClientInputManager.getTestRunning());
        assertEquals("a", ClientInputManager.getSimulatedInput());


        Mocker.endInputThread();
        Sleeper.sleepXSeconds(1);
        assertFalse(ClientInputManager.getTestRunning());
        assertEquals(null, ClientInputManager.getSimulatedInput());

    }
}
