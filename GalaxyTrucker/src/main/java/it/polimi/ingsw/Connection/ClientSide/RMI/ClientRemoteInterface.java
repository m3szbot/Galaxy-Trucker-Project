package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.ClientServerInvokableMethods;
import it.polimi.ingsw.View.ViewServerInvokableMethods;

import java.rmi.Remote;

public interface ClientRemoteInterface extends Remote, ViewServerInvokableMethods, ClientServerInvokableMethods {
}
