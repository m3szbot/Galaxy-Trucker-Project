package it.polimi.ingsw.Controller;

import it.polimi.ingsw.Controller.CorrectionPhase.CorrectionPhase;
import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.Cabin;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.CrewType;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ExceptionsHandlerTest {
    Game game;
    CorrectionPhase correctionPhase;
    Player player;
    ShipBoard shipBoard;


    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};

    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        player = Mocker.getFirstPlayer();
        shipBoard = player.getShipBoard();
        correctionPhase = game.getCorrectionPhase();
    }

    @Test
    void testFracturedShipBoardException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 6, 7);
        shipBoard.addComponent(smoothComponent, 6, 8);
        shipBoard.addComponent(new Cabin(universalSides, CrewType.Human, 2), 5, 8);

        Mocker.simulateClientInput("6 7\n1\n");
        correctionPhase.start();
    }

    @Test
    void testNoHumanCrewLeftException() throws NotPermittedPlacementException, IllegalSelectionException {
        shipBoard.addComponent(smoothComponent, 6, 7);

        Mocker.simulateClientInput("7 7\n");
        correctionPhase.start();
    }

}
