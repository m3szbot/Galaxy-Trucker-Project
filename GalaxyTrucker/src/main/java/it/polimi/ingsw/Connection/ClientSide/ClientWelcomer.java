package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;

import java.util.Scanner;

public class ClientWelcomer {

    ClientInfo clientInfo = new ClientInfo();
    String input;

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void start() {

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

        while (!input.equals("GUI") && !input.equals("TUI")) {

            System.out.print("The string you entered is not valid, please enter a valid one (GUI/TUI): ");
            input = scanner.nextLine();

        }

        clientInfo.setViewType(ViewType.valueOf(input));
        System.out.println("You're viewType is now " + input);


        System.out.print("Enter the connection type (RMI/SOCKET): ");

        input = scanner.nextLine();
        input.toUpperCase();

        while (!input.equals("RMI") && !input.equals("SOCKET")) {

            System.out.print("The string you entered is not valid, please enter a valid one (RMI/SOCKET): ");
            input = scanner.nextLine();

        }

        clientInfo.setConnectionType(ConnectionType.valueOf(input));
        System.out.println("You're connection type is now " + input);

        System.out.print("Enter the IP adress of the server: ");
        clientInfo.setServerIp(scanner.nextLine());


    }
}
