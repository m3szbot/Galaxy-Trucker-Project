package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.TimeoutException;

public class VirtualClient extends UnicastRemoteObject implements ClientRemoteInterface{

    private GeneralView currentView;
    private boolean inGame;
    private ViewCommunicator viewCommunicator;

    public VirtualClient(ClientInfo clientInfo) throws RemoteException {

        viewCommunicator = clientInfo.getViewCommunicator();
        currentView = viewCommunicator.getView();
        this.inGame = false;

    }

    @Override
    public boolean isAlive() throws RemoteException {
       return true;
    }

    @Override
    public boolean isInGame() throws RemoteException{
        return inGame;
    }


    //not in use for now. May be necessary for future versions for correcting problems

    public void unblockUserInput() throws RemoteException{
        ClientInputManager.unblockInput();
    }

    public String getString() throws RemoteException{

        try {

            return ClientInputManager.getUserInput();

        } catch (TimeoutException e) {
            String message = "Timeout reached, you are considered inactive, disconnection will soon happen";
            viewCommunicator.getView().printMessage(message);
            setInGame(false);
            throw new RemoteException("inactivity");
        }

    }

    public void printPreJoinMessage(String message) throws RemoteException{
        System.out.println(message);
    }

    @Override
    public void printMessage(String message) throws RemoteException {
        currentView.printMessage(message);
    }

    public void printComponent(Component component) throws RemoteException {

        currentView.printComponent(component);

    }

    public void printShipboard(ShipBoard shipBoard) throws RemoteException{

        currentView.printShipboard(shipBoard);

    }

    public void printCard(Card card) throws RemoteException{

        currentView.printCard(card);

    }

    public void printFlightBoard(FlightBoard flightBoard) throws RemoteException{

        currentView.printFlightBoard(flightBoard);

    }

    public void setGamePhase(GamePhase gamePhase) throws RemoteException{

        viewCommunicator.setGamePhase(gamePhase);
        currentView = viewCommunicator.getView();

    }

    public void endGame() throws RemoteException{

        System.out.println("The game has ended");
        inGame = false;
    }

    public void setInGame(boolean inGame){
        ClientInputManager.setTimeOut(300000);
        this.inGame = inGame;
    }

    public void sendCommand(String command) throws RemoteException{
       //nothing for now
    }
}
