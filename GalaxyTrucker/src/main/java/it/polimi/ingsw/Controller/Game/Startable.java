package it.polimi.ingsw.Controller.Game;

import it.polimi.ingsw.Model.GameInformation.GameInformation;

import java.io.IOException;

public interface Startable {
    void start(GameInformation gameInformation) throws IOException;
}
