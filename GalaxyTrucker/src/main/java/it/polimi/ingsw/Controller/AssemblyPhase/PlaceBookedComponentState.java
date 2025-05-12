package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * placeBookedComponentState allows the player to select one of their
 * booked components to place on the table.
 *
 * @author Giacomo
 */
public class PlaceBookedComponentState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;

    /**
     * Constructs a new state for allowing the player to place one of their
     * previously booked components.
     *
     * @param protocol the logic handler that manages components and booking
     * @param player   the player currently placing a booked component
     */
    public PlaceBookedComponentState(AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    /**
     * Displays the prompt asking the player to choose a booked component.
     *
     * @param assemblyPhase the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        for (int i = 0; i < assemblyProtocol.getBookedMap().get(player).size(); i++) {
            Component component = assemblyProtocol.getBookedMap().get(player).get(i);
            String message = "Component " + i + ": Name:" + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft();
            DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
        }
    }

    /**
     * Handles the input index provided by the player, retrieves the corresponding
     * booked component (if valid), places the current component in the uncovered list,
     * and moves the selected one to the active slot.
     *
     * @param input         the player's input (should be 1 or 2)
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        int index = Integer.parseInt(input);
        index = index - 1;
        if (index == 0 || index == 1) {
            synchronized (assemblyProtocol.lockUncoveredList) {
                assemblyPhase.getAssemblyProtocol().chooseBookedComponent(player, index);
            }
            if (index == 0 || index == 1) {
                assemblyPhase.getAssemblyProtocol().chooseBookedComponent(player, index);
                assemblyPhase.setState(new ComponentPlacingState(assemblyProtocol, player));
            } else {
                String message = "The Booked Component chose doesn't exist";
                DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                assemblyPhase.setState(new AssemblyState(assemblyProtocol, player));
            }
        }

    }
}
