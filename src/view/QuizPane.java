package view;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class QuizPane extends VBox {
    private final Controller controller;
    private QuestionPane questionPane;

    public QuizPane(Controller controller) {
        this.controller = controller;
        setAlignment(Pos.TOP_CENTER);
    }

    public void showQuestion() {
        getChildren().clear();
        questionPane = new QuestionPane(controller);
        getChildren().add(questionPane);
    }

    public void answer(boolean correct) {
        if (correct) questionPane.answeredCorrect();
        else questionPane.answeredWrong();

        //Show next button
        Button nextButton = new Button("Continue");
        nextButton.setOnAction(event -> controller.nextQuestion());
        getChildren().add(nextButton);
    }
}
