package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ServerSide.ClientMessenger;
import it.polimi.ingsw.Connection.ServerSide.DataContainer;
import it.polimi.ingsw.Model.AssemblyModel.AssemblyProtocol;
import it.polimi.ingsw.Model.ShipBoard.Player;

/**
 * AssemblyState handles the player's turn during the assembly phase,
 * managing their input and timing.
 *
 * @author Giacomo
 */
public class AssemblyState implements GameState {
    private long startTime;
    private boolean actionTaken = false;
    private AssemblyProtocol protocol;
    private Player player;
    DataContainer dataContainer;
    /**
     * Constructs a new AssemblyState for the given player.
     */
    public AssemblyState(AssemblyProtocol protocol, Player player) {
        this.protocol = protocol;
        this.player = player;
    }

    /**
     * Called when this state becomes active. Initializes the timer and resets the action flag.
     */
    @Override
    public void enter(AssemblyThread assemblyPhase) {
        startTime = System.currentTimeMillis();
        actionTaken = false;
    }

    /**
     * Handles user input commands during the assembly phase.
     */
    @Override
    public void handleInput(String input, AssemblyThread assemblyPhase) {

        if (actionTaken) return; // Ignore input after an action is taken
        DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setShipBoard(player.getShipBoard());
        dataContainer.setCommand("printShipboard");
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
        dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
        dataContainer.setComponent(assemblyPhase.getAssemblyProtocol().getInHandMap().get(player));
        dataContainer.setCommand("printComponent");
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
        String message = "👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component) / end (finish your assembling phase)";
        //view.sendMessageToPlayer(message, player);
        ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerMessage(player, message);
        switch (input.toLowerCase()) {
            case "place":
                actionTaken = true;
                assemblyPhase.setState(new ComponentPlacingState(protocol, player));
                break;
            case "draw":
                actionTaken = true;
                synchronized (assemblyPhase.getAssemblyProtocol().lockCoveredList) {
                    assemblyPhase.getAssemblyProtocol().newComponent(player);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "choose":
                actionTaken = true;
                assemblyPhase.setState(new ComponentChoiceState(protocol, player));
                break;
            case "rotate":
                if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).rotate();
                    message = "Component rotated:" + assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).getComponentName() + "Front:" + assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).getFront() + "Right:" + assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).getRight() + "Back:" + assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).getBack() + "Left:" + assemblyPhase.getAssemblyProtocol().getInHandMap().get(player).getLeft();
                    DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                } else {
                    message = "Your hand is empty";
                    dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "turn":
                actionTaken = true;
                message = "Turn the hourglass";
                dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                assemblyPhase.getAssemblyProtocol().getHourGlass().twist(assemblyPhase.getAssemblyProtocol(),ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerSocketMap().keySet().stream().toList() );
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "show":
                actionTaken = true;
                assemblyPhase.setState(new ShowDeckState(protocol, player));
                break;
            case "book":
                actionTaken = true;
                if (assemblyPhase.getAssemblyProtocol().getInHandMap().get(player) != null) {
                    assemblyPhase.getAssemblyProtocol().bookComponent(player);
                } else {
                    message = "Your hand is empty";
                    dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                    dataContainer.setMessage(message);
                    dataContainer.setCommand("printMessage");
                    ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                }
                assemblyPhase.setState(new AssemblyState(protocol, player));
                break;
            case "place booked":
                actionTaken = true;
                assemblyPhase.setState(new PlaceBookedComponentState(protocol, player));
                break;
            case "end":
                actionTaken = true;
                assemblyPhase.setState(new ChooseStartingPositionState(protocol, player));
            default:
                message = "Invalid command";
                dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);
                assemblyPhase.setState(new AssemblyState(protocol, player));
        }


    }

    /**
     * Periodically called to check if the player has timed out.
     */
    public void update(AssemblyPhase assemblyPhase) {
        if (!actionTaken) {
            long now = System.currentTimeMillis();
            if (now - startTime >= 50000) {
                String message = "👾AssemblyPhase (place (current component) / draw (a new component) / Choose (a component) / Rotate (current component) / turn (the hourglass) / book (current component and have a new one) / place booked (component) / end (finish your assembling phase)"; // 50 seconds timeout
                DataContainer dataContainer = ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).getPlayerContainer(player);
                dataContainer.setMessage(message);
                dataContainer.setCommand("printMessage");
                ClientMessenger.getGameMessenger(assemblyPhase.getAssemblyProtocol().getGameCode()).sendPlayerData(player);

                actionTaken = true;
                assemblyPhase.setState(new AssemblyState(protocol, player));
            }
        }
    }
}
