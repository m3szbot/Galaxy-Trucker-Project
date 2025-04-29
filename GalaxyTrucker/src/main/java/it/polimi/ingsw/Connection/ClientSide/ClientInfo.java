package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.ConnectionType;
import it.polimi.ingsw.Model.GameInformation.ViewType;

import java.io.Serializable;

public class ClientInfo implements Serializable {

    private String nickname;
    private ConnectionType connectionType;
    private ViewType viewType;
    private int gameCode;
    private String serverIp;
    private final static int serverPort = 5000;

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
