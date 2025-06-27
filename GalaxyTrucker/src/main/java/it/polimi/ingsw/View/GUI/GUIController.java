package it.polimi.ingsw.View.GUI;

import javafx.scene.Node;

/**
 * Abstract class which define the signature of all the methods that must
 * be implemented by the controllers of the various phases. Note that
 * some methods might not be implemented by the specific controller as it
 * will never be used. For example refresh card in the lobby controller.
 *
 * @author carlo
 */

public abstract class GUIController {

    /**
     * Refresh the console present into the current scene by appending the message
     * passed as parameter
     * @param message
     */

    public abstract void refreshConsole(String message);

    /**
     * Refresh the shipboard present into the current scene by replacing it with
     * the one passed as parameter
     * @param node
     */

    public abstract void refreshShipBoard(Node node);

    /**
     * Refresh the card present into the current scene by replacing it with
     * the one passed as parameter
     * @param node
     */

    public abstract void refreshCard(Node node);

    /**
     * Refresh the component present into the current scene by replacing it with
     * the one passed as parameter
     * @param node
     */

    public abstract void refreshComponent(Node node);

    /**
     * Refresh the flightboard present into the current scene by replacing it with
     * the one passed as parameter
     * @param node
     */

    public abstract void refreshFlightBoard(Node node);

    public abstract String getBackgroundImage();

}
