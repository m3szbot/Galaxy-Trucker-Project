package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualClient extends UnicastRemoteObject implements ClientRemoteInterface{

    private ClientInfo clientInfo;
    private AtomicReference<String> userInput;
    private GamePhase gamePhase;
    private GeneralView views[];

    public VirtualClient(ClientInfo clientInfo) throws RemoteException {
        this.clientInfo = clientInfo;
        this.userInput = clientInfo.getUserInput();

        if(clientInfo.getViewType() == ViewType.TUI){

            //TODO
        }
        else{

            //TODO
        }
    }

    public String getString(){

        while(userInput.get() == null);
        return userInput.getAndSet(null);

    }

    @Override
    public void printShortCutMessage(String message) throws RemoteException {
        System.out.println(message);
    }

    @Override
    public void makeClientJoin(Server centralserver) throws RemoteException {

        JoinerUtil joiner = new JoinerUtil(clientInfo, centralserver, this);

        try {

            joiner.start();

        } catch (RemoteException e) {
            //something went wrong during the communication, notifying the client and the server too.

            System.out.println("You were disconnected");
            throw e;

        }
    }

    public void printComponent(Component component) throws RemoteException {
        //TODO

    }

    public void printShipboard(ShipBoard shipBoard) throws RemoteException{

        //TODO
    }

    public void printCard(Card card) throws RemoteException{

        //TODO
    }

    public void printFlightBoard(FlightBoard flightBoard) throws RemoteException{

        //TODO
    }

    public void setGamePhase(GamePhase gamePhase) throws RemoteException{
        this.gamePhase = gamePhase;
    }

    public void endGame() throws RemoteException{
        //TODO

    }


}
