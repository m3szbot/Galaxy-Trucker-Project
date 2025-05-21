package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Model.GameInformation.GamePhase;

/**
 * Interface of methods that must be invokable on the Client by the Server.
 */
public interface ClientServerInvokableMethods {
    public void setGamePhase(GamePhase gamePhase);

    public void endGame();
}
