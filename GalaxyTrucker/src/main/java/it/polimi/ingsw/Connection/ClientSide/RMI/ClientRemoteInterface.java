package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientRemoteInterface extends Remote{

    public void makeClientJoin(Server centralserver) throws RemoteException;

    public void printShortCutMessage(String message) throws RemoteException;

    public String getString() throws RemoteException;

    public void printComponent(Component component) throws RemoteException;

    public void printShipboard(ShipBoard shipBoard) throws RemoteException;

    public void printCard(Card card) throws RemoteException;

    public void printFlightBoard(FlightBoard flightBoard) throws RemoteException;

    public void setGamePhase(GamePhase gamePhase) throws RemoteException;

    public void endGame() throws RemoteException;


}
