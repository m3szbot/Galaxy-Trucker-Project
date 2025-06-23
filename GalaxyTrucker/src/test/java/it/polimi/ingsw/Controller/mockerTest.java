package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.mocker;
import org.junit.jupiter.api.Test;

public class mockerTest {
    Game game = mocker.mockNormalGame1Player();

    @Test
    void testRun() {
        game.run();
    }
}
