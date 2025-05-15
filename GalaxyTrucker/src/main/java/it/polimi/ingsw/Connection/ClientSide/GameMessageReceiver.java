package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
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

public class GameMessageReceiver implements Runnable {

    private GeneralView[] views;
    private ObjectInputStream in;
    private AtomicBoolean running;
    private int viewIndex;


    public GameMessageReceiver(GeneralView[] views, ObjectInputStream in, AtomicBoolean running) {

        this.views = views;
        this.in = in;
        this.running = running;

    }

    public void run() {

        DataContainer container;

        while (running.get()) {

            try {

                container = (DataContainer) in.readObject();
                executeCommand(container.getCommand(), container);


            } catch (IOException e) {

                running.set(false);
                System.err.println("Critical error while receiving messages");
                System.out.println("You have been disconnected");

            } catch (ClassNotFoundException e) {

                running.set(false);
                System.err.println("DataContainer class not recognized");
                System.out.println("You have been disconnected");

            }

        }

    }

    private void executeCommand(String command, DataContainer dataContainer) {

        if (command.equals("setGamePhase")) {

            setGamePhase(dataContainer.getGamePhase());

        } else if (command.equals("endGame")) {

            running.set(false);
            System.out.println("The game has ended");

        }else{

            callView(dataContainer);

        }


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
        } catch (IllegalAccessException e2){
            running.set(false);
            System.err.println("Critical error while accessing view method: method does not have access to the definition of the specified class");
            System.out.println("You have been disconnected");
         }
        catch (InvocationTargetException e3){
            running.set(false);
            System.err.println("Critical error while accessing view method: the method invoked did not behave correctly");
            System.out.println("You have been disconnected");
        }
    }

    private void setGamePhase(GamePhase gamePhase){

        switch (gamePhase){
            case Assembly -> viewIndex = 0;
            case Correction -> viewIndex = 1;
            case Flight -> viewIndex = 2;
            case Evaluation -> viewIndex = 3;
        }

    }

}
