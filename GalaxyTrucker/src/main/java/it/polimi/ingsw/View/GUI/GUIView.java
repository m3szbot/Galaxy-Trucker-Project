package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

//TODO

/**
 * Class with the exact same function of TUIView but for the GUI.
 *
 * @author carlo
 */

public class GUIView extends GeneralView {


    private GUIController guiController;

    public void setGuiController(GUIController controller) {
        this.guiController = controller;
    }

    /*
    The idea of the methods is the following. The methods create a node which
    contain the information passed as parameter, ready to be shown on the screen.
    A method of the current controller is then called passing it the node as a parameter.
    The method of the current controller then handles the node by inserting it into
    the current fxml file.
     */

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

        Image componentImage = new Image(component.getImagePath());
        ImageView componentImageView = new ImageView(componentImage);

        guiController.refreshComponent(componentImageView);

    }

    @Override
    public void printShipboard(ShipBoard shipBoard) {

    }

    @Override
    public void printCard(Card card) {

        Image cardImage = new Image(card.getCardImage());
        ImageView cardImageView = new ImageView(cardImage);

        guiController.refreshCard(cardImageView);


    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {

        Image flightBoardImage = new Image(flightBoard.getImagePath());

    }

    @Override
    public void printFullShipboard(ShipBoard shipBoard) {

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

    }
}
