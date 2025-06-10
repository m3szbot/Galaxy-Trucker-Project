package it.polimi.ingsw.Controller.AssemblyPhase;


import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class DeckInUseState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    private Integer index;

    /**
     * Constructor inherited from GameState.
     */
    public DeckInUseState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player, Integer index) {
        super(assemblyProtocol, playerMessenger, player);
        this.index = index;
    }

    @Override
    public void enter(AssemblyThread assemblyThread) {
        String message = "Write yes if you have finished to consult the deck";
        playerMessenger.printMessage(message);
    }

    public void handleInput(String input, AssemblyThread assemblyThread) {
        if (input.equalsIgnoreCase("yes")) {
            synchronized (assemblyProtocol.lockDecksList) {
                assemblyProtocol.getDeck(index - 1).setInUse(false);
            }
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        } else {
            String message = "Invalid Input";
            playerMessenger.printMessage(message);
            assemblyThread.setState(new DeckInUseState(assemblyProtocol, playerMessenger, player, index));
        }
    }


}
