package it.polimi.ingsw.Controller.FlightPhase.Cards;

import java.io.Serializable;

/**
 * Enumeration used in the class Attack States Setting to
 * identify the result of a player's action
 */

public enum AttackStates implements Serializable {
    Equalized, PlayerDefeated, EnemyDefeated;
}
