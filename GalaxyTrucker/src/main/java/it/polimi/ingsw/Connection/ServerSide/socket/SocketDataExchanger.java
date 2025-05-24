package it.polimi.ingsw.Connection.ServerSide.socket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

/**
 * Class that encapsulate the exchanging of data with socket
 *
 * @author carlo
 */

public class SocketDataExchanger {

    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private Socket clientSocket;

    public SocketDataExchanger(Socket clientSocket, ObjectInputStream inputStream, ObjectOutputStream outputStream){
        this.clientSocket = clientSocket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void setSocketTimeOut(int millisecond) throws SocketException {
        clientSocket.setSoTimeout(millisecond);
    }

    public String getString() throws IOException{

        return inputStream.readUTF();

    }

    public void sendString(String input) throws IOException{
        outputStream.writeUTF(input);
        outputStream.flush();
    }

    public void sendContainer(DataContainer container) throws IOException{
        synchronized (outputStream){
            outputStream.writeObject(container);
            outputStream.flush();
            outputStream.reset();
        }
    }

    public DataContainer getContainer() throws IOException{

        try {

            return (DataContainer) inputStream.readObject();

        } catch (ClassNotFoundException e) {
            System.err.println("Error while error while receiving data container");
        }

        throw new IOException();

    }


    public void closeResources() throws IOException{

        clientSocket.close();
        inputStream.close();
        outputStream.close();


    }


}
