package it.polimi.ingsw.Model.FlightBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

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

    @BeforeEach
    void setUp() {
        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);

        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        // set up gameInformation
        GameInformation gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NormalGame, 4);

        flightBoard = new FlightBoard(GameType.NormalGame, gameInformation.getCardsList());
    }

    @Test
    void TestSetUp() {
        assertEquals(12, flightBoard.getCardsNumber());
    }

    @Test
    void addOnePlayer() {
        int tile = flightBoard.getStartingTiles().getLast();
        flightBoard.addPlayer(playerA, tile);
        assertEquals(tile, flightBoard.getPlayerTile(playerA));
        assertEquals(1, flightBoard.getPlayerOrder(playerA));
        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }

    @Test
    void nextIncrementTileOccupied() {
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
    void nextIncrementTilesOccupied() {
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
    void nextDecrementTileOccupied() {
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
    void eliminatePLayer() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.eliminatePlayer(playerA);
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerTile(playerA);
        });
        assertEquals(0, flightBoard.getPlayerOrderList().size());
        assertEquals(playerA, flightBoard.getEliminatedList().getFirst());
    }

    @Test
    void removeLappedPlayer() {
        // normal game: 24 tiles
        // 1 2 4 7
        // playerA steps on (28) playerB (4) lapping him
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getLast());
        flightBoard.incrementPlayerTile(playerA, 21);
        assertEquals(7, flightBoard.getPlayerTile(playerA));
        assertEquals(4, flightBoard.getPlayerTile(playerA));
        flightBoard.updateFlightBoard();
        assertEquals(29, flightBoard.getPlayerTile(playerA));
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerTile(playerB);
        });
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerOrder(playerB);
        });
    }

    @Test
    void addDuplicatePlayers() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        assertThrows(IllegalArgumentException.class, () -> {
            flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        });
    }

    @Test
    void AddAndExhaustGoods() {
        // {12, 17, 13, 14}
        flightBoard.addGoods(new int[]{1, 1, 1, 1});
        // nothing is removed if limit exceeded
        assertThrows(IllegalArgumentException.class, () -> {
            flightBoard.removeGoods(new int[]{0, 0, 0, 100});
        });
        // set every good to 0 (fails on Exception)
        flightBoard.removeGoods(new int[]{13, 18, 14, 15});
        assertThrows(IllegalArgumentException.class, () -> {
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
    public void incrementEliminatedPlayer() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.eliminatePlayer(playerA);
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.incrementPlayerTile(playerA, 1);
        });
        assertEquals(1, flightBoard.getEliminatedList().size());
        assertTrue(flightBoard.isEliminated(playerA));
    }

    @Test
    public void incrementGaveUpPlayer() {
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getLast());
        flightBoard.giveUpPlayer(playerA);
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

    @Test
    void concurrentAddPlayer() {
        // add all players from different threads
        // the starting tiles should cause conflicts of occupation
        for (Player player : gameInformation.getPlayerList()) {
            Thread thread = new Thread(() -> {
                while (true) {
                    try {
                        flightBoard.addPlayer(player, flightBoard.getStartingTiles().getFirst());
                        break;
                    } catch (IndexOutOfBoundsException e) {
                        System.out.println("Tile chosen occupied, retrying");
                    }
                }

            });
        }
    }

}