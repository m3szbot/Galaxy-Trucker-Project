package it.polimi.ingsw.Application.EvaluationPhase;

import java.util.Scanner;

public class EvaluationView {

    public EvaluationView() {
    }

    public void ShowPlayerCredits(String message) {
        System.out.println(message);
    }


    // TODO change to state pattern
    public boolean AskAnotherGame(String message) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(message);
        if (scanner.next().equals("y")) {
            return true;
        }
        return false;
    }
}
