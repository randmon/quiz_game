package model;

import model.domain.Quiz;
import model.domain.QuizEvent;

import java.util.List;

public class Facade extends Observable {
    private final Quiz quiz;

    public Facade(String path) {
        quiz = new Quiz(path);
    }


    public String getTitle() {
        return quiz.getTitle();
    }

    public String getPrompt() {
        return quiz.getNextQuestion().getPrompt();
    }

    public List<String> getAnswers() {
        return quiz.getPossibleAnswers();
    }

    public void addPoint() {
        quiz.addPoint();
    }

    public void answer(String s) {
        if (quiz.answer(s)) {
            updateAll(QuizEvent.ANSWER_CORRECT);
        } else {
            updateAll(QuizEvent.ANSWER_WRONG);
        }
    }

    public String getCorrectAnswer() {
        return quiz.getCorrectAnswer();
    }

    public void nextQuestion() {
        if (quiz.hasQuestionsLeft()) {
            String type = quiz.getNextType();
            if (type.equals("w")) updateAll(QuizEvent.NEXT_QUESTION_W);
            else if (type.equals("m")) updateAll(QuizEvent.NEXT_QUESTION_M);
        } else {
            updateAll(QuizEvent.GAMEOVER);
        }
    }

    public int getPoints() {
        return quiz.getPoints();
    }
}
