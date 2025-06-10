package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class CorrectionPhaseTest {
    private final InputStream originalInput = System.in;

    GameInformation gameInformation;

    CorrectionPhase correctionPhase;
    Player playerA, playerB, playerC, playerD;
    Component singleComponent, doubleComponent;

    @BeforeEach
    void setUp() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        // set up GameMessenger used by Phase
        ClientMessenger.addGame(gameInformation.getGameCode());

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

        singleComponent = new Component(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single});
        doubleComponent = new Component(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double});

        correctionPhase = new CorrectionPhase(gameInformation);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(originalInput);
    }


    /**
     * Simulates keyboard input for automated tests (inputs not permitted)
     * The scanner is created only after calling correctionPhase.start()
     * Works only if there is 1 scanner instance in the method
     *
     * @param input
     */
    void inputSimulator(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }


}