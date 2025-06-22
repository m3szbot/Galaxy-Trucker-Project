package it.polimi.ingsw.View.GUI;

import it.polimi.ingsw.Connection.ClientSide.utils.ClientInputManager;

public interface PlayerInputSetter {

    public default void setUserInput(String input){

        ClientInputManager.setUserInput(input);

    }

}
