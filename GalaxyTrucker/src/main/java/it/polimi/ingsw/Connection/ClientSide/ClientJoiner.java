package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.DataExchanger;
import it.polimi.ingsw.Connection.ServerSide.RMI.RMICommunicator;
import it.polimi.ingsw.Connection.ViewType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Client joiner is responsible for the second phase of the client
 * lifecycle, i.e, making the client join a game. It returns 1, if the client
 * join the game correctly, 0 if he is kicked, -1 if he encountered a connection
 * issue.
 *
 * @author carlo
 */


public class ClientJoiner {

    private AtomicReference<String> userInput;

    public int start(ClientInfo clientInfo) {

        if (clientInfo.getViewType() == ViewType.TUI) {

            return startTUI(clientInfo);


        } else {
            //GUI
        }

        return 0; //if no returns have been activated, the player is automatically kicked.
    }

    private int startTUI(ClientInfo clientInfo) {

        DataExchanger dataExchanger;
        Socket socket;
        AtomicBoolean terminatedFlag = new AtomicBoolean(false);
        AtomicBoolean abnormallyTerminatedFlag = new AtomicBoolean(false);
        this.userInput = clientInfo.getUserInput();

        if (clientInfo.getConnectionType() == ConnectionType.SOCKET) {

            try {
                socket = new Socket(clientInfo.getServerIp(), clientInfo.getServerPort());
                clientInfo.setServerSocket(socket);
                ObjectOutputStream dataSender = new ObjectOutputStream(socket.getOutputStream());
                dataSender.flush();
                ObjectInputStream dataReceiver = new ObjectInputStream(socket.getInputStream());

                dataSender.writeUTF(clientInfo.getNickname());
                dataSender.flush();

                String input = dataReceiver.readUTF();

                while (!input.equals("nicknameSet")) {

                    System.out.println(input);

                    while (userInput.get() == null) ;

                    dataSender.writeUTF(userInput.getAndSet(null));
                    dataSender.flush();
                    input = dataReceiver.readUTF();

                }

                System.out.println("You're nickname was changed successfully");

                input = dataReceiver.readUTF();
                clientInfo.setNickname(input);

                dataExchanger = new DataExchanger(socket, dataSender, dataReceiver, ConnectionType.SOCKET);
                clientInfo.setDataExchanger(dataExchanger);

            } catch (IOException e) {
                System.err.println("Error while connecting to the server");
                return -1;
            }

        } else {

            RMICommunicator rmiCommunicator;

            try {

                boolean flag = false;

                rmiCommunicator = (RMICommunicator) Naming.lookup("rmi://localhost/RMICommunicator");
                rmiCommunicator.registerClient(InetAddress.getLocalHost().getHostAddress());

                while (rmiCommunicator.checkNicknameAvailability(clientInfo.getNickname())) {
                    flag = true;

                    changeNickName(clientInfo);

                }

                if (flag) {

                    System.out.println("You're nickname was changed successfully");

                }

                dataExchanger = new DataExchanger(rmiCommunicator, clientInfo.getNickname(), rmiCommunicator.getCurrentGameCode(), ConnectionType.RMI);
                rmiCommunicator.makeClientJoin(clientInfo.getNickname());

            } catch (NotBoundException | RemoteException | MalformedURLException | UnknownHostException e) {
                System.err.println("Error while connecting to the server registry");
                return -1;
            }

        }

        clientInfo.setDataExchanger(dataExchanger);

        try {

            dataExchanger.sendMessage(String.valueOf(clientInfo.getGameCode()), false);

        } catch (IOException e) {
            System.err.println("Critical error while sending gameCode to server");
        }

        //One thread to receive feedback from the server

        Thread messageReceiver = new Thread(() -> {

            try {
                int trials = 0;

                while (true) {

                    if (checkTrials(trials)) {
                        terminatedFlag.set(true);
                        abnormallyTerminatedFlag.set(true);
                        break;
                    }

                    String message = dataExchanger.receiveMessage(false);

                    if (message.equals("added")) {
                        terminatedFlag.set(true);

                    } else if (message.equals("start")) {
                        break;
                    } else if (message.equals("increment trials")) {
                        trials++;
                    } else if (message.equals("terminate")) {

                        terminatedFlag.set(true);
                        abnormallyTerminatedFlag.set(true);

                        break;
                    } else {

                        System.out.println(message);
                    }

                }

            } catch (IOException e) {
                abnormallyTerminatedFlag.set(true);
                terminatedFlag.set(true);
                System.err.println("An error was encountered while receiving data from the server");
            }

        });

        Thread messageSender = new Thread(() -> {

            while (!terminatedFlag.get()) {

                if (userInput.get() != null) {

                    try {

                        dataExchanger.sendMessage(userInput.getAndSet(null), false);

                    } catch (IOException e) {
                        abnormallyTerminatedFlag.set(true);
                        terminatedFlag.set(true);
                        System.err.println("An error was encountered while sending data to the server");
                    }
                }

                try {
                    Thread.sleep(100);

                } catch (InterruptedException e) {
                    System.err.println("Sender thread was abnormally interrupted");
                }

            }

        });

        messageReceiver.start();
        messageSender.start();

        try {

            messageReceiver.join();
            messageSender.join();

        } catch (InterruptedException e1) {
            System.err.println("Error: message receiver or sender was interrupted abnormally");
            return -1;

        }

        if (abnormallyTerminatedFlag.get()) {
            return -1;
        } else {
            return 0;
        }
    }

    private void changeNickName(ClientInfo clientInfo) {

        System.out.println("You're nickname has already been chosen, please enter a new one: ");

        while (userInput.get() == null) ;

        clientInfo.setNickname(userInput.getAndSet(null));

    }

    private boolean checkTrials(int trials) {

        if (trials >= 5) {
            System.out.println("You entered too much wrong information and therefore are slowing down " +
                    "the server, you have been kicked out");
            return true;
        }
        return false;
    }

}


