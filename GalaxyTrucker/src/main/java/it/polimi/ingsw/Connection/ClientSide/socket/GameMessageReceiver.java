package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientServerInvokableMethods;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GeneralView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that receive the messages that the server sends to the player
 *
 * @author carlo
 */

public class GameMessageReceiver implements Runnable, ClientServerInvokableMethods {

    private GeneralView[] views;
    private ObjectInputStream in;
    private AtomicBoolean running;
    private int viewIndex;
    private SocketDataExchanger dataExchanger;


    public GameMessageReceiver(GeneralView[] views, SocketDataExchanger dataExchanger, AtomicBoolean running) {

        this.views = views;
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
                break;
            } catch (Exception e) {
                System.err.println("Unexpected error in receiver: " + e.getMessage());
                e.printStackTrace();
            }

        }

    }


    private void executeCommand(String command, DataContainer dataContainer) {
        if (command.equals("setGamePhase")) {
            setGamePhase(dataContainer.getGamePhase());
        } else if (command.equals("endGame")) {
            endGame();
        }
        else if(command.equals("inactivity")){
            System.out.println("Timeout reached, you are considered inactive, disconnection will soon happen");
            running.set(false);
        }
        else {
            callView(dataContainer);
        }
    }

    public void setGamePhase(GamePhase gamePhase) {

        switch (gamePhase) {
            case Assembly -> viewIndex = 0;
            case Correction -> viewIndex = 1;
            case Flight -> viewIndex = 2;
            case Evaluation -> viewIndex = 3;
        }

    }

    public void endGame() {
        running.set(false);
        System.out.println("The game has ended");
    }

    private void callView(DataContainer container) {

        GeneralView currentView = views[viewIndex];
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
