package it.polimi.ingsw.Connection.ServerSide.socket;


import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.io.Serializable;

/**
 * Bean class that contains all the possible object that the server
 * could send to the client. Before sending information to the client
 * you must set his dataContainer accordingly.
 */

public class DataContainer implements Serializable {

    private String command;
    private GamePhase gamePhase;
    private String message;
    private ShipBoard shipBoard;
    private Component component;
    private FlightBoard flightBoard;
    private Card card;

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void clearContainer() {
        message = null;
        shipBoard = null;
        component = null;
        flightBoard = null;
        card = null;
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
