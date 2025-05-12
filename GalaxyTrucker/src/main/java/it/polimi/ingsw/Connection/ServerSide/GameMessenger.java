package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * Class used to communicate with players during the game.
 * @author carlo
 */

public class GameMessenger {

    private Map<Player, Socket> playerSocketMap = new HashMap<>();
    private Map<Player, DataContainer> playerDataContainerMap = new HashMap<>();

    /**
     * To call when a player is added to the game
     * @param player
     * @param socket null if the player chose RMI
     */

    public void addPlayer(Player player, Socket socket){

        this.playerSocketMap.put(player, socket);
        this.playerDataContainerMap.put(player, new DataContainer());

    }

    /**
     *
     * @param player
     * @return the player dataContainer
     */

    public DataContainer getPlayerContainer(Player player){

        return playerDataContainerMap.get(player);

    }

    /**
     * Sends to the player his dataContainer, then clears the container.
     * @param player
     */

    public void sendPlayerData(Player player){

        Socket playerSocket = playerSocketMap.get(player);

        if(playerSocket == null){
            //RMI
            //TODO
        }
        else{
            //socket

            try {

                (new ObjectOutputStream(playerSocket.getOutputStream())).writeObject(playerDataContainerMap.get(player));

            }catch (IOException e){
                System.err.println();
            }
            finally {
            playerDataContainerMap.get(player).clearContainer();
            }

        }
    }

    /**
     *
     * @param player
     * @return the string that the player sent to the server
     */

    public String getPlayerInput(Player player) throws PlayerDisconnectedException{

        if(playerSocketMap.get(player) == null){
            //RMI
            //TODO
        }
        else{
            try {
                return (new DataInputStream((playerSocketMap.get(player)).getInputStream())).readUTF();
            } catch (IOException e) {

                System.err.println("Error while reading from client");
                e.printStackTrace();

                //closing player socket

                try {
                    playerSocketMap.get(player).close();
                } catch (IOException ex) {
                    System.err.println("Error while closing disconnected player socket");
                }

                playerSocketMap.remove(player);
                playerDataContainerMap.remove(player);

                throw new PlayerDisconnectedException(player);

            }

        }

        return null;

    }
}
