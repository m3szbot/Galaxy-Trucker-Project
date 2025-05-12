package it.polimi.ingsw.Connection.ServerSide;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.io.Serializable;

/**
 * Bean class that contains all the possible object that the server
 * could send to the client. Before sending information to the client
 * you must set his dataContainer accordingly.
 */

public class DataContainer implements Serializable {
    private String command;
    private String message;
    private ShipBoard shipBoard;
    private Component component;
    private FlightBoard flightBoard;
    private Card card;


    public void clearContainer() {
        message = null;
        shipBoard = null;
        component = null;
        flightBoard = null;
        card = null;
    }

    /**
     * Method to set DataContainer with a single command.
     * Pass null for parameters not to set a value for.
     *
     * @author Boti
     */
    public void setDataContainer(String command, String message, ShipBoard shipBoard, Component component,
                                 FlightBoard flightBoard, Card card) {
        this.command = command;
        this.message = message;
        this.shipBoard = shipBoard;
        this.component = component;
        this.flightBoard = flightBoard;
        this.card = card;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }


}
