package it.polimi.ingsw.Application;

import java.io.IOException;

public interface Startable {

    void start(GameInformation gameInformation, SetUpView setUpView) throws IOException;
}
