package it.polimi.ingsw.Application.AssemblyPhase;

import it.polimi.ingsw.Application.GameInformation;
import it.polimi.ingsw.Assembly.AssemblyProtocol;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class AssemblyGame {
    private GameState currentState;
    private boolean running = true;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private GameInformation gameInformation;
    private AssemblyProtocol assemblyProtocol;
    private AssemblyView assemblyView;

    public AssemblyGame(GameInformation gameInformation) {
        this.gameInformation = gameInformation;
        assemblyProtocol = new AssemblyProtocol(gameInformation.getCardsList(),gameInformation.getComponentList(), gameInformation.getGameType());
        assemblyView = new AssemblyView();
    }

    public AssemblyProtocol getAssemblyProtocol() {
        return assemblyProtocol;
    }

    public void setState(GameState newState) {
        this.currentState = newState;
        currentState.enter(this, assemblyView);
    }

    public GameInformation getGameInformation() {
        return gameInformation;
    }

    public void setRunning(boolean value) {
        running = value;
    }

    public void start() { // qui va lanciato il thread per tutti i player, non solo per il primo
        setState(new AssemblyState(assemblyView,assemblyProtocol, gameInformation.getPlayerList().getFirst()));

        // Thread separato per leggere l'input dell'utente
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (running) {
                String input = scanner.nextLine();
                inputQueue.offer(input);
            }
        }).start();

        // Loop principale non bloccante
        while (running) {
            try {
                Thread.sleep(100); // 10 "frame" al secondo
            } catch (InterruptedException ignored) {}

            // Controlla input utente
            String input = inputQueue.poll();
            if (input != null) {
                currentState.handleInput(input, this);
            }

            // Aggiorna stato corrente (per esempio, gestione timer)
            currentState.update(this);
        }

        assemblyView.printGameOverMessage();
    }

    //questo andrà spostato nella classe con dentro gameInformation inizializzato
    public static void main(String[] args) {
        new AssemblyGame().start();
    }
}