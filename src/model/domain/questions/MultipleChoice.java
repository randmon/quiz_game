package model.domain.questions;

import model.DomainException;

import java.util.ArrayList;
import java.util.Collections;
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
    public List<String> getAnswerList() {
        Collections.shuffle(possibleAnswers); //Randomize list
        return possibleAnswers;
    }

    @Override
    public String getType() {
        return "m";
    }
}
