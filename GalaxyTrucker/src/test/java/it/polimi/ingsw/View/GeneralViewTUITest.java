package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Controller.AssemblyPhase.NotPermittedPlacementException;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        generalViewTUI = new GeneralView();
    }

    @Test
    public void printComponent() {
        int randIndex;
        // print random components from component list
        for (int i = 0; i < 5; i++) {
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
    public void printFullShipboard() {
        ShipBoard shipBoard = new ShipBoard(GameType.NORMALGAME);
        generalViewTUI.printFullShipboard(shipBoard);
    }

    @Test
    public void printFullTestGameShipboard() {
        ShipBoard shipBoard = new ShipBoard(GameType.TESTGAME);
        generalViewTUI.printFullShipboard(shipBoard);
    }

    @Test
    public void printEmptyTestShipboard() {
        ShipBoard shipBoard = new ShipBoard(GameType.TESTGAME);
        dataContainer.setShipBoard(shipBoard);
        generalViewTUI.printShipboard(dataContainer);
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
                }
            }
        }

        dataContainer.setShipBoard(shipBoard);
        generalViewTUI.printShipboard(dataContainer);
    }

    @Test
    public void printFlightBoardTest() {
        FlightBoard flightBoard = new FlightBoard(GameType.NORMALGAME, gameInformation.getCardsList());

        Player player1 = new Player("Player1", Color.RED, gameInformation);
        Player player2 = new Player("Player2", Color.BLUE, gameInformation);
        Player player3 = new Player("Player3", Color.GREEN, gameInformation);
        Player player4 = new Player("Player4", Color.YELLOW, gameInformation);

        gameInformation.addPlayers(player1);
        gameInformation.addPlayers(player2);
        gameInformation.addPlayers(player3);
        gameInformation.addPlayers(player4);

        flightBoard.addPlayer(player1, 7);
        flightBoard.addPlayer(player2, 4);
        flightBoard.addPlayer(player3, 2);
        flightBoard.addPlayer(player4, 1);

        dataContainer.setFlightBoard(flightBoard);
        assertNotNull(dataContainer);
        generalViewTUI.printFlightBoard(dataContainer);
    }

}