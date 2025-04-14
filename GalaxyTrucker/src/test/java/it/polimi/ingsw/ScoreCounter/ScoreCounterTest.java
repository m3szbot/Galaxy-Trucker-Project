package it.polimi.ingsw.ScoreCounter;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ScoreCounterTest {
    /* 1 public method: getPlayerScore (+ constructor)
     scoreCounter must be initialized in each test
     after assigning custom playerList and playerOrderList
     */
    ScoreCounter scoreCounter;
    List<Player> playerList;
    List<Player> playerOrderList;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);

        playerList = new ArrayList<>();
        playerOrderList = new ArrayList<>();
    }

    @Test
    void onePlayerEmptyShip() {
        playerList.add(playerA);
        playerOrderList.add(playerA);
        scoreCounter = new ScoreCounter(GameType.NormalGame, playerList, playerOrderList);
        // score: 4 + 8
        assertEquals(12, scoreCounter.getPlayerScore(playerA));
    }

}