package it.polimi.ingsw.Connection.ServerSide;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class RMIListener implements Runnable{

   public void run(){

       try {
           LocateRegistry.createRegistry(1099);
       } catch (RemoteException e) {
           e.printStackTrace();
       }


   }


}
