package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.messengers.GameMessenger;
import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import it.polimi.ingsw.Model.ShipBoard.Color;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Class to test sending data with Data Containers
 */

class GameMessengerTest {
    GameInformation gameInformation;
    GameMessenger gameMessenger;

    @BeforeEach
    void setup() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        // set up GameMessenger
        gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());

        // add player
        Player playerA;
        playerA = new Player("A", Color.BLUE, gameInformation);
        gameInformation.addPlayer(playerA);
    }

    @Test
    void sendMessageToAll() {
        gameMessenger.sendMessageToAll("Hello world");
    }

    @Test
    void sendMessageDC() {
    }

}