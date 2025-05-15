package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.GameInformation.GamePhase;
import it.polimi.ingsw.View.GeneralView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread that receive the messages that the server sends to the player
 *
 * @author carlo
 */

public class GameMessageReceiver implements Runnable {

    private Map<GamePhase, GeneralView> viewMap = new HashMap<>();
    private ObjectInputStream in;
    private GamePhase currentPhase;
    private AtomicBoolean running;


    public GameMessageReceiver(GeneralView[] views, ObjectInputStream in, AtomicBoolean running) {

        GamePhase[] phases = {GamePhase.Assembly, GamePhase.Correction, GamePhase.Flight, GamePhase.Evaluation};

        for (int i = 0; i < views.length; i++) {

            this.viewMap.put(phases[i], views[i]);

        }

        this.in = in;
        this.running = running;

    }

    public void run() {

        while (running.get()) {

            try {
                DataContainer container = (DataContainer) in.readObject();

                if (executeCommand(container.getCommand(), container) == -1) {

                    running.set(false);
                    System.out.println("The game has ended, press any key to quit");

                }

                if (callView(container) == -1) {
                    running.set(false);
                    System.out.println("You have been disconnected, press any key to quit");
                }


            } catch (IOException e) {

                running.set(false);
                System.err.println("Critical error while receiving messages, you have been disconnected, press" +
                        " any key to quit");

            } catch (ClassNotFoundException e) {

                running.set(false);
                System.err.println("DataContainer class not recognized, you have been disconnected, press any key to quit");

            }

        }

    }

    private int executeCommand(String command, DataContainer dataContainer) {

        if (command.equals("setGamePhase")) {
            setGamePhase(dataContainer.getGamePhase());
        } else if (command.equals("endGame")) {
            return -1;
        }

        return 0;

    }

    private int callView(DataContainer container) {

        GeneralView currentView = viewMap.get(currentPhase);
        String methodName = container.getCommand();

        if (currentView == null) {
            System.err.println("Critical error: view not found");
            return -1;
        }


        try {

            Method method = currentView.getClass().getMethod(methodName, DataContainer.class);

            method.invoke(currentView, container);

            return 0;

        } catch (NoSuchMethodException e1) {
            System.err.println("Critical error while accessing view method: method not found ");
            return -1;
        } catch (IllegalAccessException e2){
           System.err.println("Critical error while accessing view method: method does not have access to the definition of the specified class");
            return -1;
         }
        catch (InvocationTargetException e3){
            System.err.println("Critical error while accessing view method: the method invoked did not behave correctly");
            return -1;
        }
    }

    private void setGamePhase(GamePhase gamePhase){
        this.currentPhase = gamePhase;
    }

}
