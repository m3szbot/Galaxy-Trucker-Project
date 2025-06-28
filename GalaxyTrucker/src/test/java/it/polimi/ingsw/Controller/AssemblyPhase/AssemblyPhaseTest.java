package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
        assertTrue(player.getIsConnected());
    }

/*
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

 */

    @Test
    void executeAllCommands() {
        Mocker.simulateClientInput("draw\nrotate\nbook\ndraw\nplace booked\n0\nplace\n7 8\nchoose\n0\nplace\n7 6\nturn\nshow deck\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
    }

    @Test
    void rotateComponent() {
        Mocker.simulateClientInput("rotate\ndraw\nrotate\nrotate\nrotate\nrotate\nrotate\nrotate\nplace\n7 6\nrotate\nbook\nrotate\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
    }

    @Test
    void drawComponent() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 500; i++) {
            input.append("draw\n");
        }
        input.append("end\n1\n");

        Mocker.simulateClientInput(input.toString());

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
    }

    @Test
    void placeBookedComponent() {
        Mocker.simulateClientInput("place booked\n1\n place booked\n0\nplace booked\n-1\ndraw\nplace booked\n0\ndraw\nbook\ndraw\nbook\nplace booked\n0\nplace booked\n1\nplace booked\n2\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
    }

}
