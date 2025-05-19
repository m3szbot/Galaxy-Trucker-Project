package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.RMI.RMICommunicator;
import it.polimi.ingsw.Connection.ServerSide.RMI.RMICommunicatorImpl;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Class responsible for exchanging data both with RMI and socket protocol.
 */

public class DataExchanger {

    private RMICommunicatorImpl rmiCommunicatorServerSide;
    private RMICommunicator rmiCommunicatorClientSide;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;
    private String nickname;
    private ConnectionType connectionType;
    private Integer trials;
    private final int MAXTRIALS = 5;

    public DataExchanger(RMICommunicatorImpl rmiCommunicatorServerSide, String nickname, int gameCode, ConnectionType connectionType){
        this.rmiCommunicatorServerSide = rmiCommunicatorServerSide;
        this.nickname = nickname;
        this.connectionType = connectionType;
    }

    public DataExchanger(RMICommunicator rmiCommunicatorClientSide, String nickname, int gameCode, ConnectionType connectionType){
        this.rmiCommunicatorClientSide = rmiCommunicatorClientSide;
        this.nickname = nickname;
        this.connectionType = connectionType;
    }

    public DataExchanger(Socket clientSocket, ObjectOutputStream outputStream, ObjectInputStream inputStream, ConnectionType connectionType){
        this.clientSocket = clientSocket;
        this.outputStream = outputStream;
        this.inputStream = inputStream;
        this.connectionType = connectionType;
    }

    public Socket getClientSocket(){
        return clientSocket;
    }

    public void setTrials(Integer trials){
        this.trials = trials;
    }

    public ObjectInputStream getInputStream(){
        return inputStream;
    }

    public ObjectOutputStream getOutputStream(){
        return outputStream;
    }

    public RMICommunicatorImpl getServerRmiCommunicator() {
        return rmiCommunicatorServerSide;
    }
    public RMICommunicator getRmiCommunicatorClientSide(){return rmiCommunicatorClientSide;};

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public String receiveMessage(boolean isServer) throws IOException{

        if(connectionType == ConnectionType.SOCKET){

            if(isServer){
                return inputStream.readUTF();
            }
            else{
                try {
                    return ((DataContainer) inputStream.readObject()).getMessage();
                } catch (ClassNotFoundException e) {
                    System.err.println("Error while casting data container class");
                }
            }

        }
        else{

            if(isServer) {

                if(trials.equals(MAXTRIALS)){
                    throw new IOException();
                }

                return rmiCommunicatorServerSide.getPlayerInput(nickname);

            }
            else{


                return rmiCommunicatorClientSide.getContainer(nickname).getMessage();

            }

        }
        return null;
    }

    public DataContainer receiveDataContainer() throws IOException{

        if(connectionType == ConnectionType.SOCKET){
            try {
                return (DataContainer) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                System.err.println("Container not received correctly");
            }
        }
        else{

            return rmiCommunicatorClientSide.getContainer(nickname);

        }
        return null;
    }

    public void sendDataContainer(DataContainer dataContainer) throws IOException{

       if(connectionType == ConnectionType.SOCKET){

          outputStream.writeObject(dataContainer);
          outputStream.flush();
          outputStream.reset();

       }
       else{

           rmiCommunicatorServerSide.setPlayerContainer(nickname, dataContainer);
       }
    }

    public void sendMessage(String message, boolean isServer) throws IOException{

        if(connectionType == ConnectionType.SOCKET){

            if(isServer){

                DataContainer dataContainer = new DataContainer();
                dataContainer.setMessage(message);

                outputStream.writeObject(dataContainer);
                outputStream.flush();

            }else{

                outputStream.writeUTF(message);
                outputStream.flush();

            }

        }
        else{

            if(!isServer){

                rmiCommunicatorClientSide.setPlayerInput(nickname, message);

            }
            else {

                DataContainer container = new DataContainer();
                container.setMessage(message);

                rmiCommunicatorServerSide.setPlayerContainer(nickname, container);

            }
        }

    }

    public void closeResources() throws IOException{

        if(connectionType == ConnectionType.SOCKET){

            clientSocket.close();
            inputStream.close();
            outputStream.close();

        }
        else{
            rmiCommunicatorServerSide.clearPlayer(nickname);
        }

    }
}
