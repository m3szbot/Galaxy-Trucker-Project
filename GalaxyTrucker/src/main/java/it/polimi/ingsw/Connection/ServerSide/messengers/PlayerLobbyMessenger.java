package it.polimi.ingsw.Connection.ServerSide.messengers;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;
import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.PlayerDisconnectedException;
import it.polimi.ingsw.Connection.ServerSide.socket.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Model.GameInformation.GameType;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.TimeoutException;

/**
 * Class which is used to communicate with the player during the lobby time.
 *
 * @author carlo
 */

public class PlayerLobbyMessenger {

    private ConnectionType connectionType;
    private DataContainer dataContainer;
    private SocketDataExchanger socketDataExchanger;
    private ClientRemoteInterface virtualClient;
    private String nickName;

    public PlayerLobbyMessenger(ClientRemoteInterface virtualClient, String nickName){
        this.connectionType = ConnectionType.RMI;
        this.virtualClient = virtualClient;
        this.nickName = nickName;
    }

    public PlayerLobbyMessenger(SocketDataExchanger socketDataExchanger, String nickName){
        this.connectionType = ConnectionType.SOCKET;
        this.socketDataExchanger = socketDataExchanger;
        this.nickName = nickName;
        this.dataContainer = new DataContainer();
    }

    public SocketDataExchanger getDataExchanger() {
        return socketDataExchanger;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public ClientRemoteInterface getVirtualClient() {
        return virtualClient;
    }

    public String getPlayerString() throws PlayerDisconnectedException, TimeoutException {

       if(connectionType == ConnectionType.SOCKET){

           try{

               String input = socketDataExchanger.getString();
               if(input.equals("inactivity")){
                   throw new TimeoutException();
               }
               return input;

           } catch (IOException e) {

               System.err.println("Error while obtaining data from " + nickName + ": " +
                       "a disconnection probably occurred");
               throw new PlayerDisconnectedException(nickName);

           }

       }
       else{

           try{

               return virtualClient.getString();
           }
           catch (RemoteException e){

               if(e.getMessage().contains("inactivity")){
                   throw new TimeoutException();
               }

               System.err.println("Error while obtaining data from " + nickName + ": " +
                       "a disconnection probably occurred");
               throw new PlayerDisconnectedException(nickName);

           }

       }

    }

    public void sendCommand(String command){
        if(connectionType == ConnectionType.SOCKET){

            dataContainer.clearContainer();
            dataContainer.setCommand(command);

            try{
                socketDataExchanger.sendContainer(dataContainer);
            } catch (IOException e) {

                System.err.println("Error while sending data to " + nickName);
            }

        }
    }

    public void joinGame(){
        if(connectionType == ConnectionType.SOCKET){
            sendCommand("joined");
        }
        else{
            try {
                virtualClient.setInGame(true);
            } catch (RemoteException e) {
                System.err.println("Error while communicating with " + nickName + " through rmi protocol");
            }
        }
    }

    public void sendGameType(GameType gameType){

        if(connectionType == ConnectionType.SOCKET){

            dataContainer.clearContainer();
            dataContainer.setCommand("setGameType");
            dataContainer.setMessage(gameType.toString());

            try{
                socketDataExchanger.sendContainer(dataContainer);
            } catch (IOException e) {

                System.err.println("Error while sending data to " + nickName);
            }

        }
        else{

            try {

                virtualClient.setGameType(gameType.toString());

            } catch (RemoteException e) {
                System.err.println("Error while communicating with " + nickName + " through rmi protocol");
            }

        }

    }

    public void printMessage(String message){

        if(connectionType == ConnectionType.SOCKET){

            dataContainer.clearContainer();
            dataContainer.setCommand("printMessage");
            dataContainer.setMessage(message);

            try{
                socketDataExchanger.sendContainer(dataContainer);
            } catch (IOException e) {

                System.err.println("Error while sending data to " + nickName);
            }

        }
        else{

            try {

                virtualClient.printMessage(message);

            } catch (RemoteException e) {
                System.err.println("Error while sending data to " + nickName);
            }

        }

    }

    public void cleanResources(){
       if(connectionType == ConnectionType.SOCKET) {
           socketDataExchanger.closeResources();
       }
       else{
           try {
               virtualClient.sendCommand("kicked");
           } catch (RemoteException e) {
               System.err.println("Error while communicating through rmi protocol with " + nickName);
           }
       }
    }
}
