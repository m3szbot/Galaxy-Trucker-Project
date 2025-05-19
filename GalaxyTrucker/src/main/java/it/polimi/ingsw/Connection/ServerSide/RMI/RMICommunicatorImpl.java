package it.polimi.ingsw.Connection.ServerSide.RMI;

import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Connection.ServerSide.JoinerThread;
import it.polimi.ingsw.Connection.ServerSide.Server;
import it.polimi.ingsw.Model.GameInformation.ConnectionType;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class used to communicate through RMI. There is only 1 instance
 * of the class for the whole application.
 */

public class RMICommunicatorImpl extends UnicastRemoteObject implements RMICommunicator {

    private Map<String, AtomicReference<DataContainer>> playerContainers;

    private Map<String, AtomicReference<String>> playerInputs;

    private final Server centralServer;

    private int currentGameCode;

    public RMICommunicatorImpl(Server centralServer) throws RemoteException{
        playerContainers = new HashMap<>();
        playerInputs = new HashMap<>();
        this.centralServer = centralServer;
    }


    @Override
    public int getCurrentGameCode() throws RemoteException{
        return currentGameCode;
    }


    public void clearPlayer(String nickName){

        playerContainers.remove(nickName);
        playerInputs.remove(nickName);

    }

    @Override
    public DataContainer getContainer(String nickname) throws RemoteException {

        if(playerContainers.containsKey(nickname)) {

            AtomicReference<DataContainer> dataContainer = playerContainers.get(nickname);

            synchronized (dataContainer) {

                while (dataContainer.get() == null) {
                    try {
                        dataContainer.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Thread abnormally interrupted during wait");
                        e.printStackTrace();
                    }
                }

                DataContainer container = dataContainer.getAndSet(null);
                dataContainer.notifyAll();

                return container;

            }
        }
        else{
            throw new RemoteException();
        }
    }

    public String getPlayerInput(String nickname) throws RemoteException{

        if(playerInputs.containsKey(nickname)){

            AtomicReference<String> atomicReference = playerInputs.get(nickname);

            synchronized (atomicReference){

                while(atomicReference.get() == null){
                    try{
                        atomicReference.wait();
                    } catch (InterruptedException e) {
                        System.err.println("Thread abnormally interrupted during wait");
                        e.printStackTrace();
                    }
                }

                String input = atomicReference.getAndSet(null);
                atomicReference.notifyAll();
                return input;
            }

        }
        else{
            throw new RemoteException();
        }

    }

    @Override
    public void setPlayerInput(String nickname, String input) throws RemoteException {

        AtomicReference<String> atomicReference = playerInputs.get(nickname);

        synchronized (atomicReference){

            while(atomicReference.get() != null){
                try{
                    atomicReference.wait();
                } catch (InterruptedException e) {
                    System.err.println("Thread abnormally interrupted during wait");
                    e.printStackTrace();
                }
            }
            atomicReference.set(input);
            atomicReference.notifyAll();
        }

    }

    public void setPlayerContainer(String nickname, DataContainer container){

        AtomicReference<DataContainer> dataContainer = playerContainers.get(nickname);

        if(dataContainer == null){
            System.err.println("Player container not found for nickname " + nickname);
            return;
        }

        synchronized (dataContainer){

            while(dataContainer.get() != null){
                try {
                    dataContainer.wait();
                } catch (InterruptedException e) {
                    System.err.println("Thread abnormally interrupted during wait");
                    e.printStackTrace();
                }
            }

            dataContainer.set(container);
            dataContainer.notifyAll();

        }

    }

    private void addClient(String nickName){

        playerContainers.put(nickName, new AtomicReference<>(null));
        playerInputs.put(nickName, new AtomicReference<>(null));

    }

    @Override
    public void registerClient(String ipAddress) throws RemoteException {

        System.out.println(ipAddress + " is connected through RMI protocol");

    }

    @Override
    public void makeClientJoin(String nickname) throws RemoteException {

        addClient(nickname);
        JoinerThread joinerThread = new JoinerThread(this, ConnectionType.RMI, nickname, centralServer);
        joinerThread.start();

    }

    @Override
    public boolean checkNicknameAvailability(String nickname) throws RemoteException {

        return centralServer.checkNickname(nickname);

    }
}
