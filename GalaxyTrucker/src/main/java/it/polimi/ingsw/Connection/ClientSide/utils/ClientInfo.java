package it.polimi.ingsw.Connection.ClientSide.utils;

import it.polimi.ingsw.Connection.ConnectionType;
import it.polimi.ingsw.Connection.ServerSide.socket.SocketDataExchanger;
import it.polimi.ingsw.Connection.ViewType;

import java.io.Serializable;
import java.net.Socket;

/**
 * Bean class which stores the client info, including
 * the gameCode of the game he is actually playing. If the
 * gameCode is set before the joining phase, it means
 * that the client wants to resume an interrupted game.
 *
 * @author carlo
 */

//gameCode=-1 if it is not selected yet

public class ClientInfo implements Serializable {

    private final static int serverPort = 5200;
    private String nickname;
    private transient ConnectionType connectionType;
    private transient ViewType viewType;
    private int gameCode;
    private transient String serverIp;
    private transient Socket serverSocket;
    private transient SocketDataExchanger dataExchanger;
    private transient ViewCommunicator viewCommunicator;

    public ClientInfo() {
        gameCode = -1;
    }

    private void setViewCommunicator(){
        if(viewType == ViewType.TUI){
            this.viewCommunicator = new ViewCommunicator(ViewType.TUI);
        }
        else{
            this.viewCommunicator = new ViewCommunicator(ViewType.GUI);
        }
    }

    public ViewCommunicator getViewCommunicator(){
        return this.viewCommunicator;
    }

    public SocketDataExchanger getDataExchanger() {
        return dataExchanger;
    }

    public void setDataExchanger(SocketDataExchanger dataExchanger) {
        this.dataExchanger = dataExchanger;
    }

    public Socket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
        viewCommunicator.setConnectionType(connectionType);
    }

    public int getGameCode() {
        return gameCode;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        setViewCommunicator();
    }

    public int getServerPort() {
        return serverPort;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }
}
