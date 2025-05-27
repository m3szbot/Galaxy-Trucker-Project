package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ServerSide.RMI.ServerRemoteInterface;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Thread which ping the server to see if he is disconnected.
 */

public class RMIServerPinger implements Runnable{

    private ServerRemoteInterface virtualServer;
    private AtomicBoolean serverConnected;

    public RMIServerPinger(ServerRemoteInterface virtualServer, AtomicBoolean serverConnected){
        this.virtualServer = virtualServer;
        this.serverConnected = serverConnected;
    }

    @Override
    public void run() {

        while(true){

            try {

                virtualServer.isAlive();

            } catch (RemoteException e) {
                serverConnected.set(false);
                break;
            }

            try {

                Thread.sleep(2000);

            } catch (InterruptedException e) {
                //the player has terminated the joining phase
            }

        }

    }
}
