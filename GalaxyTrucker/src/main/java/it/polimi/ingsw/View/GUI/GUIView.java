package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GUI.AssemblyControllers.AssemblyGUIController;
import it.polimi.ingsw.View.GUI.utils.FXUtil;
import it.polimi.ingsw.View.GUI.utils.FlightBoardController;
import it.polimi.ingsw.View.GUI.utils.PaneBuilder;
import it.polimi.ingsw.View.GeneralView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.List;

//TODO

/**
 * Class with the exact same function of TUIView but for the GUI.
 * The methods purpose is to pass to the current controller the node
 * containing all the information already beautifully packed.
 *
 * @author carlo
 */

public class GUIView extends GeneralView {

    /*
    The idea of the methods is the following. The methods create a node which
    contain the information passed as parameter, ready to be shown on the screen.
    A method of the current controller is then called passing it the node as a parameter.
    The method of the current controller then handles the node by inserting it into
    the current fxml file.
     */

    private GUIController guiController;

    private Pane shipBoardPane;
    private Pane flighBoardPane;
    private GameType gameType;

    private ShipBoard playerShipBoard;
    private List<ShipBoard> possibleShipBoards;

    public void setGameType(GameType gameType) {

        FXMLLoader shipBoardLoader;
        FXMLLoader flightBoardLoader;
        this.gameType = gameType;

        try {

            if (gameType == GameType.NORMALGAME) {

                shipBoardLoader = new FXMLLoader(getClass().getResource("/fxml/general/normalGameShipBoard.fxml"));
                flightBoardLoader = new FXMLLoader(getClass().getResource("/fxml/general/normalGameFlightBoard.fxml"));

            } else {

                shipBoardLoader = new FXMLLoader(getClass().getResource("/fxml/general/testGameShipBoard.fxml"));
                flightBoardLoader = new FXMLLoader(getClass().getResource("/fxml/general/testGameFlightBoard.fxml"));

            }

            shipBoardPane = shipBoardLoader.load();
            flighBoardPane = flightBoardLoader.load();

            FlightBoardController flightBoardController = flightBoardLoader.getController();
            flightBoardController.setUpTilesMap(gameType);

            PaneBuilder.setShipBoardController(shipBoardLoader.getController());
            PaneBuilder.setFlightBoardController(flightBoardController);
        } catch (IOException e) {
            System.err.println("Error while loading fxml files in GUIView");
        }


    }

    public void setGuiController(GUIController controller) {
        this.guiController = controller;
        if (guiController instanceof AssemblyGUIController) {
            ((AssemblyGUIController) guiController).setGameType(gameType);
        }
    }

    @Override
    public void printMessage(DataContainer dataContainer) {

        if (dataContainer.getMessage() == null) {
            throw new IllegalArgumentException("The DC does not contain a message");
        } else {
            printMessage(dataContainer.getMessage());
        }

    }

    @Override
    public void printMessage(String message) {
        FXUtil.runOnFXThread(() -> {
            guiController.refreshConsole(stripAnsiCodes(message));
        });

    }

    private String stripAnsiCodes(String message) {
        String ansiRegex = "\u001B\\[[0-9;]*m";
        return message.replaceAll(ansiRegex, "");
    }

    @Override
    public void printComponent(Component component) {

        FXUtil.runOnFXThread(() -> {
            guiController.refreshComponent(PaneBuilder.buildComponentImage(component));
        });

    }

    @Override
    public void printShipboard(ShipBoard shipBoard) {
        /*
        TODO
        // fracture prints new possible shipboards
        if (!shipBoard.equals(playerShipBoard)) {
            // add possible shipboard to list
            possibleShipBoards.add(shipBoard);
        } else {
            // original shipboard received, no fracture/fracture is over
            if (!possibleShipBoards.isEmpty()) {
                possibleShipBoards = new ArrayList<>();

            }
        }

         */

        FXUtil.runOnFXThread(() -> {

            PaneBuilder.buildShipBoardPane(shipBoard);
            guiController.refreshShipBoard(shipBoardPane);

        });

    }

    @Override
    public void printCard(Card card) {

        FXUtil.runOnFXThread(() -> {
            guiController.refreshCard(PaneBuilder.buildCardImage(card));
        });


    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {

        FXUtil.runOnFXThread(() -> {

            PaneBuilder.buildFlightBoardPane(flightBoard);
            guiController.refreshFlightBoard(flighBoardPane);

        });

    }


    @Override
    public void printComponent(DataContainer dataContainer) {

        if (dataContainer.getComponent() == null) {
            throw new IllegalArgumentException("The DC does not contain a component");
        } else {
            printComponent(dataContainer.getComponent());
        }


    }

    @Override
    public void printShipboard(DataContainer dataContainer) {

        if (dataContainer.getShipBoard() == null) {
            throw new IllegalArgumentException("The DC does not contain a shipboard");
        } else {
            printShipboard(dataContainer.getShipBoard());
        }

    }

    @Override
    public void printCard(DataContainer dataContainer) {

        if (dataContainer.getCard() == null) {
            throw new IllegalArgumentException("The DC does not contain a card");
        } else {
            printCard(dataContainer.getCard());
        }

    }

    @Override
    public void printFlightBoard(DataContainer dataContainer) {

        if (dataContainer.getFlightBoard() == null) {
            throw new IllegalArgumentException("The DC does not contain a flight board");
        } else {
            printFlightBoard(dataContainer.getFlightBoard());
        }

    }
}
