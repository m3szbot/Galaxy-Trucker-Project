package it.polimi.ingsw.cards;

import it.polimi.ingsw.shipboard.Player;

public interface TokenLoss {

    default void inflictLoss(Player player, ElementType lossType, int quantity){

        

    }
}
