package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Assembly.AssemblyProtocol;
import it.polimi.ingsw.Shipboard.Player;

public class ShowDeckState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private AssemblyView view;

    public ShowDeckState(AssemblyView view, AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.view = view;
        this.player = player;
    }

    @Override
    public void enter(AssemblyGame assemblyGame, AssemblyView assemblyView) {
        assemblyView.printChooseDeckMessage();
    }

    @Override
    public void handleInput(String input, AssemblyGame assemblyGame) {
        int index = Integer.parseInt(input);
        if (index >= 0 && index <4){
            assemblyGame.getAssemblyProtocol().showDeck(index);
        }
        else{
            view.printNotValidDeckNumberMessage();
        }
        assemblyGame.setState(new AssemblyState(view, assemblyProtocol, player));
    }
}
