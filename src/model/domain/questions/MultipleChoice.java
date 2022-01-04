package model.domain.questions;

import model.DomainException;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoice extends Question {
    private final List<String> possibleAnswers;

    public MultipleChoice(String prompt, String correctAnswer) {
        super(prompt, correctAnswer);
        possibleAnswers = new ArrayList<>();
    }

    public void addPossibleAnswer(String s) {
        if (s == null || s.length() == 0) throw new DomainException("Answer cannot be empty");
        possibleAnswers.add(s);
    }

    @Override
    public String toString() {
       StringBuilder result = new StringBuilder(super.toString());
       int i = 1;
       for (String s : possibleAnswers) {
           result.append("\n").append("\t(").append(i).append(") ").append(s);
           i++;
       }
       return result.toString();
    }

    @Override
    public List<String> getAnswerList() {
        return possibleAnswers;
    }
}
