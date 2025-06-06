package it.polimi.ingsw.Connection.ClientSide.RMI;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInfo;
import it.polimi.ingsw.Connection.ServerSide.RMI.ServerRemoteInterface;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicBoolean;

public class RMIGameHandler {

    private ClientInfo clientInfo;

    public RMIGameHandler(ClientInfo clientInfo){
        this.clientInfo = clientInfo;
    }

    public void start(){

        ServerRemoteInterface virtualServer;
        ClientRemoteInterface virtualClient;
        RMIServerPinger rmiServerPinger;
        AtomicBoolean serverConnected = new AtomicBoolean(true);

        try {

        virtualServer = (ServerRemoteInterface) Naming.lookup("rmi://localhost/virtualServer");
        rmiServerPinger = new RMIServerPinger(virtualServer, serverConnected);

        }catch (RemoteException e){

            System.err.println("RMI remote registry could not be contacted.");
            return;

        } catch (NotBoundException e) {

            System.err.println("The name of the remote object is not bound");
            return;

        } catch (MalformedURLException e) {

            System.err.println("The rmi url is incorrect");
            return;

        }

        try{

            virtualClient = new VirtualClient(clientInfo);

        } catch (RemoteException e) {

            System.err.println("An error occurred when creating the virtualClient object");
            return;

        }

        try{

            Thread pinger = new Thread(rmiServerPinger);
            pinger.start();

            virtualServer.registerClient(InetAddress.getLocalHost().getHostAddress());
            virtualServer.makePlayerJoin(virtualClient, clientInfo);

            while(virtualClient.isInGame() && serverConnected.get());

            if(!pinger.isInterrupted()){
                pinger.interrupt();
            }

        } catch (UnknownHostException e) {

            System.err.println("Local hostname could not be solved into an address");

        } catch (RemoteException e) {

            System.out.println("The communication was interrupted, remote exception message: " + e.getMessage());

        }
    }

}
