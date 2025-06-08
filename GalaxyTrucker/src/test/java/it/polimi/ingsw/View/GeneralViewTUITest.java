package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Model.Components.*;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.NotPermittedPlacementException;
import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.TUI.TUIView;
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

    SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] universalSidesSpecialFront = new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal};
    SideType[] universalSidesSpecialBack = new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal};


    @BeforeEach
    void setup() {
        randomizer = new Random();
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);
        dataContainer = new DataContainer();
        // abstract class cannot be instantiated
        generalViewTUI = new TUIView();
    }

    @Test
    public void printShipboardWithComponentsAdded() throws NotPermittedPlacementException {
        SideType[] universalSides = new SideType[]{SideType.Universal, SideType.Universal, SideType.Universal, SideType.Universal};
        SideType[] universalSidesSpecialFront = new SideType[]{SideType.Special, SideType.Universal, SideType.Universal, SideType.Universal};
        SideType[] universalSidesSpecialBack = new SideType[]{SideType.Universal, SideType.Universal, SideType.Special, SideType.Universal};
        // prints a shipboard with many components added
        ShipBoard shipBoard = new ShipBoard(GameType.NORMALGAME);
        shipBoard.addComponent(new Cannon(universalSidesSpecialFront, true), 7, 6);
        shipBoard.addComponent(new Battery(universalSides, 3), 8, 6);
        shipBoard.addComponent(new Engine(universalSidesSpecialBack, true), 7, 8);
        shipBoard.addComponent(new Storage(universalSidesSpecialBack, true, 4), 6, 8);
        shipBoard.addComponent(new AlienSupport(universalSides, true), 6, 7);
        shipBoard.addComponent(new Cabin(universalSides, CrewType.Purple, 1), 5, 7);
        shipBoard.addComponent(new Shield(universalSides, 0, 3), 6, 6);
        shipBoard.addComponent(new Component(universalSides), 8, 7);
        shipBoard.addComponent(new Storage(universalSides, false, 3), 9, 7);

        generalViewTUI.printShipboard(shipBoard);
    }

    @Test
    public void printManyComponents() {
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

    @Test
    public void printAlienSupports() {
        AlienSupport brown = new AlienSupport(universalSides, false);
        AlienSupport purple = new AlienSupport(universalSides, true);
        generalViewTUI.printComponent(brown);
        generalViewTUI.printComponent(purple);
    }

    @Test
    public void printBattery() {
        Battery battery = new Battery(universalSides, 3);
        generalViewTUI.printComponent(battery);
    }

    @Test
    public void printCabins() {
        Cabin human = new Cabin(universalSides, CrewType.Human, 2);
        Cabin purple = new Cabin(universalSides, CrewType.Purple, 2);
        Cabin brown = new Cabin(universalSides, CrewType.Brown, 2);

        generalViewTUI.printComponent(human);
        generalViewTUI.printComponent(purple);
        generalViewTUI.printComponent(brown);
    }

    @Test
    public void printCannon() {
        Cannon cannon = new Cannon(universalSides, true);
        generalViewTUI.printComponent(cannon);
    }

    @Test
    public void printEngine() {
        Engine engine = new Engine(universalSides, true);
        generalViewTUI.printComponent(engine);
    }

    @Test
    public void printShields() {
        Shield front = new Shield(universalSides, 0, 0);
        Shield left = new Shield(universalSides, 3, 3);
        Shield right = new Shield(universalSides, 1, 1);
        Shield back = new Shield(universalSides, 2, 2);

        Shield frontRight = new Shield(universalSides, 0, 1);
        Shield frontBack = new Shield(universalSides, 0, 2);
        Shield frontLeft = new Shield(universalSides, 0, 3);

        Shield rightBack = new Shield(universalSides, 1, 2);
        Shield rightLeft = new Shield(universalSides, 1, 3);

        Shield backLeft = new Shield(universalSides, 2, 3);

        System.out.println("single");
        generalViewTUI.printComponent(front);
        generalViewTUI.printComponent(left);
        generalViewTUI.printComponent(right);
        generalViewTUI.printComponent(back);
        System.out.println("front");
        generalViewTUI.printComponent(frontRight);
        generalViewTUI.printComponent(frontBack);
        generalViewTUI.printComponent(frontLeft);
        System.out.println("right");
        generalViewTUI.printComponent(rightBack);
        generalViewTUI.printComponent(rightLeft);
        System.out.println("back");
        generalViewTUI.printComponent(backLeft);
    }

    @Test
    public void printStorages() {
        Storage red = new Storage(universalSides, true, 4);
        Storage blue = new Storage(universalSides, false, 4);
        generalViewTUI.printComponent(red);
        generalViewTUI.printComponent(blue);
    }


}