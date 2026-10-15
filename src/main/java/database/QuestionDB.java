package database;

import domain.Quiz;
import domain.questions.Question;

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
