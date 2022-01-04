package model;

import model.domain.Quiz;

public class Facade extends Observable {
    private Quiz quiz;

    public Facade(String path) {
        quiz = new Quiz(path);
    }
}
