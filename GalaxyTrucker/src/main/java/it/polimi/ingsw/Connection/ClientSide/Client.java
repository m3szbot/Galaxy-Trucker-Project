package it.polimi.ingsw.Connection.ClientSide;

import it.polimi.ingsw.Application.ConnectionType;
import it.polimi.ingsw.Application.Game;
import it.polimi.ingsw.Application.ViewType;

import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

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
