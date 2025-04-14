package it.polimi.ingsw.FlightBoard;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardBuilder;
import it.polimi.ingsw.Cards.Sabotage;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class FlightBoardTest {
    // common fields
    FlightBoard flightBoard;
    GameInformation gameInformation;
    List<Player> playerOrderList;
    Player playerA, playerB, playerC, playerD;

    // reassigns field before each test
    @BeforeEach
    void setUp() {
        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);

        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerB = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        List<Card> cardList = new ArrayList<>();
        Card card = new Sabotage(new CardBuilder());
        for (int i = 0; i < 15; i++) {
            cardList.add(card);
        }

        flightBoard = new FlightBoard(GameType.NormalGame, cardList);

        playerOrderList = new ArrayList<>();
    }

    @Test
    void addOnePlayer() {
        playerOrderList.add(playerA);
        flightBoard.addPlayer(playerA, 1);
        assertEquals(1, flightBoard.getPlayerOrder(playerA));
        assertEquals(1, flightBoard.getPlayerTile(playerA));
        assertEquals(playerOrderList, flightBoard.getPlayerOrderList());
    }

    @Test
    void nextIncrementTilesOccupied() {
        // next tile and tile after that both occupied
        flightBoard.addPlayer(playerA, 1);
        flightBoard.addPlayer(playerB, 3);
        flightBoard.addPlayer(playerC, 4);
        flightBoard.incrementPlayerTile(playerA, 2);
        playerOrderList.add(playerA);
        playerOrderList.add(playerC);
        playerOrderList.add(playerB);
        assertEquals(5, flightBoard.getPlayerTile(playerA));
        assertEquals(playerOrderList, flightBoard.getPlayerOrderList());
    }

    @Test
    void nextDecrementTilesOccupied() {
        // next tile and tile before that both occupied
        flightBoard.addPlayer(playerA, 6);
        flightBoard.addPlayer(playerB, 3);
        flightBoard.addPlayer(playerC, 4);
        flightBoard.incrementPlayerTile(playerA, -2);
        playerOrderList.add(playerC);
        playerOrderList.add(playerB);
        playerOrderList.add(playerA);
        assertEquals(2, flightBoard.getPlayerTile(playerA));
        assertEquals(playerOrderList, flightBoard.getPlayerOrderList());
    }

    @Test
    void removeFirstPLayer() {
        flightBoard.addPlayer(playerA, 10);
        flightBoard.addPlayer(playerB, 5);
        playerOrderList.add(playerA);
        playerOrderList.add(playerB);
        assertEquals(playerOrderList, flightBoard.getPlayerOrderList());
        flightBoard.removePlayer(playerA);
        playerOrderList.remove(playerA);
        assertEquals(playerOrderList, flightBoard.getPlayerOrderList());
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerTile(playerA);
        });
    }

    @Test
    void removeLappedPlayer() {
        // normal game: 24 tiles
        // playerA steps on playerB lapping him
        flightBoard.addPlayer(playerA, 1);
        flightBoard.addPlayer(playerB, 2);
        flightBoard.incrementPlayerTile(playerA, 25);
        assertEquals(27, flightBoard.getPlayerTile(playerA));
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerTile(playerB);
        });
        assertThrows(NoSuchElementException.class, () -> {
            flightBoard.getPlayerOrder(playerB);
        });
    }

    @Test
    void addDuplicatePlayers() {
        flightBoard.addPlayer(playerA, 1);
        assertThrows(IllegalArgumentException.class, () -> {
            flightBoard.addPlayer(playerA, 10);
        });
    }

    @Test
    void AddExhaustGoods() {
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

}