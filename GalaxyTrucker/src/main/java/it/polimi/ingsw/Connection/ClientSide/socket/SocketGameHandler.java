package it.polimi.ingsw.Connection.ClientSide.socket;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Connection.ViewType;
import it.polimi.ingsw.View.GUI.GUILoader;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Game handles for the socket protocol
 *
 * @author carlo
 */

public class SocketGameHandler {

    private GameStarter gameStarter = new GameStarter();
    private ClientInfo clientInfo;

    public SocketGameHandler(ClientInfo clientInfo){
        this.clientInfo = clientInfo;

    }


    public void start() {

        Socket serverSocket;

        try {

            serverSocket = new Socket(clientInfo.getServerIp(), clientInfo.getServerPort());

        } catch (IOException e) {
            System.err.println("Error while reaching the server, check your IP!");
            return;
        }

        SocketDataExchanger socketDataExchanger;

        try {

            socketDataExchanger = setUpConnection(serverSocket);

        } catch (IOException e) {
            System.err.println("Error while setting up streams");
            return;
        }

        if(isNickNameRepeated(socketDataExchanger)){

            socketDataExchanger.closeResources();

            return;
        }

        clientInfo.setDataExchanger(socketDataExchanger);

        gameStarter.start(clientInfo);

        clientInfo.getDataExchanger().closeResources();

    }

    private boolean isNickNameRepeated(SocketDataExchanger socketDataExchanger){

        try {

            socketDataExchanger.sendString(clientInfo.getNickname());

            String result = socketDataExchanger.getString();


            if(result.equals("OK")){
                return false;
            }
            else{
                System.out.println(result);
                return true;
            }

        }catch (IOException e){
            System.err.println("Error while connecting to the server: check the server IP!");
            return true;
        }

    }


    private SocketDataExchanger setUpConnection(Socket serverSocket) throws IOException{

        SocketDataExchanger dataExchanger;

        ObjectInputStream inputStream;
        ObjectOutputStream outputStream;

        outputStream = new ObjectOutputStream(serverSocket.getOutputStream());
        outputStream.flush();
        inputStream = new ObjectInputStream(serverSocket.getInputStream());

        dataExchanger = new SocketDataExchanger(serverSocket, inputStream, outputStream);

        if(clientInfo.getViewType() == ViewType.GUI) {

            GUILoader.setSocketDataExchanger(dataExchanger);
        }

        return dataExchanger;

    }
}
