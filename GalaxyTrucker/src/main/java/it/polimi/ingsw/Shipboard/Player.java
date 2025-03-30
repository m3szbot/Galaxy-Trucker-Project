
package it.polimi.ingsw.Shipboard;

public class Player {
    // The nickname of the player
    private String nickName;

    // The player's ship board, representing their ship in the game
    public ShipBoard shipStructure;

    // The color associated with the player
    Color color;

    /**
     * Constructor for the Player class.
     * Initializes a new player with a nickname, a new ship board, and a color.
     *
     * @param nickName The nickname of the player.
     * @param color    The color representing the player.
     * @author Giacomo
     */
    public Player(String nickName, Color color) {
        this.nickName = nickName;
        this.shipStructure = new ShipBoard(gameInformation.gameType);
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
}



