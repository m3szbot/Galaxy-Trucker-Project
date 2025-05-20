package it.polimi.ingsw.Connection;

import java.io.Serializable;

/**
 * The connection type can be chosen by the player when
 * he joins the game
 *
 * @author carlo
 */

public enum ConnectionType implements Serializable {
    RMI, SOCKET;
}
