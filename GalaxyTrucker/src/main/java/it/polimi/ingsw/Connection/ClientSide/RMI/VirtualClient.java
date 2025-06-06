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
import java.util.concurrent.atomic.AtomicBoolean;

public class VirtualClient extends UnicastRemoteObject implements ClientRemoteInterface{

    private GeneralView currentView;
    private boolean inGame;
    private ViewCommunicator viewCommunicator;

    public VirtualClient(ClientInfo clientInfo) throws RemoteException {

        viewCommunicator = clientInfo.getViewCommunicator();

    }

    @Override
    public boolean isAlive() throws RemoteException {
       return true;
    }

    @Override
    public boolean isInGame() throws RemoteException{
        return inGame;
    }

    /**
     * The method avoid busy waiting avoiding useless cpu usage. If the
     * client does not insert an input for 5 min, an exception if thrown
     * @return string entered by the client
     * @throws RemoteException
     */

    public String getString(AtomicBoolean clientConnected) throws RemoteException{

        try {

            String input = ClientInputManager.getUserInput();

            return input;

        } catch (TimeoutException e) {
            String message = "Timeout reached, you are considered inactive, disconnection will soon happen";
            viewCommunicator.showData(message, false);
            throw new RemoteException("inactivity");
        }

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
            viewCommunicator.showData(message, false);
            setInGame(false);
            throw new RemoteException("inactivity");
        }

    }

    @Override
    public void printMessage(String message) throws RemoteException {
        System.out.println(message);
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

        switch (gamePhase) {
            case Assembly -> currentView = viewCommunicator.getView(0);
            case Correction -> currentView = viewCommunicator.getView(1);
            case Flight -> currentView = viewCommunicator.getView(2);
            case Evaluation -> currentView = viewCommunicator.getView(3);
        }

    }

    public void endGame() throws RemoteException{

        System.out.println("The game has ended");
        inGame = false;
    }

    public void setInGame(boolean inGame){
        ClientInputManager.setTimeOut(300000);
        this.inGame = inGame;
    }

}
