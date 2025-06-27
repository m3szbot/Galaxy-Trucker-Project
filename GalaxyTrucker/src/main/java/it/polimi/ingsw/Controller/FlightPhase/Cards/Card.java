package it.polimi.ingsw.Controller.FlightPhase.Cards;

import it.polimi.ingsw.Model.GameInformation.GameInformation;

import java.io.Serializable;

/**
 * Abstract class that is the superclass of every card class.
 *
 * @author carlo
 */

public abstract class Card implements Serializable {

    protected int cardLevel;
    protected String cardName;
    protected String message;
    protected String imagePath;

    /**
     * Abstract method that must be defined for every card class. It is
     * the method which is responsible for resolving the card.
     *
     * @return nothing
     * @parameters gameInformation, from which all the data structures
     * needed to resolve the cards are taken.
     */

    public abstract void resolve(GameInformation gameInformation);

    public abstract void showCard();

    protected void printBlows(Blow[] blows) {

        for (int i = 0; i < blows.length; i++) {

            if (blows[i] != null) {
                System.out.println("Blow " + (i + 1) + " coming from the " + solveDirection(blows[i].getDirection()) + ".");
            }

        }
    }

    private String solveDirection(int direction) {

        if (direction == 0) {
            return "front";
        } else if (direction == 1) {
            return "right";
        } else if (direction == 2) {
            return "back";
        } else {
            return "left";
        }

    }

    protected void printGoods(int[] goods) {

        for (int i = 0; i < goods.length; i++) {
            System.out.println(solveGoodsColor(i) + " goods quantity: " + goods[i]);
        }
    }

    private String solveGoodsColor(int goodIndex) {
        if (goodIndex == 0) {
            return "red";
        } else if (goodIndex == 1) {
            return "yellow";
        } else if (goodIndex == 2) {
            return "green";
        } else {
            return "blue";
        }
    }

    public String getCardImage() {
        return imagePath;
    }

    public int getCardLevel() {
        return cardLevel;
    }

    public String getCardName() {
        return cardName;
    }
}

