package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class GameMessenger {

    private Map<Player, Socket> playerSocketMap = new HashMap<>();
    private Map<Player, DataContainer> playerDataContainerMap = new HashMap<>();

    public void addPlayer(Player player, Socket socket){

        this.playerSocketMap.put(player, socket);
        this.playerDataContainerMap.put(player, new DataContainer());

    }

    public DataContainer getPlayerContainer(Player player){

        return playerDataContainerMap.get(player);

    }

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

        }


    }
}
