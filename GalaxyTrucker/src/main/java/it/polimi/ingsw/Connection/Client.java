package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Application.ConnectionType;
import it.polimi.ingsw.Application.ViewType;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {

    private ClientInfo clientInfo = new ClientInfo();
    private String serverIp;
    private String input;
    private Socket socket;
    private static final int port = 5000;

    public void welcomeTheClient(){

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to galaxy trucker\n");

        //there can be duplicated names!

        System.out.print("Enter you're nickname: ");

        input = scanner.nextLine();

        clientInfo.setNickname(input);
        System.out.println("You're nickname is now " + input);

        System.out.print("Enter the viewType (GUI/TUI): ");

        input = scanner.nextLine();
        input.toUpperCase();

        while(!input.equals("GUI") && !input.equals("TUI")){

            System.out.print("The string you entered is not valid, please enter a valid one (GUI/TUI): ");
            input = scanner.nextLine();

        }

        clientInfo.setViewType(ViewType.valueOf(input));
        System.out.println("You're viewType is now " + input);


        System.out.print("Enter the connection type (RMI/SOCKET): ");

        input = scanner.nextLine();
        input.toUpperCase();

        while(!input.equals("RMI") && !input.equals("SOCKET")){

            System.out.print("The string you entered is not valid, please enter a valid one (RMI/SOCKET): ");
            input = scanner.nextLine();

        }

        clientInfo.setConnectionType(ConnectionType.valueOf(input));
        System.out.println("You're connection type is now " + input);

        System.out.print("Enter the IP adress of the server: ");
        serverIp = scanner.nextLine();


    }

    public void connectTheClient(){

        if(clientInfo.getConnectionType() == ConnectionType.Socket){
            //socket connection

            connectWithSocket();

        }
        else{
            //RMI connection

            connectWithRMI();

        }

    }

    private void connectWithRMI(){



    }

    private void connectWithSocket(){

        try {

            Socket socket = new Socket(serverIp, port);

        } catch (Exception e) {

            //TODO

        }

        

    }

}
