package application;

import model.domain.Quiz;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class QuizConsole {
    public static void main(String[] args) {
        Quiz quiz = new Quiz("src/files/questions.txt");
        play(quiz);
    }

    public static void play(Quiz quiz) {
        int points = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("----------------------------------------------------");
                System.out.println(quiz.getNextQuestion());
                System.out.print("Answer: ");

                int input = scanner.nextInt();
                try {
                    String s = quiz.getPossibleAnswers().get(input - 1);
                    if (quiz.isCorrectAnswer(s)) {
                        System.out.println("Correct");
                        points++;
                    } else {
                        System.out.println("Wrong answer");
                        System.out.println("The correct answer was: " + quiz.getRightAnswer());
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Wrong answer");
                    System.out.println("The correct answer was: " + quiz.getRightAnswer());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Game over!\nPoints: " + points);
        }
    }
}
