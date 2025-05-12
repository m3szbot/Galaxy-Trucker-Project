package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneralViewTest {
    /**
     * gameInformation structure to test prints of models
     */
    GameInformation gameInformation;
    DataContainer dataContainer;

    @BeforeEach
    void setup() {
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NormalGame, 4);
        dataContainer = new DataContainer();
    }

    @Test
    public void printComponent() {
        for (int i = 0; i < 10; i++) {
            dataContainer.setComponent(gameInformation.getComponentList().get(i));
            printComponent();
        }
    }
}