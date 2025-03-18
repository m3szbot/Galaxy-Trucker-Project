package it.polimi.ingsw.FlightBoard;

import java.util.*;
// import Card
// import it.polimi.ingsw.;
import it.polimi.ingsw.shipboard.Player;
import it.polimi.ingsw.Application.GameType;
import it.polimi.ingsw.cards.Card;


public class FlightMechanism {
    // constants
    final int normalCardCount = 12;
    final int testCardCount = 8;
    // ArrayList
    private Stack<Card> cardStack;

    // cards passed already shuffled?

    // flight mechanism constructor
    // adds X already shuffled cards to cardStack
    public FlightMechanism(Card[] cardsList, GameType gameType) {
        cardStack = new Stack<Card>();
        int cardCount;
        if (gameType.equals("NormalGame"))
            cardCount = normalCardCount;
        else
            cardCount = testCardCount;
        for (int i = 0; i < cardCount; i++) {
            cardStack.push(cardsList[i]);
        }

    }

    // TODO
    public FlightBoard startFlightMechanism(Card[] cardsList, GameType gameType, Player[] playerList) {
        FlightBoard flightBoard = setUpBoard(gameType, playerList);
        return flightBoard;
    }

    // TODO
    private FlightBoard setUpBoard(GameType gameType, Player[] playerList) {
        FlightBoard flightBoard = new FlightBoard(gameType);
        // add players
        return flightBoard;
    }

    // get new card from the stack
    private Card getNewCard() {
        return cardStack.pop();
    }

    private void endFlightMehcanism() {
    }
}