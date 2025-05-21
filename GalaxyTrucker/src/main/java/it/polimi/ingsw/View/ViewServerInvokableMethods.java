package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * Interface defining the methods of the Client Views that must be invokable by the Server.
 */
public interface ViewServerInvokableMethods {
    public void printMessage(String message);

    public void printComponent(Component component);

    public void printShipboard(ShipBoard shipBoard);

    public void printCard(Card card);

    public void printFlightBoard(FlightBoard flightBoard);

}
