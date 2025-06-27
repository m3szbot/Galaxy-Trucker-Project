package it.polimi.ingsw.Model.GameInformation;

import java.io.Serializable;

/**
 * Phases of the game.
 */
public enum GamePhase implements Serializable {
    Initialization, Assembly, Correction, Flight, Evaluation, Lobby;
}
