package it.polimi.ingsw.FlightBoard;

import java.util.*;

import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.Shipboard.Player;
import it.polimi.ingsw.Cards.Card;

/**
 * FlightMechanism class
 *
 * @author Boti
 */
public class FlightMechanism {
    // constants
    private final int normalCardCount = 12;
    private final int testCardCount = 8;
    // Stack as ArrayList
    private Stack<Card> cardStack;

    /**
     * FlightMechanism constructor, create cardStack
     *
     * @param gameType  Type of game
     * @param cardsList List of already shuffled cards
     */
    public FlightMechanism(GameType gameType, List<Card> cardsList) {
        cardStack = new Stack<>();
        int cardCount;
        if (gameType == GameType.NormalGame)
            cardCount = normalCardCount;
        else
            cardCount = testCardCount;
        for (int i = 0; i < cardCount; i++)
            cardStack.push(cardsList.get(i));
    }

    // TODO
    public FlightBoard startFlightMechanism(GameType gameType, List<Card> cardsList, List<Player> playerList) {
        FlightBoard flightBoard = setUpBoard(gameType, playerList);
        return flightBoard;
    }

    // TODO
    private FlightBoard setUpBoard(GameType gameType, List<Player> playerList) {
        FlightBoard flightBoard = new FlightBoard(gameType);
        // add players
        return flightBoard;
    }

    /**
     * Get new card from the cardStack
     *
     * @return new card
     */
    private Card getNewCard() {
        return cardStack.pop();
    }

    private void endFlightMehcanism() {
    }
}