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
import org.junit.jupiter.api.Test;

public class ExceptionsHandlerTest {
    // GAME 1
    Game game1 = Mocker.mockNormalGame1Player();
    CorrectionPhase correctionPhase1 = game1.getCorrectionPhase();
    Player player1 = game1.getGameInformation().getPlayerList().getFirst();
    ShipBoard shipBoard1 = player1.getShipBoard();

    // GAME 2
    Game game2 = Mocker.mockNormalGame2Players();
    CorrectionPhase correctionPhase2 = game2.getCorrectionPhase();
    Player player2 = game2.getGameInformation().getPlayerList().get(1);
    FlightBoard flightBoard2 = game2.getGameInformation().getFlightBoard();

    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @Test
    void testFracturedShipBoardException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard1.addComponent(smoothComponent, 6, 7);
        shipBoard1.addComponent(smoothComponent, 6, 8);
        shipBoard1.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 8);

        Mocker.simulateClientInput("6 7\n1\n");
        correctionPhase1.start();

    }

    @Test
    void testNoHumanCrewLeftException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard1.addComponent(smoothComponent, 6, 7);

        Mocker.simulateClientInput("7 7\n");
        correctionPhase1.start();
    }

    @Test
    void testLappedPlayerException() {
        // check lapped players - not possible in correctionPhase
    }
}
