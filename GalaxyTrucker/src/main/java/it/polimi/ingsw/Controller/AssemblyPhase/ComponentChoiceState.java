package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.messengers.PlayerMessenger;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.IllegalSelectionException;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ComponentChoice handles the user input when a player chooses
 * a component from the uncovered list.
 *
 * @author Giacomo
 */
public class ComponentChoiceState extends GameState {
    // inherited attributes: assemblyProtocol, playerMessenger, player
    private String message;
    private List<Component> components;

    /**
     * Constructor inherited from GameState.
     */
    public ComponentChoiceState(AssemblyProtocol assemblyProtocol, PlayerMessenger playerMessenger, Player player) {
        super(assemblyProtocol, playerMessenger, player);
    }

    /**
     * Displays the message prompting the player to choose a component.
     *
     * @param assemblyThread the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyThread) {
        components = new ArrayList<>(assemblyProtocol.getUncoveredComponentsList());
        int i = 0;
        for (Component component : components) {
            message = i + ": " + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack() + " Left: " + component.getLeft();
            playerMessenger.printMessage(message);
            i++;
        }
        message = "Enter the number of the component you would like:";
        playerMessenger.printMessage(message);
    }

    /**
     * Handles the input used to choose a component from the uncovered list.
     * If the choice is valid, the component is assigned to the player.
     *
     * @param input          the index of the component
     * @param assemblyThread the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyThread) {
        input = input.toLowerCase();
        int index;

        try {
            index = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            playerMessenger.printMessage("\nNot valid input");
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            return;
        }

        if (index >= 0 && index < assemblyProtocol.getUncoveredComponentsList().size() && index < components.size()) {
            if (components.get(index).equals(assemblyProtocol.getUncoveredComponentsList().get(index))) {
                try {
                    assemblyProtocol.chooseUncoveredComponent(player, index);
                } catch (IllegalSelectionException e) {
                    playerMessenger.printMessage("\nAnother unreachable place in the universe has been reached.");
                }
            } else {
                message = "\nComponent has been already taken";
                playerMessenger.printMessage(message);
            }
        }else{
            message = "\nComponent index out of range";
            playerMessenger.printMessage(message);
        }
        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
    }

    @Override
    public void update(AssemblyThread assemblyThread) {
        super.update(assemblyThread);
    }

}
