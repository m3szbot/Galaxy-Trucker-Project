package it.polimi.ingsw.View;

import it.polimi.ingsw.Controller.AssemblyPhase.AssemblyThread;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

/**
 * Interface of methods that must be invokable on the Client Views by the Server.
 */
public interface ViewServerInvokableMethods {
    public void printMessage(String message);

    public void printComponent(Component component);

    public void printShipboard(ShipBoard shipBoard);

    public void printCard(Card card);

    public void printFlightBoard(FlightBoard flightBoard);

    default String getFXML() {
        return null;
    }
}
