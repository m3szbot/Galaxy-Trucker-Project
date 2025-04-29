package it.polimi.ingsw.Connection.ClientSide;

import java.io.IOException;

public class Client {

    public static void main(){

        ClientWelcomer welcomer = new ClientWelcomer();
        ClientJoiner joiner = new ClientJoiner();
        ClientGameHandler gamehandler = new ClientGameHandler();
        ClientInfo clientInfo;

        welcomer.start();

        clientInfo = welcomer.getClientInfo();

        try {

            joiner.start(clientInfo);

        }catch (IOException e){
            //TODO
        }




    }



}
