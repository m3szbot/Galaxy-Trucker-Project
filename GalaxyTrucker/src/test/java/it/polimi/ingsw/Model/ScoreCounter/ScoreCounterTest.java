package it.polimi.ingsw.Model.ScoreCounter;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.Components.Storage;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// TODO players with shipBoards (goods, exposed links, lost components)
class ScoreCounterTest {
    // 2 public methods: constructor, getPlayerScore
    // add players to gameInformation.flightBoard
    ScoreCounter scoreCounter;
    GameInformation gameInformation;
    FlightBoard flightBoard;
    Player playerA, playerB, playerC, playerD;
    SideType[] singleSides;

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

        singleSides = new SideType[]{SideType.SINGLE, SideType.SINGLE, SideType.SINGLE, SideType.SINGLE};
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

    @Test
    void OneOfEachGoodScore() {
        // 8 order points
        // 4 least exposed links points?
        // 4 3 2 1 goods points
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        Storage storage = new Storage(singleSides, true, 4);
        playerA.getShipBoard().addComponent(storage, 7, 8);
        storage.addGoods(new int[]{1, 1, 1, 1});
        // check correct shipboard and goods
        assertFalse(playerA.getShipBoard().isErroneous());
        assertEquals(1, playerA.getShipBoard().getShipBoardAttributes().getGoods()[0]);
        assertEquals(1, playerA.getShipBoard().getShipBoardAttributes().getGoods()[1]);
        assertEquals(1, playerA.getShipBoard().getShipBoardAttributes().getGoods()[2]);
        assertEquals(1, playerA.getShipBoard().getShipBoardAttributes().getGoods()[3]);

        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(22, scoreCounter.getPlayerScore(playerA));
    }

    @Test
    void LostOneComponentScores() {
        // add and remove 1 component
        // 8 order + 4 least links - 1 component
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        Component component = new Component(singleSides);
        playerA.getShipBoard().addComponent(component, 7, 8);
        playerA.getShipBoard().removeComponent(7, 8, false);
        assertEquals(1, playerA.getShipBoard().getShipBoardAttributes().getDestroyedComponents());
        scoreCounter.calculatePlayerScores(flightBoard);
        assertEquals(11, scoreCounter.getPlayerScore(playerA));

    }

}