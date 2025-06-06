package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ServerSide.Messengers.ClientMessenger;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

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
        gameInformation.addPlayers(playerA);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

        singleComponent = new Component(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single});
        doubleComponent = new Component(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double});

        correctionPhase = new CorrectionPhase(gameInformation);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(originalInput);
    }

    @Test
    void start() {
        correctionPhase.start();
    }

    /**
     * playerA has 3 erroneous components, removes the one connecting them to the shipboard so others fall of
     * and shipboard becomes valid - player A doesn't get removed
     */
    @Test
    void RemoveOneComponent() {
        try {
            playerA.getShipBoard().addComponent(singleComponent, 7, 8);
            playerA.getShipBoard().addComponent(doubleComponent, 6, 8);
            playerA.getShipBoard().addComponent(doubleComponent, 8, 8);
        } catch (NotPermittedPlacementException e) {
        }
        assertTrue(playerA.getShipBoard().isErroneous());

        // correct playerA's shipboard
        inputSimulator("7\n8\n");
        correctionPhase.start();
        assertFalse(playerA.getShipBoard().isErroneous());
        assertEquals(4, gameInformation.getPlayerList().size());
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

    /**
     * playerA has erroneous shipboard, times out (2 minutes) and gets removed from the game
     */
    @Test
    void kickPlayerErroneousShipboard() {
        try {
            playerA.getShipBoard().addComponent(singleComponent, 7, 8);
            playerA.getShipBoard().addComponent(doubleComponent, 6, 8);
        } catch (NotPermittedPlacementException e) {
        }
        assertTrue(playerA.getShipBoard().isErroneous());

        correctionPhase.start();
        assertEquals(3, gameInformation.getPlayerList().size());

    }

}