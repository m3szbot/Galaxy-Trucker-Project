package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;
import it.polimi.ingsw.View.TUI.TUIView;

import java.io.Serializable;

/**
 * The class representing a player in the game.
 */
public class Player implements Serializable {
    // The color associated with the player
    private final Color color;
    // The player's ship board, representing their ship in the game
    private final ShipBoard shipBoard;
    // The nickname of the player
    private String nickName;
    private boolean connected;

    /**
     * Constructor for the Player class.
     * Initializes a new player with a nickname, a new ship board, and a color.
     *
     * @param nickName The nickname of the player.
     * @param color    The color representing the player.
     * @author Giacomo
     */
    public Player(String nickName, Color color, GameInformation gameInformation) {
        this.nickName = nickName;
        this.shipBoard = new ShipBoard(gameInformation.getGameType(), color);
        this.color = color;
        this.connected = true;
    }

    /**
     * Set the connection status to false of the player.
     */
    public void disconnect() {
        connected = false;
    }

    /**
     * @return the connection status of the player.
     */
    public boolean getIsConnected() {
        return connected;
    }

    /**
     * Used by the server (nickname is an identifier, without the color code - assigned only for the game).
     * Uncoloured nickname is the id of the client.
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Can be used only in print statements (TUI)!
     * (Ruins the nickname id of the client saved in the server)
     *
     * @return The player's nickname, colored (with strings).
     * @author Boti
     */
    public String getColouredNickName() {
        // color the nickname
        return getColorCode(color) + nickName + TUIView.RESET;
    }

    /**
     * Return the ascii color coder string of the given player color, or RESET (default color)
     * if color couldn't be translated.
     *
     * @param color
     * @return
     */
    private String getColorCode(Color color) {
        if (color == null)
            return TUIView.RESET;

        switch (color) {
            case Color.RED -> {
                return TUIView.RED;
            }
            case BLUE -> {
                return TUIView.BLUE;
            }
            case GREEN -> {
                return TUIView.GREEN;
            }
            case YELLOW -> {
                return TUIView.YELLOW;
            }
        }
        return TUIView.RESET;
    }

    /**
     * @return The color associated with the player.
     * @author Giacomo
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return the player's shipboard.
     * @author Boti
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

}



