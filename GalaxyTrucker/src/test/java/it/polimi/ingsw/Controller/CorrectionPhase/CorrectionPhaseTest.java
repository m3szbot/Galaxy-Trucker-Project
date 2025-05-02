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

// TODO test removal of more than 1 components

class CorrectionPhaseTest {
    private final InputStream originalInput = System.in;
    GameInformation gameInformation;
    CorrectionPhase correctionPhase;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        // create correctionPhase after simulating input!
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
        gameInformation.addPlayers(playerA);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

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

    @Test
    void start() {
        correctionPhase.start(gameInformation);
    }

    /**
     * playerA has 3 erroneous components, removes the one connecting them to the shipboard so others fall of
     * and shipboard becomes valid - player A doesn't get removed
     */
    @Test
    void RemoveOneComponent() {
        int errors;
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        errors = playerA.getShipBoard().checkErrors();
        assertEquals(3, errors);

        // correct playerA's shipboard
        inputSimulator("7\n8\n");
        correctionPhase.start(gameInformation);
        errors = playerA.getShipBoard().checkErrors();
        assertEquals(0, errors);
        assertEquals(4, gameInformation.getPlayerList().size());
    }

    // TODO shipboard check errors not working?
    @Test
    void RemoveTwoComponents() {
        int errors;
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        errors = playerA.getShipBoard().checkErrors();
        assertEquals(3, errors);

        // correct playerA's shipboard
        inputSimulator("6\n8\n");
        correctionPhase.start(gameInformation);

        assertEquals(4, gameInformation.getPlayerList().size());
    }

    /**
     * playerA has erroneous shipboard, times out (2 minutes) and gets removed from the game
     */
    @Test
    void kickPlayerErroneousShipboard() {
        int errors;
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 7, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 6, 8);
        playerA.getShipBoard().addComponent(new Component(new SideType[]{SideType.Universal, SideType.Single, SideType.Smooth, SideType.Double}), 8, 8);
        errors = playerA.getShipBoard().checkErrors();
        assertEquals(3, errors);

        correctionPhase.start(gameInformation);
        assertEquals(3, gameInformation.getPlayerList().size());

    }

}