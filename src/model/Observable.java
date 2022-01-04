package model;

import controller.Observer;

import java.util.HashSet;
import java.util.Set;

public class Observable {
    Set<Observer> observers;

    public Observable() {
        observers = new HashSet<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void updateAll() {
        for (Observer o : observers) {
            o.update();
        }
    }
}

