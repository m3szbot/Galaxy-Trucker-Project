package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientServerInvokableMethods;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GeneralView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that receive the messages that the server sends to the player
 *
 * @author carlo
 */

public class GameMessageReceiver implements Runnable, ClientServerInvokableMethods {

    private ViewCommunicator viewCommunicator;
    private AtomicBoolean running;
    private SocketDataExchanger dataExchanger;

    public GameMessageReceiver(ViewCommunicator viewCommunicator, SocketDataExchanger dataExchanger, AtomicBoolean running) {

        this.viewCommunicator = viewCommunicator;
        this.running = running;
        this.dataExchanger = dataExchanger;

    }

    public void run() {

        DataContainer container;

        while (running.get()) {

            try {

                container = dataExchanger.getContainer();
                executeCommand(container.getCommand(), container);


            } catch (IOException e) {
                running.set(false);
                viewCommunicator.showData("You have been disconnected from the server", false);
                break;
            } catch (Exception e) {
                viewCommunicator.showData("Unexpected error in receiver: " + e.getMessage(), true);
                e.printStackTrace();
            }

        }

    }


    private void executeCommand(String command, DataContainer dataContainer) {
        if (command.equals("setGamePhase")) {
            setGamePhase(dataContainer.getGamePhase());
        } else if (command.equals("endGame")) {
            endGame();
            ClientInputManager.unblockInput();
        }
        else if(command.equals("joined")){
            ClientInputManager.setTimeOut(300000);
        }
        else if(command.equals("unblock")){
            ClientInputManager.unblockInput();
        }
        else {
            callView(dataContainer);
        }
    }

    public void setGamePhase(GamePhase gamePhase) {

        viewCommunicator.setGamePhase(gamePhase);

    }

    public void endGame() {
        running.set(false);
        System.out.println("The game has ended");
    }

    private void callView(DataContainer container) {

        GeneralView currentView = viewCommunicator.getView();
        String methodName = container.getCommand();

        if (currentView == null) {
            System.err.println("Critical error: view not found");
            running.set(false);
        }

        try {

            Method method = currentView.getClass().getMethod(methodName, DataContainer.class);
            method.invoke(currentView, container);


        } catch (NoSuchMethodException e1) {
            running.set(false);
            System.err.println("Critical error while accessing view method: method not found ");
            System.out.println("You have been disconnected");
        } catch (IllegalAccessException e2) {
            running.set(false);
            System.err.println("Critical error while accessing view method: method does not have access to the definition of the specified class");
            System.out.println("You have been disconnected");
        } catch (InvocationTargetException e3) {
            running.set(false);
            System.err.println("Critical error while accessing view method: the method invoked did not behave correctly");
            System.out.println("You have been disconnected");
        }
    }

}
