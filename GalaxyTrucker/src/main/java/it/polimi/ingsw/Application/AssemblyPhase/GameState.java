package it.polimi.ingsw.Application.AssemblyPhase;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public interface GameState {
    void enter(AssemblyPhase assemblyPhase, AssemblyView assemblyView);
    void handleInput(String input, AssemblyPhase assemblyPhase);
    default void update(AssemblyPhase assemblyPhase) {} // opzionale, può essere vuoto
}