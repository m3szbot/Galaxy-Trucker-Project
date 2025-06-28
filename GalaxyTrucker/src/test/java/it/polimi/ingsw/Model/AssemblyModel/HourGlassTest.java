package it.polimi.ingsw.Model.AssemblyModel;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyPhase;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Controller.Sleeper;
import it.polimi.ingsw.Mocker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HourGlassTest {
    Game game;
    AssemblyPhase assemblyPhase;
    AssemblyProtocol assemblyProtocol;
    HourGlass hourGlass;
    // 1 life = 60 secs

    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        assemblyPhase = game.getAssemblyPhase();
        assemblyProtocol = assemblyPhase.getAssemblyProtocol();
        hourGlass = assemblyPhase.getAssemblyProtocol().getHourGlass();
    }

    @RepeatedTest(5)
    void interruptImmediately() {
        hourGlass.twist(assemblyProtocol);
        hourGlass.stopHourglass();

        assertTrue(hourGlass.getScheduler().isShutdown());
        // wait for current task to finish
        Sleeper.sleepXSeconds(0.1);
        assertTrue(hourGlass.getScheduler().isTerminated());
    }


    @Test
    void interruptAfter15Secs() {
        hourGlass.twist(assemblyProtocol);
        Sleeper.sleepXSeconds(15.1);
        hourGlass.stopHourglass();

        Sleeper.sleepXSeconds(0.1);
        assertTrue(hourGlass.getScheduler().isShutdown());
        assertTrue(hourGlass.getScheduler().isTerminated());
    }

    @Test
    void fullCycle() {
        // cycle 0
        hourGlass.twist(assemblyProtocol);
        Sleeper.sleepXSeconds(60.1);
        assertTrue(hourGlass.isFinished());
        assertEquals(1, hourGlass.getState());

        // cycle 1
        hourGlass.twist(assemblyProtocol);
        Sleeper.sleepXSeconds(60.1);
        assertTrue(hourGlass.isFinished());
        assertEquals(2, hourGlass.getState());

        // cycle 2
        hourGlass.twist(assemblyProtocol);
        Sleeper.sleepXSeconds(60.1);
        assertTrue(hourGlass.isFinished());
        assertEquals(3, hourGlass.getState());

        // finished - twist does nothing
        hourGlass.twist(assemblyProtocol);
        assertTrue(hourGlass.isFinished());
        assertEquals(3, hourGlass.getState());
    }

}