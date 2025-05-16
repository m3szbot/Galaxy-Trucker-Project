package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * ComponentPlacingState handles the logic for placing a component
 * onto the player's ship board based on user-provided coordinates.
 * After placing, a new component is drawn and the game returns to AssemblyState.
 *
 * @author Giacomo
 */
public class ComponentPlacingState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;

    /**
     * Constructs a ComponentPlacingState for the current player.
     *
     * @param protocol the game logic handler
     * @param player   the current player placing the component
     */
    public ComponentPlacingState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    /**
     * Called when this state becomes active. Displays a prompt to the player
     * asking where to place the component.
     *
     * @param assemblyPhase the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        String message = "Where do you want to place the component? Indicate coordinates Cols and Rows";
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
    }

    /**
     * Parses the player's input for placement coordinates, adds the current component
     * to the ship board at the specified position, draws a new component, and returns
     * to the AssemblyState.
     *
     * @param input         the coordinates as a string (e.g., "3 4" or "3,4")
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        String[] parts = input.split("[ ,]");
        int num1 = Integer.parseInt(parts[0]);
        int num2 = Integer.parseInt(parts[1]);


        if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
            if (assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getComponent(num1 - 1, num2) != null || assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getComponent(num1 + 1, num2) != null || assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getComponent(num1, num2 - 1) != null || assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().getComponent(num1, num2 + 1) != null) {
                try {
                    assemblyPhase.getGameInformation().getPlayerList().get(assemblyPhase.getGameInformation().getPlayerList().indexOf(player)).getShipBoard().addComponent(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player), num1, num2);
                    synchronized (assemblyProtocol.lockCoveredList) {
                        assemblyPhase.getAssemblyProtocol().newComponent(player);
                    }
                } catch (NotPermittedPlacementException e) {
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, "Your are not allowed to place your component here");
                }
                assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
            } else{
                // di al giocatore che il componente non è adiacente e a nulla.
            }
        }else{
            String message = "Your hand is empty";
            assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
            DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
        }
    }
}
