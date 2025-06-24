package it.polimi.ingsw.Connection.ClientSide.utils;

import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class responsible for managing user input. It is
 *
 * @author carlo
 */

public final class ClientInputManager {

    private static AtomicReference<String> userInput = new AtomicReference<>(null);
    private static long timeOut;


    public static void setUserInput(String input){
        userInput.set(input);
    }



    public static void unblockInput(){
        userInput.set("unblock");
    }

    public static void setTimeOut(long playerInputTimeOut){
        timeOut = playerInputTimeOut;
    }

    public static String getUserInput() throws TimeoutException {
        long temp = timeOut;

        while(true) {
            if (userInput.get() != null) {

                return userInput.getAndSet(null);

            } else {

                try {
                    Thread.sleep(100);
                    temp -= 100;

                    if(temp == 60000){
                        System.out.println("Warning: 1 minute left before you are being considered inactive, please enter an input");
                    }

                    if(temp < 0){
                        throw new TimeoutException();
                    }

                } catch (InterruptedException e) {
                    System.err.println("ClientInputManager thread abnormally interrupted");
                }
            }
        }
    }

}
