package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

public interface TokenLoss {

    default void inflictLoss(Player player, ElementType lossType, int quantity) {


    }
}
