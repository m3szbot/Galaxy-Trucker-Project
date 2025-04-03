package it.polimi.ingsw.Application;

import it.polimi.ingsw.Assembly.AssemblyProtocol;

import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Game {
    private GameState currentState;
    private boolean running = true;
    private BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private GameInformation gameInformation;
    private AssemblyProtocol assemblyProtocol;
    private AssemblyView assemblyView;

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

    public void start() {
        setState(new AssemblyState(assemblyView, assemblyProtocol));

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

        System.out.println("🎮 Game Over.");
    }

    public static void main(String[] args) {
        new Game().start();
    }
}