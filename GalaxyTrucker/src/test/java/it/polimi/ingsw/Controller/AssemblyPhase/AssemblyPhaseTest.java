package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

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

    @Test
    void placeComponent() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        input.append("draw\n");
        for (int i = 0; i < 100; i++) {
            input.append(String.format("place\n%d %d\n", ThreadLocalRandom.current().nextInt(-10, 20), ThreadLocalRandom.current().nextInt(-10, 20)));
        }
        input.append("end\n1\n");


        Mocker.simulateClientInput(input.toString());
        assemblyPhase.start();
        assertTrue(player.getIsConnected());
    }

}
