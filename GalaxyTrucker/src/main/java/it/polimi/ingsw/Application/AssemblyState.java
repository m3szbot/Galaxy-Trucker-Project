package it.polimi.ingsw.Application;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;


public class AssemblyState implements GameState{
    private long startTime;
    private boolean actionTaken = false;
    private AssemblyView assembly;
    private AssemblyProtocol protocol;
    private Player player;

    public AssemblyState(AssemblyView assembly, AssemblyProtocol protocol, Player player) {
        this.assembly = assembly;
        this.protocol = protocol;
        this.player = player;
    }


    @Override
    public void enter(Game game, AssemblyView view) {
        startTime = System.currentTimeMillis();
        actionTaken = false;
        assembly = view;
    }

    @Override
    public void handleInput(String input, Game game) {
        if (actionTaken) return; // Ignora input dopo che è stata presa una decisione
        assembly.printAssemblyMessage();
        switch (input.toLowerCase()) {
            case "Place":
                System.out.println("Dove vuoi posizionare il componente (X Y):");
                actionTaken = true;
                game.setState(new AssemblyState(assembly, protocol,player));
                break;
            case "pesca":
                System.out.println("New Component:");
                actionTaken = true;
                game.getGameInformation().getAssemblyProtocol().newComponent(player);
                game.setState(new AssemblyState(assembly, protocol, player));
                break;
            case "Choose":
                actionTaken = true;
                game.setState(new ComponentChoice(assembly, protocol));
                break;
            case "Rotate":
                System.out.println("Componente ruotato.");
                game.setState(new AssemblyState(assembly, protocol, player));
                break;
            case "gira clessidra":
                //
                break;
            default:
                System.out.println("Comando non valido.");
                game.setState(new AssemblyState(assembly, protocol, player));
        }
    }

    public void update(Game game) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 5000) { // 5 secondi
                System.out.println("❗ Time out");
                actionTaken = true;
                game.setState(new AssemblyState(assembly, protocol, player));
            }
        }
    }
}
