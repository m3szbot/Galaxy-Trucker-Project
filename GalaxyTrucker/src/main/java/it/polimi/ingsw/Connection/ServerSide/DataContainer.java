package it.polimi.ingsw.Connection.ServerSide;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.io.Serializable;

public class DataContainer implements Serializable {

    private String message;
    private ShipBoard shipBoard;
    private Card card;
    private FlightBoard flightBoard;

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
