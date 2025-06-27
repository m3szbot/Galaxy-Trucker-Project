package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Controller.Game.Game;
import it.polimi.ingsw.Mocker;
import it.polimi.ingsw.Model.ShipBoard.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class AssemblyPhaseTest {
    /*
        @BeforeEach
        public void  createGameInformation(){

            GameInformation gameInformation = new GameInformation();
            gameInformation.setGameType(GameType.NormalGame);
            gameInformation.setUpPlayers(gecky = new Player("Gecky", Color.RED, gameInformation), 1);
            try {gameInformation.setUpCards(GameType.NormalGame);} catch (Exception e){e.printStackTrace();}
            try  {gameInformation.setUpComponents();} catch (Exception e){e.printStackTrace();}
            ass = new AssemblyPhase();
            view = new AssemblyView();
        }

        @Test
        public void start1(){
            ComponentPlacingState state = new ComponentPlacingState(ass.getAssemblyView(), ass.getAssemblyProtocol(), gecky);
            ComponentChoiceState state1;
           // ass.start();
            ass.setState(state);
            state.enter(ass, ass.getAssemblyView());
            ass.getAssemblyProtocol().newComponent(gecky);
            ass.getAssemblyProtocol().newComponent(gecky);
            ass.getAssemblyProtocol().newComponent(gecky);
            state.handleInput("7 7", ass);
            assertTrue(gecky.getShipBoard().getComponent(6, 6) != null);
            System.out.println("Boti Boti Boti");
            state1 = new ComponentChoiceState(ass.getAssemblyView(),ass.getAssemblyProtocol(), gecky);
            ass.setState(state1);
            state1.enter(ass, ass.getAssemblyView());
            System.out.println(ass.getAssemblyProtocol().getPlayersInHandComponents().get(gecky).getComponentName() + " "+ ass.getAssemblyProtocol().getPlayersInHandComponents().get(gecky).getFront() + " " + ass.getAssemblyProtocol().getPlayersInHandComponents().get(gecky).getRight() + " " + ass.getAssemblyProtocol().getPlayersInHandComponents().get(gecky).getBack());

        }
    */
    Game game;
    AssemblyPhase assemblyPhase;
    Player player;


    @BeforeEach
    void setup() {
        Mocker.mockNormalGame1Player();
        game = Mocker.getGame();
        assemblyPhase = game.getAssemblyPhase();
        player = Mocker.getFirstPlayer();
    }

    @Test
    void drawPlaceEnd() {
        Mocker.simulateClientInput("draw\nplace\n7 8\nend\n1\n");

        assemblyPhase.start();
    }


    @Test
    public void bookComponent() {
        assemblyPhase.start();
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("draw");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersBookedComponents().get(player));
        Mocker.simulateClientInput("book");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersBookedComponents().get(player));
        Mocker.simulateClientInput("place booked");
        Mocker.simulateClientInput("0");
        Mocker.simulateClientInput("67 67");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void testComponentPlacing() {
        assemblyPhase.start();
        assertNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("draw");
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("45 67");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("6 6");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
        Mocker.simulateClientInput("place");
        Mocker.simulateClientInput("7 6");
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(player.getShipBoard().getComponent(7, 6));
        try {
            wait(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        assertNotNull(assemblyPhase.getAssemblyProtocol().getPlayersInHandComponents().get(player));
    }


}
