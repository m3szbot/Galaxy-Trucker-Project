package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientServerInvokableMethods;
import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;
import it.polimi.ingsw.Connection.ClientSide.utils.ViewCommunicator;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.Model.GameInformation.GameType;
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
                System.out.println("You have been disconnected from the server");
                ClientInputManager.unblockInput();
                break;
            } catch (Exception e) {
                System.err.println("Unexpected error in receiver");
                e.printStackTrace();
            }

        }

    }

    /***
     * Method which recognized the command or, if not recognized, call the method of the
     * view which name correspond to the command parameter
     * @param command to be executed
     * @param dataContainer containing all the information sent by the server
     */

    private void executeCommand(String command, DataContainer dataContainer) {
        if (command.equals("setGamePhase")) {
            setGamePhase(dataContainer.getGamePhase());
        } else if (command.equals("endGame")) {
            endGame();
        } else if (command.equals("joined")) {
            ClientInputManager.setTimeOut(300000);
        } else if (command.equals("unblock")) {
            ClientInputManager.unblockInput();
        } else if(command.equals("setGameType")){
            viewCommunicator.setGameType(GameType.valueOf(dataContainer.getMessage()));
        } else {
            callView(dataContainer);
        }
    }

    /**
     * Sets the game phase of the game.
     * @param gamePhase
     */

    public void setGamePhase(GamePhase gamePhase) {

        viewCommunicator.setGamePhase(gamePhase);

    }

    /**
     * Ends the game. It causes the termination of the messageReceiver thread and
     * of the messageSender thread.
     */

    public void endGame() {
        running.set(false);
        System.out.println("The game has ended");
        ClientInputManager.unblockInput();
    }

    /**
     * Method which invoke the method of the current view, which name is contained in the command
     * attribute of the container
     * @param container
     */

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
            System.err.printf("Critical error while accessing view method: method %s not found\n", methodName);
            e1.printStackTrace();
            System.out.println("You have been disconnected");
            ClientInputManager.unblockInput();
        } catch (IllegalAccessException e2) {
            running.set(false);
            System.err.printf("Critical error while accessing view method: method %s does not have access to the definition of the specified class\n", methodName);
            e2.printStackTrace();
            System.out.println("You have been disconnected");
            ClientInputManager.unblockInput();
        } catch (InvocationTargetException e3) {
            running.set(false);
            System.err.printf("Critical error while accessing view method: the method invoked %s did not behave correctly\n", methodName);
            e3.printStackTrace();
            System.out.println("You have been disconnected");
            ClientInputManager.unblockInput();
        }

    }

}
