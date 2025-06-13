package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GUI.LobbyControllers.LobbyGUIController;
import it.polimi.ingsw.View.GeneralView;
//TODO

public class GUIView extends GeneralView {

    private GUIController guiController = new LobbyGUIController();
    private GamePhase gamePhase = GamePhase.Lobby;

    /*
    The idea of the methods is the following. The methods create a node which
    contain the information passed as parameter, ready to be shown on the screen.
    A method of the current controller is then called passing it the node as a parameter.
    The method of the current controller then handles the node by inserting it into
    the current fxml file.
     */

    public void changePhase(){

       switch (gamePhase){
           case Lobby -> {
               gamePhase = GamePhase.Assembly;
               //guiController = new AssemblyGUIController();
           }
           case Assembly -> {
               gamePhase = GamePhase.Correction;
               //guiController = new CorrectionGUIController();
           }
           case Correction -> {
               gamePhase = GamePhase.Flight;
               //guiController = new FlightGUIController();
           }
           case Flight -> {
               gamePhase = GamePhase.Evaluation;
               //guiController = new EvaluationGUIController();
           }

       }

    }

    @Override
    public void printMessage(String message) {

        guiController.showMessage(message);

    }

    @Override
    public void printMessage(DataContainer dataContainer) {

        guiController.showMessage(dataContainer.getMessage());

    }

    @Override
    public void printCard(Card card) {



    }

    @Override
    public void printCard(DataContainer dataContainer) {

    }

    @Override
    public void printComponent(Component component) {

    }

    @Override
    public void printComponent(DataContainer dataContainer) {

    }

    @Override
    public void printFlightBoard(FlightBoard flightBoard) {

    }

    @Override
    public void printFlightBoard(DataContainer dataContainer) {

    }

    @Override
    public void printFullShipboard(ShipBoard shipBoard) {

    }

    @Override
    public void printShipboard(ShipBoard shipBoard) {

    }

    @Override
    public void printShipboard(DataContainer dataContainer) {

    }
}
