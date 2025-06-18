package it.polimi.ingsw.Model.FlightBoard;

import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.TUI.TUIView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests:
 * in game: getPlayerOrder, getPlayerTile
 * eliminated: eliminatedList
 * gave up: gaveUpList
 * <p>
 * Normal game starting tiles: 1, 2, 4, 7
 * <p>
 * (test game: redo setup)
 */

class FlightBoardTest {
    // common fields
    FlightBoard flightBoard;
    GameInformation gameInformation;
    Player playerA, playerB, playerC, playerD;
    TUIView flightViewTUI;

    GameMessenger gameMessenger;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // set up players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        gameInformation.addPlayer(playerA);
        gameInformation.addPlayer(playerB);
        gameInformation.addPlayer(playerC);
        gameInformation.addPlayer(playerD);

        flightBoard = gameInformation.getFlightBoard();

        flightViewTUI = new TUIView();
    }

    @Test
    void testNormalSetUp() throws LappedPlayersException {
        assertEquals(4, gameInformation.getPlayerList().size());

        // flightboard
        flightBoard.updateFlightBoard();
        assertEquals(0, flightBoard.getPlayerOrderList().size());
        assertEquals(12, flightBoard.getCardsNumber());
        int levelOneCount = 0, levelTwoCount = 0;
        for (Card card : flightBoard.getCardsStack()) {
            if (card.getCardLevel() == 1)
                levelOneCount++;
            else
                levelTwoCount++;
        }
        assertEquals(4, levelOneCount);
        assertEquals(8, levelTwoCount);
    }

    @Test
    void testTestSetup() {
        // test new gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.TESTGAME, 4);
        gameInformation.addPlayer(playerA);
        gameInformation.addPlayer(playerB);
        gameInformation.addPlayer(playerC);
        gameInformation.addPlayer(playerD);
        assertEquals(GameType.TESTGAME, gameInformation.getGameType());
        assertEquals(4, gameInformation.getPlayerList().size());

        // test new flightBoard
        flightBoard = gameInformation.getFlightBoard();
        assertEquals(8, gameInformation.getCardsList().size());
        int levelOneCount = 0, levelTwoCount = 0;
        for (Card card : flightBoard.getCardsStack()) {
            if (card.getCardLevel() == 1)
                levelOneCount++;
            else
                levelTwoCount++;
        }
        assertEquals(8, levelOneCount);
        assertEquals(0, levelTwoCount);
    }

    @Test
    void addOnePlayer() throws IllegalSelectionException {
        assertFalse(flightBoard.isInGame(playerA));
        int tile = flightBoard.getStartingTiles().getLast();
        flightBoard.addPlayer(playerA, tile);
        assertEquals(tile, flightBoard.getPlayerTile(playerA));
        assertEquals(1, flightBoard.getPlayerOrder(playerA));
        assertEquals(1, flightBoard.getPlayerOrderList().size());
        assertTrue(flightBoard.isInGame(playerA));
    }

    @Test
    void nextIncrementTileOccupied() throws LappedPlayersException, IllegalSelectionException {
        // next tile occupied
        // 1 2 4 7
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerB, 3);
        flightBoard.updateFlightBoard();
        assertEquals(7, flightBoard.getPlayerTile(playerA));
        assertEquals(2, flightBoard.getPlayerOrder(playerA));
        assertEquals(8, flightBoard.getPlayerTile(playerB));
        assertEquals(1, flightBoard.getPlayerOrder(playerB));
    }

    @Test
    void nextIncrementTilesOccupied() throws LappedPlayersException, IllegalSelectionException {
        // next 2 tiles occupied
        // 1 2 4 7
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerC, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerB, 2);
        flightBoard.incrementPlayerTile(playerC, 4);
        flightBoard.updateFlightBoard();
        assertEquals(7, flightBoard.getPlayerTile(playerA));
        assertEquals(2, flightBoard.getPlayerOrder(playerA));
        assertEquals(6, flightBoard.getPlayerTile(playerB));
        assertEquals(3, flightBoard.getPlayerOrder(playerB));
        assertEquals(8, flightBoard.getPlayerTile(playerC));
        assertEquals(1, flightBoard.getPlayerOrder(playerC));
    }

    @Test
    void nextDecrementTileOccupied() throws LappedPlayersException, IllegalSelectionException {
        // next tile occupied
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerA, -3);
        flightBoard.updateFlightBoard();
        assertEquals(3, flightBoard.getPlayerTile(playerA));
        assertEquals(2, flightBoard.getPlayerOrder(playerA));
        assertEquals(4, flightBoard.getPlayerTile(playerB));
        assertEquals(1, flightBoard.getPlayerOrder(playerB));
    }

    @Test
    void eliminatePLayer() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.eliminatePlayer(playerA);
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerTile(playerA);
        });
        assertEquals(0, flightBoard.getPlayerOrderList().size());
        assertEquals(playerA, flightBoard.getEliminatedList().getFirst());
    }

    @Test
    void removeLappedPlayerOnUpdate() throws IllegalSelectionException {
        // normal game: 24 tiles
        // 1 2 4 7
        // playerA steps on (28->29) playerB (4) lapping him
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerA, 21);
        assertEquals(4, flightBoard.getPlayerTile(playerB));
        assertEquals(29, flightBoard.getPlayerTile(playerA));
        assertEquals(2, flightBoard.getPlayerOrderList().size());
        // playerB gets removed
        assertThrows(LappedPlayersException.class, () -> {
            flightBoard.updateFlightBoard();
        });
    }

    @Test
    void lapPlayerByFar() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerA, 100);
        assertThrows(LappedPlayersException.class, () -> {
            flightBoard.updateFlightBoard();
        });

    }

    @Test
    void addDuplicatePlayers() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        assertThrows(IllegalArgumentException.class, () -> {
            flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        });
    }

    @Test
    void AddAndExhaustGoods() throws IllegalSelectionException {
        // {12, 17, 13, 14}
        flightBoard.addGoods(new int[]{1, 1, 1, 1});
        // nothing is removed if limit exceeded
        assertThrows(IllegalSelectionException.class, () -> {
            flightBoard.removeGoods(new int[]{0, 0, 0, 100});
        });
        // set every good to 0 (fails on Exception)
        flightBoard.removeGoods(new int[]{13, 18, 14, 15});
        assertThrows(IllegalSelectionException.class, () -> {
            flightBoard.removeGoods(new int[]{0, 1, 0, 0});
        });
    }

    @Test
    void incrementNonPresentPlayer() {
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.incrementPlayerTile(playerA, 1);
        });
    }

    @Test
    void removeNonPresentPlayer() {
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.eliminatePlayer(playerA);
        });
    }

    @Test
    public void incrementEliminatedPlayer() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.eliminatePlayer(playerA);
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.incrementPlayerTile(playerA, 1);
        });
        assertEquals(1, flightBoard.getEliminatedList().size());
        assertTrue(flightBoard.isEliminated(playerA));
    }

    @Test
    public void incrementGaveUpPlayer() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.voluntarilyGiveUpPlayer(playerA);
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.incrementPlayerTile(playerA, 1);
        });
        assertEquals(1, flightBoard.getGaveUpList().size());
        assertTrue(flightBoard.isGaveUp(playerA));
    }

    @Test
    void getNewCard() {
        assertNotNull(flightBoard.getNewCard());
        assertEquals(11, flightBoard.getCardsNumber());
    }

    /**
     * Add 4 players from different threads concurrently.
     * All try to add to the first tile, which causes conflicts of occupation.
     */
    @RepeatedTest(1000)
    void testConcurrentAddPlayer() throws InterruptedException {
        // setup tests
        assertEquals(4, gameInformation.getPlayerList().size());
        assertEquals(0, flightBoard.getPlayerOrderList().size());

        // create threads
        ExecutorService executor = Executors.newFixedThreadPool(4);
        // the return list modified by the threads must be synchronized too!
        List<Player> order = Collections.synchronizedList(new ArrayList<>());

        // create adder threads
        for (Player player : gameInformation.getPlayerList()) {
            executor.submit((() -> {
                // thread task
                while (!flightBoard.isInGame(player)) {
                    int highestTile = flightBoard.getStartingTiles().getLast();
                    try {
                        // add player to highest starting tile (List in descending order)
                        flightBoard.addPlayer(player, highestTile);
                        // List: first added is first
                        order.add(player);
                    } catch (IllegalSelectionException e) {
                    }
                }
            }));
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        // result tests
        assertEquals(4, flightBoard.getPlayerOrderList().size());
        assertEquals(order, flightBoard.getPlayerOrderList());
        assertEquals(0, flightBoard.getStartingTiles().size());

    }

    @Test
    void addPlayersCheckOrder() throws IllegalSelectionException {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerC, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerD, flightBoard.getStartingTiles().getFirst());
        // playerOrderList sorted in descending order automatically after adding a player
        assertEquals(playerA, flightBoard.getPlayerOrderList().get(3));
        assertEquals(4, flightBoard.getPlayerOrder(playerA));
        assertEquals(playerB, flightBoard.getPlayerOrderList().get(2));
        assertEquals(3, flightBoard.getPlayerOrder(playerB));
        assertEquals(playerC, flightBoard.getPlayerOrderList().get(1));
        assertEquals(2, flightBoard.getPlayerOrder(playerC));
        assertEquals(playerD, flightBoard.getPlayerOrderList().get(0));
        assertEquals(1, flightBoard.getPlayerOrder(playerD));
    }

    @Test
    void printCards() {
        DataContainer dataContainer = new DataContainer();
        while (flightBoard.getCardsNumber() > 0) {
            dataContainer.setCard(flightBoard.getNewCard());
            flightViewTUI.printCard(dataContainer);
            dataContainer.clearContainer();
            // print line between cards
            dataContainer.setMessage("");
            flightViewTUI.printMessage(dataContainer);
        }
    }
}