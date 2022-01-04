package view;

import controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class QuizPane extends VBox {
    private final Controller controller;
    private QuestionPane questionPane;

    public QuizPane(Controller controller) {
        super(20);
        this.controller = controller;
        setAlignment(Pos.TOP_CENTER);
    }

    public void showQuestion(String type) {
        getStyleClass().clear();
        getStyleClass().add("vb");
        getChildren().clear();
        questionPane = new QuestionPane(controller, type);
        getChildren().add(questionPane);
    }

    public void answer(boolean correct) {
        if (correct) {
            getStyleClass().clear();
            getStyleClass().add("correctVB");
            questionPane.answeredCorrect();
        }
        else {
            getStyleClass().clear();
            getStyleClass().add("wrongVB");
            questionPane.answeredWrong();
        }

        //Show next button
        Button nextButton = new Button("Continue");
        nextButton.getStyleClass().add("continueButton");
        nextButton.setOnAction(event -> controller.nextQuestion());
        getChildren().add(nextButton);
    }
}
