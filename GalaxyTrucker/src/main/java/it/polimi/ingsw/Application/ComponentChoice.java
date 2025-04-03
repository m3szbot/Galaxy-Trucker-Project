package it.polimi.ingsw.Application;
import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.*;

import javax.swing.text.View;
import java.util.ArrayList;

public class ComponentChoice implements GameState{
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;


    public ComponentChoice(AssemblyView view, AssemblyProtocol protocol) {
        this.assemblyProtocol = protocol;
        this.view = view;
    }

    @Override
    public void enter(Game game, AssemblyView view) {
        view.printComponentChoice();
    }

    @Override
    public void handleInput(String input, Game game) {
        String imput = input.toLowerCase();
        int caseManagement = -1;
        try {caseManagement = Integer.parseInt(input);}
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        if(caseManagement >= 0 && caseManagement < game.getGameInformation().getAssemblyProtocol().getUncoveredList().size()){
            caseManagement = 1;
        }
        else{
            caseManagement = 0;
        }
        switch (caseManagement) {
            case 1:
                gameInformation.assemblyProtocol.chooseComponent(player, Integer.parseInt(input));
                break;
            case 2:
                System.out.println("Error in component choice");
                break;
        }
        game.setState(new AssemblyState());
    }

}
