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

        String[] phases = {"setup", "assembly", "correction", "flight"};

        for(int i = 0; i < views.length; i++){

            this.viewMap.put(phases[i], views[i]);

        }

        this.in = in;
        this.currentPhase = "setup";

    }

    public void run(){

        int result;

        while(true){

            try {
                DataContainer data = (DataContainer) in.readObject();
                result = executeCommand(data.getCommand());

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
            case "setup": currentPhase = "assembly";
            break;
            case "assembly": currentPhase = "correction";
            break;
        }

    }

    private int executeCommand(String command){

        if(command.equals("change phase")){

        }

    }

    private int callView(DataContainer container){

        GeneralView currentView = viewMap.get(currentPhase);

        if(currentView == null){
            System.err.println("Critical error: view not found");
            return -1;
        }

        String methodName = command.substring(0, command.indexOf('('));

        try {

            Method method = currentView.getClass().getMethod(methodName);

            method.invoke(currentView);

            return 0;

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.err.println("Critical error while accessing view method ");
            e.printStackTrace();
            return -1;
        }
    }
}
