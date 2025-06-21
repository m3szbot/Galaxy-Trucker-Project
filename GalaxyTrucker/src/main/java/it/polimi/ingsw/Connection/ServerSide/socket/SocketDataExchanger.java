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

    public String getString() throws IOException {

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

    /*
    Important: if the resources are closed after a write failure, the stream is corrupted and therefore
    most likely an exception will be thrown on the corrupted one. On the other hand, if they are closed
    after a read failure no exception are thrown.
     */

    public void closeResources(){

        try {
            clientSocket.close();
        } catch (IOException e) {
            System.err.println("Error closing client socket: " + e.getMessage());
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing input stream: " + e.getMessage());
        }

        try {
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Error closing output stream: " + e.getMessage());
        }

        System.out.println("Resources closed successfully");

    }


}
