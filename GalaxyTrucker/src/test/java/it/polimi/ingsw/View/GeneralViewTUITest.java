package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.EvaluationView.EvaluationViewTUI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

class GeneralViewTUITest {
    /**
     * gameInformation structure to test prints of models
     */
    GameInformation gameInformation;
    DataContainer dataContainer;
    GeneralView generalViewTUI;
    Random randomizer;


    @BeforeEach
    void setup() {
        randomizer = new Random();
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        dataContainer = new DataContainer();
        // abstract class cannot be instantiated
        generalViewTUI = new EvaluationViewTUI();
    }

    @Test
    public void printComponent() {
        int randIndex;
        // print random components from component list
        for (int i = 0; i < 15; i++) {
            randIndex = randomizer.nextInt(gameInformation.getComponentList().size());
            dataContainer.setComponent(gameInformation.getComponentList().get(randIndex));
            generalViewTUI.printComponent(dataContainer);
        }
    }

    @Test
    public void printMessage() {
        dataContainer.setMessage("Hello world!\n");
        generalViewTUI.printMessage(dataContainer);
    }

    @Test
    public void printEmptyShipboard() {
        ShipBoard shipBoard = new ShipBoard(GameType.NORMALGAME);
        dataContainer.setShipBoard(shipBoard);
        generalViewTUI.printShipboard(dataContainer);
    }

    @Test
    public void printFilledShipboard() {
        int componentIndex;
        ShipBoard shipBoard = new ShipBoard(GameType.NORMALGAME);
        // fill shipboard with random components
        for (int i = 0; i < shipBoard.getMatrixCols(); i++) {
            for (int j = 0; j < shipBoard.getMatrixRows(); j++) {
                componentIndex = randomizer.nextInt(gameInformation.getComponentList().size());
                try {
                    shipBoard.addComponent(gameInformation.getComponentList().get(componentIndex), i + 1, j + 1);
                } catch (NotPermittedPlacementException e) {
                    e.printStackTrace();
                }
            }
        }

        dataContainer.setShipBoard(shipBoard);
        generalViewTUI.printShipboard(dataContainer);
    }

}