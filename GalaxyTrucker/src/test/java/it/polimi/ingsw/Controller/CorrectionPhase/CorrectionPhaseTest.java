package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CorrectionPhaseTest {
    private final InputStream originalInput = System.in;
    GameInformation gameInformation;
    CorrectionPhase correctionPhase;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        try {
            gameInformation.setUpComponents();
        } catch (IOException e) {
        }
        // add players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        gameInformation.setMaxNumberOfPlayers(4);
        // add only 1 player as starting point
        gameInformation.addPlayers(playerA);

        correctionPhase = new CorrectionPhase(gameInformation);
    }

    @AfterEach
    void restoreInput() {
        System.setIn(originalInput);
    }

    /**
     * Simulates keyboard input for automated tests (inputs not permitted)
     * To call before scanner is created
     *
     * @param input
     */
    void inputSimulator(String input) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        System.setIn(inputStream);
    }

    @Test
    void start() {
        correctionPhase.start(gameInformation);
    }

    @Test
    void RemoveOneComponent() {
        int errors;
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        errors = playerA.getShipBoard().checkErrors();
        assertEquals(errors, 3);

        inputSimulator("a\n");
        correctionPhase.start(gameInformation);
    }


}