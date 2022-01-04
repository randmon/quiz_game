package application;

import model.domain.Quiz;
import model.domain.questions.MultipleChoice;
import model.domain.questions.Question;
import model.domain.questions.WrittenAnswer;

import java.util.List;
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

                try {
                    String s = "";

                    if (q.getType().equals("m")) {
                        List<String> pAnswers = quiz.getPossibleAnswers();
                        for (int i = 0; i < pAnswers.size(); ++i) {
                            System.out.println("\t(" + (i+1) + ") " + pAnswers.get(i));
                        }

                        System.out.print("Answer: ");
                        String input = scanner.nextLine();
                        s = pAnswers.get(Integer.parseInt(input) - 1);
                    } else if (q.getType().equals("w")) {
                        System.out.print("Answer: ");
                        s = scanner.nextLine();
                    }
                    if (quiz.isCorrectAnswer(s)) {
                        System.out.println("Correct");
                        points++;
                    } else {
                        System.out.println("Wrong, the correct answer was: " + quiz.getCorrectAnswer());
                    }
                } catch (IndexOutOfBoundsException e) {
                    System.out.println("Wrong, the correct answer was: " + quiz.getCorrectAnswer());
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Game over!\nPoints: " + points + "/" + quiz.getTotalAmount());
        }
    }
}
