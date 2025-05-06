package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.Connection.ClientSide.View.AssemblyView.AssemblyView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public interface GameState {
    void enter(AssemblyThread assemblyPhase, AssemblyView assemblyView);

    void handleInput(String input, AssemblyThread assemblyThread);

    default void update(AssemblyThread assemblyThread) {
    } // opzionale, può essere vuoto
}