package it.polimi.ingsw.Connection.ServerSide.socket;

import it.polimi.ingsw.Model.ShipBoard.Player;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

public class ClientSocketMessenger {

    private static HashMap<Player, DataOutputStream> playerDataOutputStreamMap = new HashMap<>();
    private static HashMap<Player, DataInputStream> playerDataInputStreamMap = new HashMap<>();

    public void addPlayerToSocket(Player player, Socket socket){

        try {
            playerDataOutputStreamMap.put(player, new DataOutputStream(socket.getOutputStream()));
            playerDataInputStreamMap.put(player, new DataInputStream(socket.getInputStream()));
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
