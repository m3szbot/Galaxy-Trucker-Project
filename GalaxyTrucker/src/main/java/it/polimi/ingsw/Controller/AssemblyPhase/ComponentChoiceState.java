package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.Components.Component;
import it.polimi.ingsw.Model.ShipBoard.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * ComponentChoice handles the user input when a player chooses
 * a component from the uncovered list.
 *
 * @author Giacomo
 */
public class ComponentChoiceState implements GameState {
    private AssemblyProtocol assemblyProtocol;
    private Player player;
    private String message;
    private List<Component> components;

    /**
     * Constructs a ComponentChoice state.
     *
     * @param protocol the game logic handler
     * @param player   the current player
     */
    public ComponentChoiceState( AssemblyProtocol protocol, Player player) {
        this.assemblyProtocol = protocol;
        this.player = player;
    }

    @Override
    public void update(AssemblyThread assemblyPhase) {
        GameState.super.update(assemblyPhase);
    }

    /**
     * Displays the message prompting the player to choose a component.
     *
     * @param assemblyPhase the current game instance
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        components = new ArrayList<>(assemblyPhase.getAssemblyProtocol().getUncoveredList());
        int i = 0;
        for(Component component : components) {
            message = i + ": " + component.getComponentName() + " Front: " + component.getFront() + " Right: " + component.getRight() + " Back: " + component.getBack()  + " Left: " + component.getLeft();
            DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
            dataContainer.setMessage(message);
            dataContainer.setCommand("printMessage");
            ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
            i++;
        }
        message = "Enter the number of the component you would like:";
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setMessage(message);
        dataContainer.setCommand("printMessage");
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
    }

    /**
     * Handles the input used to choose a component from the uncovered list.
     * If the choice is valid, the component is assigned to the player.
     *
     * @param input         the index of the component
     * @param assemblyPhase the current game instance
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {
        Component component;
        String imput = input.toLowerCase();
        int caseManagement = -1;
        try {
            caseManagement = Integer.parseInt(imput);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (caseManagement >= 0 && caseManagement < assemblyPhase.getAssemblyProtocol().getUncoveredList().size()) {
            caseManagement = 1;
        } else {
            caseManagement = 0;
        }
        switch (caseManagement) {
            case 1:
                synchronized (assemblyProtocol.lockUncoveredList) {
                    if(components.get(Integer.parseInt(input.toLowerCase())) == assemblyPhase.getAssemblyProtocol().getUncoveredList().get(Integer.parseInt(input.toLowerCase()))) {
                        assemblyPhase.getAssemblyProtocol().chooseUncoveredComponent(player, Integer.parseInt(imput));
                        component = assemblyPhase.getAssemblyProtocol().getInHandMap().get(player);
                        message ="New component:" + component.getComponentName() + "Front:" + component.getFront() + "Right:" + component.getRight() + "Back:" + component.getBack()  + "Left:" + component.getLeft();
                    }
                    else {
                        message = "Component has been already taken";
                    }
                }
                DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                break;
            case 2:
                message = "Error in component choice";
                dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                break;


        }
        assemblyPhase.setState(new AssemblyState( assemblyProtocol, player));
    }

}
