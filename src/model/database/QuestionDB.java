package model.database;

import model.domain.Quiz;
import model.domain.questions.Question;

import java.util.LinkedList;
import java.util.List;

public class QuestionDB {
    private List<Question> questions;
    private FileManager fileManager;

    public QuestionDB(String path, Quiz quiz) {
        fileManager = new FileManager(path);
        questions = new LinkedList<>();
        questions = fileManager.loadQuestions(quiz);
    }

    public List<Question> getQuestions() {
        return questions;
    }
}
