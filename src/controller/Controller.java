package controller;

import model.Facade;
import model.domain.QuizEvent;
import view.MainView;

import java.util.List;

public class Controller implements Observer {
    private MainView view;
    private Facade model;

    public Controller(Facade model) {
        this.model = model;
        model.addObserver(this);
    }

    @Override
    public void update(QuizEvent event) {
        switch (event) {
            case NEXT_QUESTION:
                view.showNextQuestion();
                break;
            case ANSWER_WRONG:
                view.answer(false);
                break;
            case ANSWER_CORRECT:
                view.answer(true);
                break;
            case GAMEOVER:
                view.gameOver(model.getPoints());
                break;
        }
    }

    public void setView(MainView view) {
        this.view = view;
    }

    public String getTitle() {
        return model.getTitle();
    }

    public String getPrompt() {
        return model.getPrompt();
    }

    public List<String> getAnswers() {
        return model.getAnswers();
    }

    public void answer(String text) {
        model.answer(text);
    }

    public String getCorrectAnswer() {
        return model.getCorrectAnswer();
    }

    public void nextQuestion() {
        model.nextQuestion();
    }
}
