package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClientSocketMessenger {

    private static Map<Player, DataOutputStream> playerDataOutputStreamMap = new ConcurrentHashMap<>();
    private static Map<Player, DataInputStream> playerDataInputStreamMap = new ConcurrentHashMap<>();

    public void addPlayerToSocket(Player player, Socket socket){

        try {
            playerDataOutputStreamMap.put(player, new DataOutputStream(socket.getOutputStream()));
            playerDataInputStreamMap.put(player, new DataInputStream(socket.getInputStream()));
        } catch (IOException e) {
            throw new RuntimeException(e); //prob qui va modificato con un altro tipo di errore che permetta di gestire la mancata ricezione
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
