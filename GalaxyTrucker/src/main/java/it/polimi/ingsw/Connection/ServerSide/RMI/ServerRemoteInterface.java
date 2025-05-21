package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientServerInvokableMethods;
import it.polimi.ingsw.View.ViewServerInvokableMethods;

import java.rmi.Remote;

public interface ServerRemoteInterface extends Remote, ViewServerInvokableMethods, ClientServerInvokableMethods {
}
