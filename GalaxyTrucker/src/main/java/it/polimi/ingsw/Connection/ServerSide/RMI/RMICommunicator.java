package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMICommunicator extends Remote {

    public void setPlayerInput(String nickname, String input) throws RemoteException;

    public DataContainer getContainer(String nickname) throws RemoteException;

    public void makeClientJoin(String nickname) throws RemoteException;

    public boolean checkNicknameAvailability(String nickname) throws RemoteException;

    public int getCurrentGameCode() throws RemoteException;

    public void registerClient(String ipAddress) throws RemoteException;


}
