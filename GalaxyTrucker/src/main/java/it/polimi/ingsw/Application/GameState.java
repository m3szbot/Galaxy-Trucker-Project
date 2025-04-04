package it.polimi.ingsw.Application;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public interface GameState {
    void enter(Game game, AssemblyView assemblyView);
    void handleInput(String input, Game game);
    default void update(Game game) {} // opzionale, può essere vuoto
}