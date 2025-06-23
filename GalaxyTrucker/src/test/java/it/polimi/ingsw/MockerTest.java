package it.polimi.ingsw;

import it.polimi.ingsw.Controller.Game.Game;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MockerTest {
    // prints some text
    Game game = Mocker.mockNormalGame1Player();

    @Test
    void checkSetup() {
        assertEquals(1, game.getGameInformation().getPlayerList().size());
    }

    @Test
    void testRun() {
        game.run();
    }

    @Test
    void testInput() {
        Mocker.simulateInput("banana");
        Scanner scanner = new Scanner(System.in);
        assertEquals("banana", scanner.nextLine());
    }
}
