package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Application.ViewType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class ClientJoiner {

    public void start(ClientInfo clientInfo) throws IOException, UnknownHostException{

       Socket socket = new Socket(clientInfo.getServerIp(), clientInfo.getServerPort());
       DataInputStream dataReceiver = new DataInputStream(socket.getInputStream());
       DataOutputStream dataSender = new DataOutputStream(socket.getOutputStream());
        ObjectOutputStream objectSender = new ObjectOutputStream(socket.getOutputStream());
       String message;
       Scanner scanner = new Scanner(System.in);

       if(clientInfo.getViewType() == ViewType.CLI){

           objectSender.writeObject(clientInfo);

           while(true) {

               message = dataReceiver.readUTF();

               if(message.equals("You have been added to the game!")){
                   //thread terminates when the client is added to the game
                   break;
               }

               System.out.print(message);

               dataSender.writeUTF(scanner.nextLine());

           }
       }
       else{
           //GUI
       }


    }
}
