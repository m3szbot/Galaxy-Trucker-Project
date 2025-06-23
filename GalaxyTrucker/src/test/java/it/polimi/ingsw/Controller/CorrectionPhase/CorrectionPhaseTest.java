package it.polimi.ingsw.Controller.CorrectionPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.Components.SideType;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

class CorrectionPhaseTest {
    private final InputStream originalInput = System.in;

    Game game = Mocker.mockNormalGame1Player();
    CorrectionPhase correctionPhase = game.getCorrectionPhase();
    Player player = game.getGameInformation().getPlayerList().getFirst();

    Component smoothComponent = new Component(new SideType[]{SideType.Smooth, SideType.Smooth, SideType.Smooth, SideType.Smooth});
    Component singleComponent = new Component(new SideType[]{SideType.Single, SideType.Single, SideType.Single, SideType.Single});
    Component doubleComponent = new Component(new SideType[]{SideType.Double, SideType.Double, SideType.Double, SideType.Double});

    @BeforeEach
    void setUp() {

    }

    @Test
    void erroneousShipBoard() throws NotPermittedPlacementException, IllegalSelectionException {
        player.getShipBoard().addComponent(smoothComponent, 7, 8);
        inputSimulator("7 8\n");

        correctionPhase.start();
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

    @AfterEach
    void restoreInput() {
        System.setIn(originalInput);
    }


}