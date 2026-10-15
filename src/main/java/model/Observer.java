package model;

import model.domain.QuizEvent;

public interface Observer {
    void update(QuizEvent event);
}
