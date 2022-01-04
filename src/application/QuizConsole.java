package application;

import model.domain.Quiz;
import model.domain.questions.MultipleChoice;
import model.domain.questions.Question;
import model.domain.questions.WrittenAnswer;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class QuizConsole {
    public static void main(String[] args) {
        Quiz quiz = new Quiz("src/files/javaQuestions.txt");
        play(quiz);
    }

    public static void play(Quiz quiz) {
        int points = 0;
        try {
            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.println("----------------------------------------------------");

                Question q = quiz.getNextQuestion();
                System.out.println(q);
                System.out.print("Answer: ");

                String input = scanner.nextLine();
                try {
                    String s = "";
                    if (q instanceof MultipleChoice) {
                        s = quiz.getPossibleAnswers().get(Integer.parseInt(input) - 1);
                    } else if (q instanceof WrittenAnswer) {
                        s = input;
                    }
                    if (quiz.isCorrectAnswer(s)) {
                        System.out.println("Correct");
                        points++;
                    } else {
                        System.out.println("Wrong answer");
                        System.out.println("The correct answer was: " + quiz.getCorrectAnswer());
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Wrong answer");
                    System.out.println("The correct answer was: " + quiz.getCorrectAnswer());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Game over!\nPoints: " + points);
        }
    }
}
