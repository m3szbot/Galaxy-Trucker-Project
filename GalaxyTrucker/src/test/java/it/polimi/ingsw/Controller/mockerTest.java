package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.mocker;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class mockerTest {
    Game game = mocker.mockNormalGame1Player();

    @Test
    void checkSetup() {
        assertEquals(1, game.getGameInformation().getPlayerList().size());
    }

    @Test
    void testRun() {
        game.run();
    }
}
