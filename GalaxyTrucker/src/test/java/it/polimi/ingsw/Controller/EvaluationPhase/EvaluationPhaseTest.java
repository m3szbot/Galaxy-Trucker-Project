package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

class EvaluationPhaseTest {
    FlightBoard flightBoard;
    EvaluationPhase evaluationPhase;
    GameInformation gameInformation;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        try {
            gameInformation.setUpComponents();
            gameInformation.setUpCards(gameInformation.getGameType());
        } catch (IOException e) {
        }
        // add players
        Player playerA, playerB, playerC, playerD;
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        gameInformation.setMaxNumberOfPlayers(4);
        gameInformation.addPlayers(playerA);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

        // set up flightBoard
        gameInformation.setUpFlightBoard();
        gameInformation.getFlightBoard().addPlayer(playerA, 1);
        gameInformation.getFlightBoard().addPlayer(playerB, 2);
        gameInformation.getFlightBoard().addPlayer(playerC, 3);
        gameInformation.getFlightBoard().addPlayer(playerD, 4);

        // set up evaluationPhase
        evaluationPhase = new EvaluationPhase();
    }

    /**
     * player points: finish order + least exposed links (2)
     * tests base case: empty ships, only finish order set
     * timeout (15s)
     */
    @Test
    public void start() {
        evaluationPhase.start(gameInformation);
    }
}