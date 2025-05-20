package it.polimi.ingsw.Connection.ServerSide;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.Model.GameInformation.GameType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameMessengerTest {
    GameInformation gameInformation;
    ClientMessenger clientMessenger;
    GameMessenger gameMessenger;

    @BeforeEach
    void setup() {
        // set up gameInformation
        gameInformation = new GameInformation();
        gameInformation.setUpGameInformation(GameType.NORMALGAME, 4);

        // set up GameMessenger
        ClientMessenger.addGame(gameInformation.getGameCode());
        gameMessenger = ClientMessenger.getGameMessenger(gameInformation.getGameCode());

    }

    @Test
    void sendMessage() {

    }

}