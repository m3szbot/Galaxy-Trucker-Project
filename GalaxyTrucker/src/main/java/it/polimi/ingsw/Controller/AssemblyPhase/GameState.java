package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public abstract class GameState {
    protected final AssemblyProtocol assemblyProtocol;
    protected final PlayerMessenger playerMessenger;
    protected final Player player;

    public GameState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        this.assemblyProtocol = assemblyProtocol;
        this.playerMessenger = playerMessenger;
        this.player = player;
    }


    abstract void enter(AssemblyThread assemblyThread);

    abstract void handleInput(String input, AssemblyThread assemblyThread);

    /**
     * Default empty update method.
     * Overridden by some child classes.
     */
    void update(AssemblyThread assemblyThread) {
    }
}