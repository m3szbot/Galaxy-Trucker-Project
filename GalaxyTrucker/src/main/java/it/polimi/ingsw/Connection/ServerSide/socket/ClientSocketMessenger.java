package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to comunicate with the player
 *
 */

public class ClientSocketMessenger {

    private static Map<Player, DataOutputStream> playerDataOutputStreamMap = new ConcurrentHashMap<>();
    private static Map<Player, DataInputStream> playerDataInputStreamMap = new ConcurrentHashMap<>();
    private static Map<Player, ObjectOutputStream> playerObjectOutputStreamMap = new ConcurrentHashMap<>();

    public void addPlayerToSocket(Player player, Socket socket){

        try {

            playerDataOutputStreamMap.put(player, new DataOutputStream(socket.getOutputStream()));
            playerObjectOutputStreamMap.put(player, new ObjectOutputStream(socket.getOutputStream()));
            playerDataInputStreamMap.put(player, new DataInputStream(socket.getInputStream()));

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void sendObjectToPlayer(Player player, Object object){

        ObjectOutputStream sender = playerObjectOutputStreamMap.get(player);
        try{
            sender.writeObject(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessageToPlayer(Player player, String message){

       DataOutputStream sender = playerDataOutputStreamMap.get(player);
        try {
            sender.writeUTF(message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void sendMessageToAll(String message){

        for(DataOutputStream sender: playerDataOutputStreamMap.values()){
            try {
                sender.writeUTF(message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static int receiveInteger(Player player){

        DataInputStream receiver = playerDataInputStreamMap.get(player);

        try {
            return receiver.readInt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static String receiveString(Player player){

        DataInputStream receiver = playerDataInputStreamMap.get(player);

        try{
            return receiver.readUTF();
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

}
