package view;

import controller.Controller;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class MainView {
    private final Stage stage = new Stage();
    private final Controller controller;
    private final int width = 1280;
    private final int height = 720;
    private QuizPane quizPane;
    private final Group root;

    public MainView(Controller controller) {
        this.controller = controller;
        controller.setView(this);
        root = new Group();
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("style.css")).toExternalForm());

        stage.setTitle(controller.getTitle());
        //stage.getIcons().add(new Image("icon.png"));
        stage.setResizable(false);
        stage.setScene(scene);
        stage.sizeToScene();
        stage.show();


        startQuiz();
    }

    private void startQuiz() {
        quizPane = new QuizPane(controller);
        quizPane.setPrefHeight(height);
        quizPane.setPrefWidth(width);
        root.getChildren().add(quizPane);

        controller.nextQuestion();
    }

    public void answer(boolean correct) {
        quizPane.answer(correct);
    }

    public void showNextQuestion() {
        quizPane.showQuestion();
    }

    public void gameOver(int points) {
        root.getChildren().clear();
        GameOverPane gameOverPane = new GameOverPane(points);
        gameOverPane.setPrefHeight(height);
        gameOverPane.setPrefWidth(width);
        root.getChildren().add(gameOverPane);
    }
}
