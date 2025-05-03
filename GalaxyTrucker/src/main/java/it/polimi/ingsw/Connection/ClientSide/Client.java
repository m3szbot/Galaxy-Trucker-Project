package it.polimi.ingsw.Connection.ClientSide;

/**
 * Client class. The client lifecycle is composed of
 * three phases. The welcoming phase consists in asking
 * the client config information that are necessary for the
 * correct comunication with the client during the next phases.
 * The joining phase consists in making the client join a game.
 * And the gaming phase consist in the client playing.
 *
 * @author carlo
 */

public class Client {

    public static void main(){

        ClientWelcomer welcomer = new ClientWelcomer();
        ClientJoiner joiner = new ClientJoiner();
        ClientGameHandler gamehandler = new ClientGameHandler();
        ClientInfo clientInfo = new ClientInfo();

        welcomer.start(clientInfo);

        if(clientInfo.getGameCode() != -1){
            //the player wants to rejoin an interrupted game.
            //TODO

        }

        int resultCode = joiner.start(clientInfo);

        if(resultCode == 1){
            gamehandler.start();
        }
        else if(resultCode == 0){

        }
        else if(resultCode == -1){

        }



    }

}
