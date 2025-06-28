package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AssemblyPhaseTest {

    Game game;
    AssemblyPhase assemblyPhase;
    Player player;


    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        assemblyPhase = game.getAssemblyPhase();
        player = Mocker.getFirstPlayer();
    }

    @Test
    void drawPlaceEnd() {
        Mocker.simulateClientInput("draw\nplace\n7 8\nend\n1\n");

        assemblyPhase.start();
    }


    @Test
    public void bookComponent() {
        assemblyPhase.start();
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("draw");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersBookedComponents().get(player));
        Mocker.simulateClientInput("book");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersBookedComponents().get(player));
        Mocker.simulateClientInput("place booked");
        Mocker.simulateClientInput("0");
        Mocker.simulateClientInput("67 67");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testComponentPlacing() {
        assemblyPhase.start();
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("draw");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("45 67");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("6 6");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("7 6");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(player.getShipBoard().getComponent(7, 6));
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
    }

    @Test
    void executeAllCommands() {
        Mocker.simulateClientInput("draw\nrotate\nbook\nplace booked\n1\nchoose\n0\nplace\n7 6\nturn\nshow deck\nend\n1\n");

        assemblyPhase.start();
    }

}
