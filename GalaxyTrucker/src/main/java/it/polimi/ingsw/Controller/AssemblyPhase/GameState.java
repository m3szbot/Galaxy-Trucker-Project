package it.polimi.ingsw.Controller.AssemblyPhase;

import it.polimi.ingsw.View.AssemblyView.AssemblyView;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public interface GameState {
    void enter(AssemblyThread assemblyPhase, AssemblyView assemblyView);

    void handleInput(String input, AssemblyThread assemblyPhase);

    default void update(AssemblyThread assemblyPhase) {
    } // opzionale, può essere vuoto
}