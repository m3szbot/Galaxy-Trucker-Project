package it.polimi.ingsw.Application;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public interface GameState {
    void enter(AssemblyGame assemblyGame, AssemblyView assemblyView);
    void handleInput(String input, AssemblyGame assemblyGame);
    default void update(AssemblyGame assemblyGame) {} // opzionale, può essere vuoto
}