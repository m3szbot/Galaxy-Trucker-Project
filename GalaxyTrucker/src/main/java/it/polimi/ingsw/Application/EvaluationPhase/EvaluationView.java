package it.polimi.ingsw.Application.EvaluationPhase;

import java.util.Scanner;

public class EvaluationView {

    public EvaluationView() {
    }

    public void printEvaluationMessage() {
        System.out.println("EvaluationPhase: evaluating...");
    }

    public void showPlayerCredits(String message) {
        System.out.println(message);
    }


    // TODO change to state pattern
    public boolean askAnotherGame(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        if (scanner.next().equals("y")) {
            return true;
        }
        return false;
    }
}
