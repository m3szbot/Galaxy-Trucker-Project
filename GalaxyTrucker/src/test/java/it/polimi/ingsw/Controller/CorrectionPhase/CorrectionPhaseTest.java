package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CorrectionPhaseTest {
    Game game = Mocker.mockNormalGame1Player();
    CorrectionPhase correctionPhase = game.getCorrectionPhase();
    Player player = game.getGameInformation().getPlayerList().getFirst();

    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});

    @BeforeEach
    void setUp() {

    }

    @Test
    void erroneousShipBoard() throws NotPermittedPlacementException, IllegalSelectionException {
        player.getShipBoard().addComponent(smoothComponent, 7, 8);
        Mocker.simulateInput("7 8\n");

        correctionPhase.start();
    }

    @Test
    void noInput() {
        correctionPhase.start();
    }

}