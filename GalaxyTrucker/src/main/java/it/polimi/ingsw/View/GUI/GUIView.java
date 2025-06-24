package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GUI.utils.FlightBoardController;
import it.polimi.ingsw.View.GUI.utils.PaneBuilder;
import it.polimi.ingsw.View.GeneralView;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.io.IOException;

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

    private Pane shipBoardPane;
    private Pane flightBoardPane;

    private GUIController guiController;

    public void setGameType(GameType gameType){

        try {

            FXMLLoader shipBoardLoader;
            FXMLLoader flightBoardLoader;

            if(gameType == GameType.NORMALGAME){

                shipBoardLoader = new FXMLLoader(getClass().getResource("/fxml/normalGameShipBoard.fxml"));
                flightBoardLoader = new FXMLLoader(getClass().getResource("/fxml/normalGameFlightBoard.fxml"));

            }
            else{

                shipBoardLoader = new FXMLLoader(getClass().getResource("/fxml/testGameShipBoard.fxml"));
                flightBoardLoader = new FXMLLoader(getClass().getResource("/fxml/testGameFlightBoard.fxml"));

            }

            shipBoardPane = shipBoardLoader.load();
            flightBoardPane = flightBoardLoader.load();
            FlightBoardController flightBoardController = flightBoardLoader.getController();
            flightBoardController.setUpTilesMap(gameType);

            PaneBuilder.setShipBoardController(shipBoardLoader.getController());
            PaneBuilder.setFlightBoardController(flightBoardController);

        } catch (IOException e) {

            System.err.println("Error while setting up ship board pane");

        }

    }

    public void setGuiController(GUIController controller) {
        this.guiController = controller;
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

        guiController.refreshConsole(message);

    }

    @Override
    public void printComponent(Component component) {

        guiController.refreshComponent(PaneBuilder.buildComponentImage(component));

    }

    @Override
    public void printShipboard(ShipBoard shipBoard) {

        PaneBuilder.buildShipBoardPane(shipBoard);
        guiController.refreshShipBoard(shipBoardPane);

    }

    @Override
    public void printCard(Card card) {

        guiController.refreshCard(PaneBuilder.buildCardImage(card));

    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {

        PaneBuilder.buildFlightBoardPane(flightBoard);
        guiController.refreshFlightBoard(flightBoardPane);

    }

    @Override
    public void printFullShipboard(ShipBoard shipBoard) {
        //used only for testng by boti
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

        if(dataContainer.getShipBoard() == null){
            throw new IllegalArgumentException("The DC does not contain a shipboard");
        }
        else{
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

        if(dataContainer.getFlightBoard() == null){
            throw new IllegalArgumentException("The DC does not contain a flight board");
        } else{
            printFlightBoard(dataContainer.getFlightBoard());
        }

    }
}
