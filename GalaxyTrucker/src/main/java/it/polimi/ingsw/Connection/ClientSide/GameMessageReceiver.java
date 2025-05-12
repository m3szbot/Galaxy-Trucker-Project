package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.View.GeneralView;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class GameMessageReceiver implements Runnable{

    private Map<String, GeneralView> viewMap = new HashMap<>();
    private ObjectInputStream in;
    private String currentPhase;


    public GameMessageReceiver(GeneralView[] views, ObjectInputStream in){

        String[] phases = {"assembly", "correction", "flight", "evaluation"};

        for(int i = 0; i < views.length; i++){

            this.viewMap.put(phases[i], views[i]);

        }

        this.in = in;
        this.currentPhase = "assembly";

    }

    public void run(){

        int result;

        while(true){

            try {
                DataContainer container = (DataContainer) in.readObject();

                if(executeCommand(container.getCommand()) == -1){
                    break;
                }

                if(callView(container) == -1) {
                   throw new IOException();
                }


            } catch (IOException e) {
                System.err.println("Critical error while receiving messages");
                break;
            } catch (ClassNotFoundException e) {

                System.err.println("DataContainer class not recognized");
                break;

            }

        }

    }

    private void advancePhase(){

        switch (currentPhase){
            case "assembly": currentPhase = "correction";
            break;
            case "correction": currentPhase = "flight";
            break;
            case "flight": currentPhase = "evaluation";
            break;
        }

    }

    private int executeCommand(String command){

        if(command.equals("advance phase")){
            advancePhase();
        }
        else if(command.equals("game ended")){
            return -1;
        }

        return 0;

    }

    private int callView(DataContainer container){

        GeneralView currentView = viewMap.get(currentPhase);
        String methodName = container.getCommand();

        if(currentView == null){
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
}
