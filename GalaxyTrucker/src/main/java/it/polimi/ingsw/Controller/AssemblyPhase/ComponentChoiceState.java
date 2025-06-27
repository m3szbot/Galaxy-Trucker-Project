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
        Component component;
        String imput = input.toLowerCase();
        int caseManagement = -1;
        try {
            caseManagement = Integer.parseInt(imput);
        } catch (NumberFormatException e) {
            playerMessenger.printMessage("Not valid input");
            assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
            return;
        }
        if (caseManagement >= 0 && caseManagement < assemblyProtocol.getUncoveredComponentsList().size()) {
            caseManagement = 1;
        } else {
            caseManagement = 0;
        }
        switch (caseManagement) {
            case 1:
                if (components.get(Integer.parseInt(input.toLowerCase())) == assemblyProtocol.getUncoveredComponentsList().get(Integer.parseInt(input.toLowerCase()))) {
                    try {
                        assemblyProtocol.chooseUncoveredComponent(player, Integer.parseInt(imput));
                    } catch (IllegalSelectionException e) {
                        playerMessenger.printMessage("Another unreachable place in the universe has been reached.");
                        break;
                    }
                    component = assemblyProtocol.getPlayersInHandComponents().get(player);
                    message = "New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack() + "Left:" + component.getLeft();
                } else {
                    message = "Component has been already taken";
                }
                playerMessenger.printMessage(message);
                break;
            case 2:
                message = "Error in component choice";
                playerMessenger.printMessage(message);
                break;


        }
        assemblyThread.setState(new AssemblyState(assemblyProtocol, playerMessenger, player));
    }

    @Override
    public void update(AssemblyThread assemblyThread) {
        super.update(assemblyThread);
    }

}
