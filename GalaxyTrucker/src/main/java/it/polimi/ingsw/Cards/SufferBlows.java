package it.polimi.ingsw.Cards;

import it.polimi.ingsw.Shipboard.Player;

public interface SufferBlows {

    default void hit(Player player, Blow[] blows, ElementType blowType) {


    }

}
