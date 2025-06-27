package it.polimi.ingsw.Connection.ClientSide.utils;

import java.util.Scanner;

/**
 * Thread which reads from the user continually. It is a daemon
 * thread as it stops only when the program ends. It gets the user
 * input and set it through the ClientInputManager class.
 *
 * @author carlo
 */

public class InputDaemon extends Thread{

    public void run(){

        Scanner scanner = new Scanner(System.in);

       while(true){
          ClientInputManager.setUserInput(scanner.nextLine());
       }

    }
}
