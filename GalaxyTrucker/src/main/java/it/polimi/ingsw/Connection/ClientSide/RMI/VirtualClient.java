package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.FlightBoard.FlightBoard;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.ShipBoard.ShipBoard;
import it.polimi.ingsw.View.GeneralView;
import it.polimi.ingsw.View.TUI.TuiView;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class VirtualClient extends UnicastRemoteObject implements ClientRemoteInterface{

    private AtomicReference<String> userInput;
    private GeneralView views[] = new GeneralView[4];
    private GeneralView currentView;
    private int TIMEOUT;
    private boolean inGame;

    public VirtualClient(ClientInfo clientInfo) throws RemoteException {

        this.userInput = clientInfo.getUserInput();

        if (clientInfo.getViewType() == ViewType.TUI) {

            views[0] = new TuiView();
            views[1] = new TuiView();
            views[2] = new TuiView();
            views[3] = new TuiView();

        } else {/*
                GUI

            views[0] = new AssemblyViewGUI();
            views[1] = new CorrectionViewGUI();
            views[2] = new FlightViewGUI();
            views[3] = new EvaluationViewGUI();
            */
        }
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
     *
     * @param value true for a 5-minute time out, false for a 1-minute time out.
     */

    public void setInputTimeOut(boolean value){
        if(value){
            //5 minute timeout
            TIMEOUT = 6000;
        }
        else{
            //1 minute time out
            TIMEOUT = 1200;
        }
    }

    /**
     * The method avoid busy waiting avoiding useless cpu usage. If the
     * client does not insert an input for 5 min, an exception if thrown
     * @return string entered by the client
     * @throws RemoteException
     */

    public String getString(AtomicBoolean clientConnected) throws RemoteException{

        String input;
        int time = 0;

        while(clientConnected.get()){

            if(time == TIMEOUT){
                System.out.println("Timeout reached, you are considered inactive, disconnection will soon happen");
                throw new RemoteException("Player was kicked out because of inactivity");
            }

            if(userInput.get() != null){
                return userInput.getAndSet(null);
            }

            try{
                time++;
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("Thread in getString method of virtual client was abnormally interrupted");
            }

        }

        throw new RemoteException();
    }

    //not in use for now. May be necessary for future versions for correcting problems

    public void unblockUserInput() throws RemoteException{
        userInput.set(" ");
    }

    public String getString() throws RemoteException{

        int time = 0;

        while(true){

            if(time == TIMEOUT){
                System.out.println("Timeout reached, you are considered inactive, disconnection will soon happen");
                setInGame(false);
                throw new RemoteException("inactivity");
            }

            if(userInput.get() != null){
                return userInput.getAndSet(null);
            }

            try{
                time++;
                Thread.sleep(50);
            } catch (InterruptedException e) {
                System.err.println("Thread in getString method of virtual client was abnormally interrupted");
            }


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
            case Assembly -> currentView = views[0];
            case Correction -> currentView = views[1];
            case Flight -> currentView = views[2];
            case Evaluation -> currentView = views[3];
        }

    }

    public void endGame() throws RemoteException{
        //TODO
        System.out.println("The game has ended");
        inGame = false;
    }

    public void setInGame(boolean inGame){
        this.inGame = inGame;
    }

}
