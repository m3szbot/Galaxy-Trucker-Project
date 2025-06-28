package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * placeBookedComponentState allows the player to select one of their
 * booked components to place on the table.
 *
 * @author Giacomo
 */
public class PlaceBookedComponentState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player

    /**
     * Constructor inherited from GameState.
     */
    public PlaceBookedComponentState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }


    /**
     * Displays the prompt asking the player to choose a booked component.
     *
     * @param assemblyThread the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        // if there are booked components
        if (assemblyProtocol.getPlayersBookedComponents().get(player).size() > 0) {
            playerMessenger.printMessage("Booked components:");
            for (int i = 0; i < assemblyProtocol.getPlayersBookedComponents().get(player).size(); i++) {
                Component component = assemblyProtocol.getPlayersBookedComponents().get(player).get(i);
                String message = "Component " + i + ": Name: " + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft();
                playerMessenger.printMessage(message);
            }
        }
        // no booked components
        else {
            playerMessenger.printMessage("You don't have any booked component");
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        }
    }

    /**
     * Handles the input index provided by the player, retrieves the corresponding
     * booked component (if valid), places the current component in the uncovered list,
     * and moves the selected one to the active slot.
     *
     * @param input          the player's input (should be 1 or 2)
     * @param assemblyThread the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        try {
            int index = Integer.parseInt(input);
            if (index >= 0 && index < assemblyProtocol.getPlayersBookedComponents().get(player).size()) {
                if (assemblyProtocol.getPlayersBookedComponents().get(player).get(index) != null) {
                    try {
                        assemblyProtocol.chooseBookedComponent(player, index);
                    } catch (IllegalSelectionException e) {
                        playerMessenger.printMessage("For a moment, nothing happened. Then, after a second or so, nothing continued to happen.");
                        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                    }
                    playerMessenger.printComponent(assemblyProtocol.getPlayersInHandComponents().get(player));
                    assemblyThread.setState(new ComponentPlacingState(assemblyProtocol, playerMessenger, player, true));
                } else {
                    String message = "The chosen booked component doesn't exist";
                    playerMessenger.printMessage(message);
                    assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
                }
            }
            // selected index out of bounds
            else {
                String message = "Wrong input (enter a valid index)";
                playerMessenger.printMessage(message);
                assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            }
        }
        // input couldn't be parsed to int
        catch (NumberFormatException e) {
            String message = "Wrong input (enter a number)";
            playerMessenger.printMessage(message);
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
        }
    }
}
