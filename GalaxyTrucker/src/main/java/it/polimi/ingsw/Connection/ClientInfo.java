package it.polimi.ingsw.Connection;

import it.polimi.ingsw.Application.ConnectionType;
import it.polimi.ingsw.Application.ViewType;

public class ClientInfo {

    private String nickname;
    private ConnectionType connectionType;
    private ViewType viewType;
    private int gameCode;

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
}
