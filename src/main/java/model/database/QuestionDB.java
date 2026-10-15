package model.database;

import model.domain.Quiz;
import model.domain.questions.Question;

import java.util.List;

public class QuestionDB {
    private final List<Question> questions;

    public QuestionDB(String path, Quiz quiz) {
        questions = new FileManager(path).loadQuestions(quiz);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
