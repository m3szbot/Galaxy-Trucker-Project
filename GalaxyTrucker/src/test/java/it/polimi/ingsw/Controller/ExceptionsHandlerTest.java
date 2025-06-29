package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.CrewType;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Run tests individually
 */
public class ExceptionsHandlerTest {
    Game game;
    CorrectionPhase correctionPhase;
    Player player;
    ShipBoard shipBoard;
    FlightBoard flightBoard;


    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @BeforeEach
    void setup() throws IllegalSelectionException {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        player = Mocker.getFirstPlayer();
        shipBoard = player.getShipBoard();
        flightBoard = game.getGameInformation().getFlightBoard();
        correctionPhase = game.getCorrectionPhase();

        flightBoard.addPlayer(player, 1);
    }

    @Test
    void checkSetup() {
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void testFracturedShipBoardException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 6, 7);
        shipBoard.addComponent(smoothComponent, 6, 8);
        shipBoard.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 8);

        Mocker.simulateClientInput("6 7\n1\n");
        correctionPhase.start();
        assertTrue(flightBoard.isInFlight(player));
    }

    @Test
    void testNoHumanCrewLeftException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 6, 7);

        Mocker.simulateClientInput("7 7\n");
        correctionPhase.start();
        assertFalse(flightBoard.isInFlight(player));
    }

}
