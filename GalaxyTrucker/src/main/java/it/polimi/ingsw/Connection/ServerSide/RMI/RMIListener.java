package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ServerSide.Server;

import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

/**
 * Thread that listen for incoming clients with RMI
 * protocol
 *
 * @author carlo
 */

public class RMIListener implements Runnable{

    private Server centralServer;

    public RMIListener(Server centralServer){

        this.centralServer = centralServer;

    }

   public void run(){

       try {
           LocateRegistry.createRegistry(1099);

           RMIJoinerImpl rmiJoiner = new RMIJoinerImpl(centralServer);

           Naming.bind("Joiner", rmiJoiner);

           System.out.println("Rmi listener is activated and is listening...");

       } catch (RemoteException e) {
           e.printStackTrace();
       } catch (MalformedURLException e) {
           throw new RuntimeException(e);
       } catch (AlreadyBoundException e) {
           throw new RuntimeException(e);
       }


   }


}
