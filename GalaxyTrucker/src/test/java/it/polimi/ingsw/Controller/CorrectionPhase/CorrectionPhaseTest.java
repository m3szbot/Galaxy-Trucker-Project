package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrectionPhaseTest {
    Game game;
    CorrectionPhase correctionPhase;
    Player player;
    ShipBoard shipBoard;
    FlightBoard flightBoard;

    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @BeforeEach
    void setUp() throws IllegalSelectionException {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        correctionPhase = game.getCorrectionPhase();
        player = Mocker.getFirstPlayer();
        shipBoard = player.getShipBoard();
        flightBoard = game.getGameInformation().getFlightBoard();

        flightBoard.addPlayer(player, 1);
    }

    @Test
    void checkSetup() {
        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }

    @Test
    void noInputStarterShipBoard() {
        correctionPhase.start();
        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }


    @Test
    void shipBoardWith1ErrorCorrected() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 7, 8);
        Mocker.simulateClientInput("7 8\n");
        correctionPhase.start();
        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }


    @Test
    void shipBoardWith2ErrorsCorrected() throws NotPermittedPlacementException, IllegalSelectionException {
        // test removing 2 components
        shipBoard.addComponent(smoothComponent, 7, 6);
        shipBoard.addComponent(smoothComponent, 7, 8);
        Mocker.simulateClientInput("7 6\n7 8\n");
        correctionPhase.start();
        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }


    @Test
    void exhaustErrorCorrectionTrialsWithError() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 7, 8);
        // exhaust error trials and input trials
        Mocker.simulateClientInput("1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n1 1\n");
        correctionPhase.start();
        assertEquals(0, flightBoard.getPlayerOrderList().size());
    }

    @Test
    void timeoutDuringErrorCorrectionWithError() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 7, 8);

        // no input for timeout
        assertNull(ClientInputManager.getSimulatedInput());

        correctionPhase.start();

        assertEquals(0, flightBoard.getPlayerOrderList().size());
    }


    @Test
    void selectCrewType() throws NotPermittedPlacementException, IllegalSelectionException {
        // test alien selected 2x, no support nearby, wrong inputs until trials exhausted (5)
        shipBoard.addComponent(6, 7, new AlienSupport(universalSides, true));
        shipBoard.addComponent(8, 7, new AlienSupport(universalSides, false));
        shipBoard.addComponent(6, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        Mocker.simulateClientInput("Brown\nPurple\npurple\n!\n\na\n1\nhuman\nbrown");
        correctionPhase.start();

        assertEquals(1, flightBoard.getPlayerOrderList().size());
        assertTrue(shipBoard.getShipBoardAttributes().getPurpleAlien());
        assertTrue(shipBoard.getShipBoardAttributes().getBrownAlien());
    }

    @Test
    void exhaustCrewSelectionTrials() throws NotPermittedPlacementException, IllegalSelectionException {
        // test alien selected 2x, no support nearby, wrong inputs until trials exhausted (5)
        shipBoard.addComponent(6, 7, new AlienSupport(universalSides, true));
        shipBoard.addComponent(8, 7, new AlienSupport(universalSides, false));
        shipBoard.addComponent(6, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));
        Mocker.simulateClientInput("a\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\na\n");
        correctionPhase.start();

        assertEquals(1, flightBoard.getPlayerOrderList().size());
        assertFalse(shipBoard.getShipBoardAttributes().getPurpleAlien());
        assertFalse(shipBoard.getShipBoardAttributes().getBrownAlien());
    }


    @Test
    void timeoutDuringCrewSelection() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(6, 7, new AlienSupport(universalSides, true));
        shipBoard.addComponent(8, 7, new AlienSupport(universalSides, false));
        shipBoard.addComponent(6, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(9, 7, new Cabin(universalSides, CrewType.Human, 2));

        // no input for timeout
        assertNull(ClientInputManager.getSimulatedInput());

        correctionPhase.start();

        assertEquals(0, flightBoard.getPlayerOrderList().size());
    }

    @Test
    void fracturedShipBoardWithNoErrors() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(6, 7, new Component(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth}));
        shipBoard.addComponent(6, 8, new Cabin(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Universal}, CrewType.Human, 2));
        shipBoard.addComponent(5, 8, new Component(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth}));

        Mocker.simulateClientInput("0\n");
        correctionPhase.start();

        assertEquals(1, flightBoard.getPlayerOrderList().size());
    }

    @Test
    void timeoutDuringFracturedShipBoardWithNoErrors() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(6, 7, new Component(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth}));
        shipBoard.addComponent(6, 8, new Cabin(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Universal}, CrewType.Human, 2));
        shipBoard.addComponent(5, 8, new Component(new SideType[]{SideType.Smooth, SideType.Universal, SideType.Smooth, SideType.Smooth}));

        correctionPhase.start();

        assertEquals(0, flightBoard.getPlayerOrderList().size());
    }


}