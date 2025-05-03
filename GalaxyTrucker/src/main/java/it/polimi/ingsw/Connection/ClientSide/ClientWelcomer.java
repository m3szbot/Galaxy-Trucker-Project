package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;

import java.util.InputMismatchException;
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

        //there can be duplicated names!

        System.out.print("Enter you're nickname: ");

        input = scanner.nextLine();

        clientInfo.setNickname(input);
        System.out.println("You're nickname is " + input);

        System.out.print("Enter the viewType (GUI/TUI): ");

        input = scanner.nextLine();
        input.toUpperCase();

        while (!input.equals("GUI") && !input.equals("TUI")) {

            System.out.print("The string you entered is not valid, please enter a valid one (GUI/TUI): ");
            input = scanner.nextLine();

        }

        clientInfo.setViewType(ViewType.valueOf(input));
        System.out.println("You're viewType is " + input);


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
        input = scanner.nextLine();
        clientInfo.setServerIp(input);
        System.out.println("The ip adress of the server is " + input);

        System.out.println("Do you want to resume an interrupted game ? (y/n)");

        input = scanner.nextLine();

        while(true){

            input.toLowerCase();

            if(input.equals('y')){

                System.out.println("Enter the game code of the interrupted game: ");

                try {

                    int gameCode = scanner.nextInt();
                    clientInfo.setGameCode(gameCode);
                    break;

                }catch (InputMismatchException e){

                    System.out.println("The input is incorrect, you must enter an integer.");
                    continue;

                }


            }
            else if(input.equals('n')){

                break;

            }

            System.out.println("The string you entered is invalid, please reenter it (y/n): ");
            input = scanner.nextLine();
        }
    }
}
