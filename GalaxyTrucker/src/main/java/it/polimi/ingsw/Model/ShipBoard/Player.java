package it.polimi.ingsw.Model.ShipBoard;

import it.polimi.ingsw.Model.GameInformation.GameInformation;

import java.io.Serializable;

public class Player implements Serializable {
    // The nickname of the player
    private String nickName;
    // The color associated with the player
    private Color color;
    // The player's ship board, representing their ship in the game
    private ShipBoard shipBoard;

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
        this.shipBoard = new ShipBoard(gameInformation.getGameType());
        this.color = color;
    }

    /**
     * Returns the player's nickname.
     *
     * @return The player's nickname.
     * @author Giacomo
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * Returns the player's color.
     *
     * @return The color associated with the player.
     * @author Giacomo
     */
    public Color getColor() {
        return color;
    }

    /**
     * @return shipBoard
     * @author Boti
     */
    public ShipBoard getShipBoard() {
        return shipBoard;
    }

}



