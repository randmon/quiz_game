package model.domain;

import model.database.QuestionDB;
import model.domain.questions.Question;

import java.util.*;

public class Quiz {
    private final QuestionDB questionDB;
    private final Deque<Question> remainingQuestions;
    private Question currentQuestion;

    public Quiz(String path) {
        questionDB = new QuestionDB(path);
        remainingQuestions = new LinkedList<>();
        remainingQuestions.addAll(questionDB.getQuestions());
    }

    public Question getNextQuestion() throws NoSuchElementException {
        currentQuestion = remainingQuestions.removeFirst();
        return currentQuestion;
    }

    public List<String> getPossibleAnswers() {
        return currentQuestion.getAnswerList();
    }

    public List<Boolean> isCorrectAnswer(List<String> input) {
        List<Boolean> result = new ArrayList<>();
        for (String s : input) {
            result.add(currentQuestion.isCorrect(s));
        }
        return result;
    }

    public boolean isCorrectAnswer(String input) {
        return currentQuestion.isCorrect(input);
    }

    public String getRightAnswer() {
        return currentQuestion.getCorrectAnswer();
    }
}
