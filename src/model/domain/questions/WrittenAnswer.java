package model.domain.questions;

import java.util.ArrayList;
import java.util.List;

public class WrittenAnswer extends Question {

    public WrittenAnswer(String propmt, String correctAnswer) {
        super(propmt, correctAnswer);
    }

    @Override
    public List<String> getAnswerList() {
        return new ArrayList<>();
    }

    @Override
    public String getType() {
        return "w";
    }
}
