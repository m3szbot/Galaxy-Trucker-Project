package it.polimi.ingsw.Connection.ServerSide.RMI;

/**
 * Thread that listen for incoming clients with RMI
 * protocol
 *
 * @author carlo
 */
/*

public class RMIListener implements Runnable{

    private ServerInterfaceImpl serverInterface;

    public RMIListener(ServerInterfaceImpl serverInterface){
       this.serverInterface = serverInterface;
    }

    public void run(){

       try {
           LocateRegistry.createRegistry(1099);

           Naming.bind("serverInterface", serverInterface);

           System.out.println("Rmi listener is activated and is listening...");


       } catch (RemoteException e) {
           System.err.println("Errow while initializing RMI");
       } catch (MalformedURLException e) {
           System.err.println("Error with RMI URL");
       } catch (AlreadyBoundException e) {
           System.err.println("Name joiner is already bounded");
       }


   }


}
 */
