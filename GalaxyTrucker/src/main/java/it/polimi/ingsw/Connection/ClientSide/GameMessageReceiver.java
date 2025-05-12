package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
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

    private Map<String, GeneralView> viewMap = new HashMap<>();
    private ObjectInputStream in;
    private String currentPhase;
    private AtomicBoolean running;


    public GameMessageReceiver(GeneralView[] views, ObjectInputStream in, AtomicBoolean running) {

        String[] phases = {"assembly", "correction", "flight", "evaluation"};

        for (int i = 0; i < views.length; i++) {

            this.viewMap.put(phases[i], views[i]);

        }

        this.in = in;
        this.currentPhase = "assembly";
        this.running = running;

    }

    public void run() {

        while (running.get()) {

            try {
                DataContainer container = (DataContainer) in.readObject();

                if (executeCommand(container.getCommand()) == -1) {

                    System.out.println("The game has ended, press any key to quit");
                    running.set(false);
                }

                if (callView(container) == -1) {
                    running.set(false);
                }


            } catch (IOException e) {
                System.err.println("Critical error while receiving messages, you have been disconnected");

                running.set(false);
            } catch (ClassNotFoundException e) {

                System.err.println("DataContainer class not recognized, you have been disconnected");

                running.set(false);

            }

        }

    }

    private int executeCommand(String command) {

        if (command.equals("advancePhase")) {
            advancePhase();
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

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Critical error while accessing view method ");
            e.printStackTrace();
            return -1;
        }
    }

    private void advancePhase() {

        switch (currentPhase) {
            case "assembly":
                currentPhase = "correction";
                break;
            case "correction":
                currentPhase = "flight";
                break;
            case "flight":
                currentPhase = "evaluation";
                break;
        }

    }
}
