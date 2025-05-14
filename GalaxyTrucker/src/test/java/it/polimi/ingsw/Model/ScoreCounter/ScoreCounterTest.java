package it.polimi.ingsw.Model.ScoreCounter;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

// TODO players with shipBoards (goods, exposed links, lost components)
class ScoreCounterTest {
    // 2 public methods: constructor, getPlayerScore
    // add players to gameInformation.flightBoard
    ScoreCounter scoreCounter;
    GameInformation gameInformation;
    FlightBoard flightBoard;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        // re-setup gameInformation, flightBoard, scoreCounter for test game
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        scoreCounter = new ScoreCounter(gameInformation.getGameType());
        flightBoard = gameInformation.getFlightBoard();

        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
    }

    @Test
    void onePlayerEmptyShip() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        scoreCounter.calculatePlayerScores(flightBoard);
        // score: 4 + 8
        assertEquals(12, scoreCounter.getPlayerScore(playerA));
    }

    @Test
    void fourPlayersEmptyShip() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerC, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerD, flightBoard.getStartingTiles().getLast());
        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(12, scoreCounter.getPlayerScore(playerA));
        assertEquals(10, scoreCounter.getPlayerScore(playerB));
        assertEquals(8, scoreCounter.getPlayerScore(playerC));
        assertEquals(6, scoreCounter.getPlayerScore(playerD));

    }

    @Test
    void OneEliminatedPlayerEmptyShip() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        flightBoard.eliminatePlayer(playerA);
        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(0, scoreCounter.getPlayerScore(playerA));
    }

    @Test
    void OneGaveUpPlayerEmptyShip() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        flightBoard.giveUpPlayer(playerA);
        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(0, scoreCounter.getPlayerScore(playerA));
    }

    @Test
    void getNonPresentPlayer() {
        scoreCounter.calculatePlayerScores(flightBoard);
        assertThrows(IllegalArgumentException.class, () -> {
            scoreCounter.getPlayerScore(playerA);
        });
    }

    @Test
    void testGameOnePlayerEmptyShip() {
        gameInformation.setUpGameInformation(GameType.TESTGAME, 4);
        scoreCounter = new ScoreCounter(gameInformation.getGameType());
        flightBoard = gameInformation.getFlightBoard();
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(6, scoreCounter.getPlayerScore(playerA));

    }

}