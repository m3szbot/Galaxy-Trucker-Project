package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EvaluationPhaseTest {
    Game game;
    EvaluationPhase evaluationPhase;
    Player player;
    ShipBoard shipBoard;
    FlightBoard flightBoard;

    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        player = Mocker.getFirstPlayer();
        shipBoard = player.getShipBoard();
        evaluationPhase = game.getEvaluationPhase();
        flightBoard = Mocker.getGameInformation().getFlightBoard();
    }

    @Test
    void testNoPlayersOnFlightBoard() {
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }
    }

    @Test
    void test1PlayerOnFlightBoard() throws IllegalSelectionException {
        flightBoard.addPlayer(player, 1);
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }

        assertEquals(12, shipBoard.getShipBoardAttributes().getCredits());
    }

    @Test
    void addGainedCreditsToExistingCredits() throws IllegalSelectionException {
        shipBoard.getShipBoardAttributes().addCredits(10);
        flightBoard.addPlayer(player, 1);
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }

        // 10 + 12
        assertEquals(22, shipBoard.getShipBoardAttributes().getCredits());
    }

    @Test
    void eliminatedPlayer() throws IllegalSelectionException {
        flightBoard.addPlayer(player, 1);
        flightBoard.eliminatePlayer(player);
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }

        // DNF
        assertEquals(0, shipBoard.getShipBoardAttributes().getCredits());
    }

    @Test
    void disconnectedPlayer() throws IllegalSelectionException {
        flightBoard.addPlayer(player, 1);
        flightBoard.eliminatePlayer(player);
        game.getGameInformation().disconnectPlayerInGameInformation(player);
        try {
            evaluationPhase.start();
        } catch (Exception e) {
        }

        // DNF
        assertEquals(0, shipBoard.getShipBoardAttributes().getCredits());
    }
}