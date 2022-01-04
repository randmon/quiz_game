package controller;

import model.domain.QuizEvent;

public interface Observer {
    void update(QuizEvent event);
}
