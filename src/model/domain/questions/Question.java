package model.domain.questions;

import model.DomainException;

import java.util.*;

public abstract class Question {
    private String prompt;
    private String correctAnswer;

    public Question(String propmt, String correctAnswer) {
        setPrompt(propmt);
        setCorrecthAnswer(correctAnswer);
    }

    public boolean isCorrect(String userAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    private void setCorrecthAnswer(String answer) {
        if (answer == null || answer.length() == 0) throw new DomainException("Answer cannot be empty");
        correctAnswer = answer;
    }

    private void setPrompt(String prompt) {
        if (prompt == null || prompt.length() == 0) throw new DomainException("Prompt cannot be empty");
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return "Question: " + prompt;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public abstract List<String> getAnswerList();

    public String getPrompt() {
        return prompt;
    }

    public abstract String getType();
}
