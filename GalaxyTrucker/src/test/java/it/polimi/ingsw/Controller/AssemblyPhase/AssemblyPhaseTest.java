package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Model.ShipBoard.Player;
import it.polimi.ingsw.View.AssemblyView.AssemblyView;

public class AssemblyPhaseTest {
    AssemblyPhase ass;
    AssemblyView view;
    Player gecky;
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
        System.out.println(ass.getAssemblyProtocol().getInHandMap().get(gecky).getComponentName() + " "+ ass.getAssemblyProtocol().getInHandMap().get(gecky).getFront() + " " + ass.getAssemblyProtocol().getInHandMap().get(gecky).getRight() + " " + ass.getAssemblyProtocol().getInHandMap().get(gecky).getBack());

    }
*/
}
