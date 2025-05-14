package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneralViewTUITest {
    /**
     * gameInformation structure to test prints of models
     */
    GameInformation gameInformation;
    DataContainer dataContainer;
    GeneralView generalViewTUI;


    @BeforeEach
    void setup() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        dataContainer = new DataContainer();
        // abstract class cannot be instantiated
        generalViewTUI = new EvaluationViewTUI();
    }

    @Test
    public void printComponent() {
        // print random components from component list
        for (int i = 0; i < 5; i++) {
            dataContainer.setComponent(gameInformation.getComponentList().get(i * 8));
            generalViewTUI.printComponent(dataContainer);
        }
    }

    @Test
    public void printMessage() {
        dataContainer.setMessage("Hello world!\n");
        generalViewTUI.printMessage(dataContainer);
    }
}