package it.polimi.ingsw.Controller.AssemblyPhase;


import it.polimi.ingsw.Connection.ServerSide.messengers.ClientMessenger;
import it.polimi.ingsw.Controller.Cards.Card;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.AssemblyModel.Deck;
import it.polimi.ingsw.Model.ShipBoard.Player;

public class DeckInUseState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private Integer index;

    public DeckInUseState(AssemblyProtocol protocol, Player player, Integer index) {
        this.assemblyProtocol = protocol;
        this.player = player;
        this.index = index;
    }

    @Override
    public void enter(AssemblyThread assemblyPhase) {
        String message = "Write yes if you have finished to consult the deck";
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
    }

    public void handleInput(String input, AssemblyThread assemblyPhase) {
        if(input.toLowerCase().equals("yes")){
            synchronized (assemblyProtocol.lockDecksList) {
                assemblyProtocol.getDeck(index-1).setInUse(false);
            }
            assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
        }
        else{
            String message = "Invalid Input";
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerMessenger(player).printMessage(message);
            assemblyPhase.setState(new DeckInUseState(assemblyProtocol, player, index));
        }
    }


}
