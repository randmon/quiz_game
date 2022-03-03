package model.domain;

import model.database.QuestionDB;
import model.domain.questions.Question;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

public class Quiz {
    private final QuestionDB questionDB;
    private final Deque<Question> remainingQuestions;
    private Question currentQuestion;
    private String title;
    private int points;

    public Quiz(String path) {
        questionDB = new QuestionDB(path, this);
        remainingQuestions = new LinkedList<>();
        remainingQuestions.addAll(questionDB.getQuestions());
        points = 0;
    }

    public Question getNextQuestion() throws NoSuchElementException {
        currentQuestion = remainingQuestions.removeFirst();
        return currentQuestion;
    }

    public List<String> getPossibleAnswers() {
        return currentQuestion.getAnswerList();
    }

    public boolean isCorrectAnswer(String input) {
        return currentQuestion.isCorrect(input);
    }

    public String getCorrectAnswer() {
        return currentQuestion.getCorrectAnswer();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void addPoint() {
        ++points;
    }

    public int getPoints() {
        return points;
    }

    public boolean hasQuestionsLeft() {
        return remainingQuestions.size() > 0;
    }

    public boolean answer(String s) {
        if (isCorrectAnswer(s)) {
            addPoint();
            return true;
        } else return false;
    }

    public String getNextType() {
        Question q = remainingQuestions.peekFirst();
        return q.getType();
    }

    public int getTotalAmount() {
        return questionDB.getQuestions().size();
    }
}
