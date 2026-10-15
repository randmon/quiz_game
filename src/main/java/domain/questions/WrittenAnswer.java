package domain.questions;

import java.util.ArrayList;
import java.util.List;

public class WrittenAnswer extends Question {

    public WrittenAnswer(String prompt, String correctAnswer) {
        super(prompt, correctAnswer);
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
