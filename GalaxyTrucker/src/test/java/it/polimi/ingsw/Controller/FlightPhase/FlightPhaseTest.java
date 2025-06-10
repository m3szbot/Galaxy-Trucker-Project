package it.polimi.ingsw.Controller.FlightPhase;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FlightPhaseTest {
    GameInformation gameInformation;
    Player playerA, playerB, playerC, playerD;
    /*
    initialize flightPhase only after setting simulated input!
    creates flightView and scanner in it!
     */
    FlightPhase flightPhase;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NORMALGAME);
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        // add players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        gameInformation.setMaxNumberOfPlayers(4);
        gameInformation.addPlayer(playerA);
        gameInformation.addPlayer(playerB);
        gameInformation.addPlayer(playerC);
        gameInformation.addPlayer(playerD);

    }

    @Test
    public void start() {
        flightPhase = new FlightPhase(gameInformation);
    }
}