package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;

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

    private String nickname;
    private ConnectionType connectionType;
    private ViewType viewType;
    private int gameCode = -1;
    private String serverIp;
    private final static int serverPort = 5000;
    private Socket serverSocket;

    public Socket getServerSocket() {
        return serverSocket;
    }

    public void setServerSocket(Socket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public int getGameCode() {
        return gameCode;
    }

    public String getNickname() {
        return nickname;
    }

    public ViewType getViewType() {
        return viewType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public void setGameCode(int gameCode) {
        this.gameCode = gameCode;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
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
