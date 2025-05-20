package it.polimi.ingsw.Controller.EvaluationPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EvaluationPhaseTest {
    GameInformation gameInformation;

    FlightBoard flightBoard;
    EvaluationPhase evaluationPhase;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        // set up GameMessenger used by Phase
        ClientMessenger.addGame(gameInformation.getGameCode());

        // add players
        Player playerA, playerB, playerC, playerD;
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        gameInformation.addPlayers(playerA);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

        // set up flightBoard
        flightBoard = gameInformation.getFlightBoard();
        flightBoard.addPlayer(playerA, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerB, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerC, flightBoard.getStartingTiles().getFirst());
        flightBoard.addPlayer(playerD, flightBoard.getStartingTiles().getFirst());


        // set up evaluationPhase
        evaluationPhase = new EvaluationPhase(gameInformation);
    }

    /**
     * player points: finish order + least exposed links (2)
     * tests base case: empty ships, only finish order set
     * timeout (15s)
     */
    @Test
    public void start() {
        evaluationPhase.start();
    }
}