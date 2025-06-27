package it.polimi.ingsw.View;

import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Controller.FlightPhase.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * Abstract class that defines the signature of all the method that will be overridden
 * by the TUIView and GUIView. All the methods have the same purpose, which is that of printing
 * the passed parameter in case of a TUI implementation or passing the parameter packed as a Node
 * to the guiController of the current phase in case of GUI implementation.
 */

public abstract class GeneralView implements ViewServerInvokableMethods {

    public abstract void printMessage(DataContainer dataContainer);

    public abstract void printMessage(String message);

    public abstract void printComponent(Component component);

    public abstract void printShipboard(ShipBoard shipBoard);

    public abstract void printCard(Card card);

    public abstract void printFlightBoard(FlightBoard flightBoard);

    public abstract void printComponent(DataContainer dataContainer);

    public abstract void printShipboard(DataContainer dataContainer);

    public abstract void printCard(DataContainer dataContainer);

    public abstract void printFlightBoard(DataContainer dataContainer);


}
