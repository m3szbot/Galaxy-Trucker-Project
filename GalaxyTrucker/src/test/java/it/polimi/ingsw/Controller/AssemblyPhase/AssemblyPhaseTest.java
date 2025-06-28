package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Commands:
 * place
 * draw
 * rotate
 * choose
 * book
 * place booked
 * turn
 * show
 * end
 */
public class AssemblyPhaseTest {

    Game game;
    AssemblyPhase assemblyPhase;
    Player player;
    FlightBoard flightBoard;


    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        assemblyPhase = game.getAssemblyPhase();
        player = Mocker.getFirstPlayer();
        flightBoard = game.getGameInformation().getFlightBoard();
    }

    @Test
    void drawPlaceEnd() {
        Mocker.simulateClientInput("draw\nplace\n7 8\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void drawBookPlaceBooked() {
        Mocker.simulateClientInput("draw\nbook\nplace booked\n0\n7 8\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
        assertNotNull(player.getShipBoard().getComponent(7, 8));
    }


    @Test
    void executeAllCommands() {
        Mocker.simulateClientInput("draw\nrotate\nbook\ndraw\nplace booked\n0\n7 8\nchoose\n0\nplace\n7 6\nturn\nshow deck\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
        assertNotNull(player.getShipBoard().getComponent(7, 8));
        assertNotNull(player.getShipBoard().getComponent(7, 6));
    }

    @Test
    void rotateComponent() {
        Mocker.simulateClientInput("rotate\ndraw\nrotate\nrotate\nrotate\nrotate\nrotate\nrotate\nplace\n7 6\nrotate\nbook\nrotate\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void drawComponent() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        // ~150 components
        for (int i = 0; i < 400; i++) {
            input.append("draw\n");
        }
        input.append("end\n1\n");

        Mocker.simulateClientInput(input.toString());

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void placeBookedComponent() {
        Mocker.simulateClientInput("place booked\n1\n place booked\n0\nplace booked\n-1\ndraw\nplace booked\n0\ndraw\nbook\ndraw\nbook\nplace booked\n0\nplace booked\n1\nplace booked\n2\nend\n1\n");

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
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
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void chooseUncovered() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        input.append("draw\n");
        for (int i = 0; i < 250; i++) {
            input.append(String.format("draw\nchoose\n%d\n", ThreadLocalRandom.current().nextInt(-100, 200)));
        }
        input.append("end\n1\n");

        Mocker.simulateClientInput(input.toString());
        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void bookPlaceBooked() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 250; i++) {
            input.append("draw\nbook\n");
            if (i % 2 == 1) {
                input.append(String.format("place booked\n%d\n%d %d\n", ThreadLocalRandom.current().nextInt(-2, 5), ThreadLocalRandom.current().nextInt(-10, 20), ThreadLocalRandom.current().nextInt(-10, 20)));
            }
        }
        input.append("end\n1\n");

        Mocker.simulateClientInput(input.toString());
        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void showDeck() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 100; i++) {
            input.append(String.format("show\n%d\nyes\n", ThreadLocalRandom.current().nextInt(-2, 5)));
        }
        input.append("end\n1\n");

        Mocker.simulateClientInput(input.toString());
        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void shortRandomInputStressTest() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            input.append(randomCommand());
        }
        input.append("end\n1\n");
        Mocker.simulateClientInput(input.toString());

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    private String randomCommand() {
        int command = ThreadLocalRandom.current().nextInt(0, 8);

        // indexes from -2 to simulate wrong selection input
        switch (command) {
            case 0 -> {
                // place
                return String.format("place\n%d %d\n", ThreadLocalRandom.current().nextInt(-10, 20), ThreadLocalRandom.current().nextInt(-10, 20));
            }
            case 1 -> {
                // draw
                return String.format("draw\n");
            }
            case 2 -> {
                // rotate
                return String.format("rotate\n");
            }
            case 3 -> {
                // choose uncovered
                return String.format("choose\n%d\n", ThreadLocalRandom.current().nextInt(-100, 200));
            }
            case 4 -> {
                // book
                return String.format("book\n");
            }
            case 5 -> {
                // place booked
                return String.format("place booked\n%d\n%d %d\n", ThreadLocalRandom.current().nextInt(-5, 8), ThreadLocalRandom.current().nextInt(-100, 200), ThreadLocalRandom.current().nextInt(-100, 200));
            }
            case 6 -> {
                // turn
                return String.format("turn\n");
            }
            case 7 -> {
                // show deck
                return String.format("show\n%d\nyes\n", ThreadLocalRandom.current().nextInt(-5, 8));
            }
        }
        return "";
    }

    @Test
    void longRandomInputStressTest() {
        ClientInputManager.setTimeOut(100000);
        StringBuilder input = new StringBuilder();

        for (int i = 0; i < 500; i++) {
            input.append(randomCommand());
        }
        input.append("end\n1\n");
        Mocker.simulateClientInput(input.toString());

        assemblyPhase.start();
        assertTrue(player.getIsConnected());
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void timeoutNoInput() {
        ClientInputManager.setTimeOut(110);
        assemblyPhase.start();
        assertFalse(player.getIsConnected());
    }

    @Test
    void timeoutPlace() {
        ClientInputManager.setTimeOut(110);
        Mocker.simulateClientInput("place\n");
        assemblyPhase.start();
        assertFalse(player.getIsConnected());
    }

    @Test
    void timeoutChooseUncovered() {
        ClientInputManager.setTimeOut(110);
        Mocker.simulateClientInput("choose\n");
        assemblyPhase.start();
        assertFalse(player.getIsConnected());
    }
}
