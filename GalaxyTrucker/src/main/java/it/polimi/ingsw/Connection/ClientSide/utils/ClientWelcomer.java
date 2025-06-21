package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ViewType;

import java.util.Scanner;

/**
 * Class that handles the first phase of the client
 * lifecycle. It stores the client information into the
 * clientInfo bean.
 *
 * @author carlo
 */

public class ClientWelcomer {

    String input;

    public void start(ClientInfo clientInfo) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to galaxy trucker\n");

        //there can be duplicated names! (but not in the same game)

        System.out.print("Enter your nickname: ");

        input = scanner.nextLine();

        while (input.contains(" ") || input.equals("unblock") || input.equals("show-shipboard") ||
                input.equals("private-message") || input.equals("public-message")) {

            System.out.println("Your nickname is invalid!");
            System.out.print("Enter a valid nickname: ");
            input = scanner.nextLine();
        }

        clientInfo.setNickname(input);
        System.out.println("Your nickname is " + input);

        System.out.print("Enter the viewType (GUI/TUI): ");

        input = scanner.nextLine();
        input = input.toUpperCase();

        while (!input.equals("GUI") && !input.equals("TUI")) {

            System.out.print("The string you entered is not valid, please enter a valid one (GUI/TUI): ");
            input = scanner.nextLine();
            input = input.toUpperCase();

        }

        clientInfo.setViewType(ViewType.valueOf(input));
        System.out.println("Your viewType is " + input);


        System.out.print("Enter the connection type (RMI/SOCKET): ");

        input = scanner.nextLine();
        input = input.toUpperCase();

        while (!input.equals("RMI") && !input.equals("SOCKET")) {

            System.out.print("The string you entered is not valid, please enter a valid one (RMI/SOCKET): ");
            input = scanner.nextLine();
            input = input.toUpperCase();

        }

        clientInfo.setConnectionType(ConnectionType.valueOf(input));
        System.out.println("Your connection type is now " + input);

        System.out.print("Enter the IP address of the server: ");
        input = scanner.nextLine();
        clientInfo.setServerIp(input);
        System.out.println("The IP address of the server is " + input);
    }
}
