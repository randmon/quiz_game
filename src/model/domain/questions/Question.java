package model.domain.questions;

import model.DomainException;

import java.util.*;

public abstract class Question {
    private String propmt;
    private String correctAnswer;

    public Question(String propmt, String correctAnswer) {
        setPropmt(propmt);
        setCorrecthAnswer(correctAnswer);
    }

    public boolean isCorrect(String userAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    private void setCorrecthAnswer(String answer) {
        if (answer == null || answer.length() == 0) throw new DomainException("Answer cannot be empty");
        correctAnswer = answer;
    }

    private void setPropmt(String propmt) {
        if (propmt == null || propmt.length() == 0) throw new DomainException("Prompt cannot be empty");
        this.propmt = propmt;
    }

    @Override
    public String toString() {
        return "Question: " + propmt;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public abstract List<String> getAnswerList();
}
