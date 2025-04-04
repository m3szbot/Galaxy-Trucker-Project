package it.polimi.ingsw.Application;
import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.*;

public class ComponentChoice implements GameState{
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;


    public ComponentChoice(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    @Override
    public void enter(AssemblyGame assemblyGame, AssemblyView view) {
        view.printComponentChoice();
    }

    @Override
    public void handleInput(String input, AssemblyGame assemblyGame) {
        String imput = input.toLowerCase();
        int caseManagement = -1;
        try {caseManagement = Integer.parseInt(input);}
        catch(NumberFormatException e) {
            e.printStackTrace();
        }
        if(caseManagement >= 0 && caseManagement < assemblyGame.getGameInformation().getAssemblyProtocol().getUncoveredList().size()){
            caseManagement = 1;
        }
        else{
            caseManagement = 0;
        }
        switch (caseManagement) {
            case 1:
                view.printUncoveredComponentsMessage(assemblyGame.getGameInformation());
                assemblyGame.getGameInformation().getAssemblyProtocol().chooseComponent(player, Integer.parseInt(input));
                view.printNewComponentMessage(assemblyGame.getGameInformation().getAssemblyProtocol().getViewMap().get(player));
                break;
            case 2:
                view.printErrorComponentChoiceMessage();
                break;
        }
        assemblyGame.setState(new AssemblyState(view, assemblyProtocol, player));
    }

}
