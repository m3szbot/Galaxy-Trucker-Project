package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.RMI.ClientRemoteInterface;

import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A pinger that is used during the joining phase to check the
 * connectivity of the client.
 *
 * @author carlo
 */

public class RMIClientPinger implements Runnable{

    private ClientRemoteInterface virtualClient;
    private AtomicBoolean clientConnected;

    public RMIClientPinger(ClientRemoteInterface virtualClient, AtomicBoolean clientConnected){
        this.virtualClient = virtualClient;
        this.clientConnected = clientConnected;
    }

    @Override
    public void run() {

       while(true){

           try {

               virtualClient.isAlive();

           } catch (RemoteException e) {
               clientConnected.set(false);
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
