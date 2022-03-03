package model;

import controller.Observer;
import model.domain.QuizEvent;

import java.util.HashSet;
import java.util.Set;

public class Observable {
    private final Set<Observer> observers;

    public Observable() {
        observers = new HashSet<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void updateAll(QuizEvent event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}

