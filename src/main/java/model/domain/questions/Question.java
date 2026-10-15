package model.domain.questions;

import model.QuizGameException;

import java.util.List;

public abstract class Question {
    private String prompt;
    private String correctAnswer;

    public Question(String prompt, String correctAnswer) {
        setPrompt(prompt);
        setCorrectAnswer(correctAnswer);
    }

    public boolean isCorrect(String userAnswer) {
        return userAnswer.equalsIgnoreCase(correctAnswer);
    }

    private void setCorrectAnswer(String answer) {
        if (answer == null || answer.isEmpty()) throw new QuizGameException("Answer cannot be empty");
        correctAnswer = answer;
    }

    private void setPrompt(String prompt) {
        if (prompt == null || prompt.isEmpty()) throw new QuizGameException("Prompt cannot be empty");
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
