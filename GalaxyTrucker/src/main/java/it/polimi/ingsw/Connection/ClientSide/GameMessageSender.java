package it.polimi.ingsw.Connection.ClientSide;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class GameMessageSender implements Runnable{

    private DataOutputStream out;

    public GameMessageSender(DataOutputStream out){

        this.out = out;

    }

    @Override
    public void run() {

        Scanner reader = new Scanner(System.in);

        while(true){

            try {

                out.writeUTF(reader.nextLine());

            } catch (IOException e) {

                System.err.println("Error while writing data to the server");

            }

        }

    }
}
