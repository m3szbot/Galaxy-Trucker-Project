package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;


public class AssemblyState implements GameState {
    private long startTime;
    private boolean actionTaken = false;
    private AssemblyView view;
    private AssemblyProtocol protocol;
    private Player player;

    public AssemblyState(AssemblyView assembly, AssemblyProtocol protocol, Player player) {
        this.view = assembly;
        this.protocol = protocol;
        this.player = player;
    }


    @Override
    public void enter(AssemblyGame assemblyGame, AssemblyView view) {
        startTime = System.currentTimeMillis();
        actionTaken = false;
        view = view;
    }

    @Override
    public void handleInput(String input, AssemblyGame assemblyGame) {
        if (actionTaken) return; // Ignora input dopo che è stata presa una decisione
        view.printAssemblyMessage();
        switch (input.toLowerCase()) {
            case "place":
                actionTaken = true;
                assemblyGame.setState(new AssemblyState(view, protocol,player));
                break;
            case "draw":
                actionTaken = true;
                assemblyGame.getAssemblyProtocol().newComponent(player);
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "choose":
                actionTaken = true;
                assemblyGame.setState(new ComponentChoice(view, protocol, player));
                break;
            case "rotate":
                assemblyGame.getAssemblyProtocol().getViewMap().get(player).rotate();
                view.printRotateMessage(assemblyGame.getAssemblyProtocol().getViewMap().get(player));
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "turn":
                actionTaken = true;
                view.printTurnMessage();
                assemblyGame.getAssemblyProtocol().getHourGlass().twist();
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            case "show":
                actionTaken = true;
                assemblyGame.setState(new ShowDeckState(view, protocol, player));
                break;
            case "book":
                actionTaken = true;
                assemblyGame.getAssemblyProtocol().bookComponent(player);
                assemblyGame.setState(new AssemblyState(view, protocol, player));
                break;
            default:
                view.printErrorInCommandMessage();
                assemblyGame.setState(new AssemblyState(view, protocol, player));
        }
    }

    public void update(AssemblyGame assemblyGame) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) { // 5 secondi
                view.printAssemblyMessage();
                actionTaken = true;
                assemblyGame.setState(new AssemblyState(view, protocol, player));
            }
        }
    }
}
