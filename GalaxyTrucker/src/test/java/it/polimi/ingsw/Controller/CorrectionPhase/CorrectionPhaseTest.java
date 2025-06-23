package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrectionPhaseTest {
    Game game = Mocker.mockNormalGame1Player();
    CorrectionPhase correctionPhase = game.getCorrectionPhase();
    Player player = game.getGameInformation().getPlayerList().getFirst();
    ShipBoard shipBoard = player.getShipBoard();

    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @BeforeEach
    void setUp() {

    }

    @Test
    void shipBoardWith1Error() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 7, 8);
        Mocker.simulateClientInput("7 8\n");
        correctionPhase.start();
    }

    @Test
    void shipBoardWith2Errors() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 7, 6);
        shipBoard.addComponent(smoothComponent, 7, 8);
        // TODO multiple input does not work
        Mocker.simulateClientInput("7 6\n7 8\n");
        correctionPhase.start();
    }

    @Test
    void noInput() {
        Game game = Mocker.mockNormalGame1Player();
        CorrectionPhase correctionPhase = game.getCorrectionPhase();
        correctionPhase.start();
    }

    @Test
    void selectCrewType() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(6, 7, new AlienSupport(universalSides, true));
        shipBoard.addComponent(8, 7, new AlienSupport(universalSides, false));
        shipBoard.addComponent(6, 6, new Cabin(universalSides, CrewType.Human, 2));
        shipBoard.addComponent(8, 6, new Cabin(universalSides, CrewType.Human, 2));
        Mocker.simulateClientInput("Brown\nPurple\nHuman\nBrown\n");
        correctionPhase.start();

    }

}