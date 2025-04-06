package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

public class ComponentPlacingState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;

    public ComponentPlacingState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    @Override
    public void enter(AssemblyGame assemblyGame, AssemblyView view) {
        view.printComponentPlacingMessage();
    }

    @Override
    public void handleInput(String input, AssemblyGame assemblyGame){
        String[] parts = input.split("[ ,]");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[1]);

        assemblyGame.getGameInformation().getPlayerList().get(assemblyGame.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().addComponent(assemblyGame.getAssemblyProtocol().getViewMap().get(player), num1, num2);
        assemblyGame.getAssemblyProtocol().newComponent(player);
        assemblyGame.setState(new AssemblyState(view, assemblyProtocol, player));
    }
}
