package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FlightPhaseTest {
    Game game;
    FlightPhase flightPhase;
    Player player;
    FlightBoard flightBoard;


    @BeforeEach
    void setup() throws IllegalSelectionException {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        flightPhase = game.getFlightPhase();
        player = Mocker.getFirstPlayer();
        flightBoard = game.getGameInformation().getFlightBoard();
        flightBoard.addPlayer(player, 1);

        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void timeoutNoInput() {
        flightPhase.start();

        assertEquals(0, game.getGameInformation().getPlayerList().size());
        assertFalse(flightBoard.isInFlight(player));
        assertFalse(player.getIsConnected());
    }
}