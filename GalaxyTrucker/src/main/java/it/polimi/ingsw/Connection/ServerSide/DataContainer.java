package it.polimi.ingsw.Connection.ServerSide;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.io.Serializable;

/**
 * Bean class that contains all the possible object that the server
 * could send to the client. Before sending information to the client
 * you must set his dataContainer accordingly.
 */

public class DataContainer implements Serializable {

    private String message;
    private ShipBoard shipBoard;
    private Card card;
    private FlightBoard flightBoard;
    private String command;

    public void clearContainer(){
        message = null;
        shipBoard = null;
        card = null;
        flightBoard = null;
    }

    public void setCommand(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setShipBoard(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
    }

    public Card getCard() {
        return card;
    }

    public FlightBoard getFlightBoard() {
        return flightBoard;
    }

    public ShipBoard getShipBoard() {
        return shipBoard;
    }

    public String getMessage() {
        return message;
    }
}
