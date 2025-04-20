package it.polimi.ingsw.Assembly;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Cards.Card;
import it.polimi.ingsw.Cards.CardBuilder;
import it.polimi.ingsw.Cards.Epidemic;
import it.polimi.ingsw.Cards.Sabotage;
import it.polimi.ingsw.Shipboard.Color;
import it.polimi.ingsw.Shipboard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AssemblyProtocolTest {
    AssemblyProtocol assemblyProtocol;
    Player playerA, playerB, playerC, playerD;

    @BeforeEach
    void setUp() {
        GameInformation gameInformation = new GameInformation();
        gameInformation.setGameType(GameType.NormalGame);
        // players
        playerA = new Player("A", Color.BLUE, gameInformation);
        playerB = new Player("B", Color.RED, gameInformation);
        playerC = new Player("C", Color.YELLOW, gameInformation);
        playerD = new Player("D", Color.GREEN, gameInformation);
        try {
            gameInformation.setUpCards(GameType.NormalGame);
            gameInformation.setUpComponents();

        } catch (IOException e) {
        }
        gameInformation.setUpPlayers(playerA, 4);
        gameInformation.addPlayers(playerB);
        gameInformation.addPlayers(playerC);
        gameInformation.addPlayers(playerD);

        assemblyProtocol = new AssemblyProtocol(gameInformation);
    }

    @Test
    void checkSetup() {
        assertEquals(0, assemblyProtocol.getUncoveredList().size());
    }

    @Test
    void oneNewComponent() {
        assemblyProtocol.newComponent(playerA);
    }

}