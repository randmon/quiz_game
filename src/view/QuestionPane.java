package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class QuestionPane extends VBox {
    private Label promptLabel;
    private List<Button> answerButtons = new ArrayList<>();
    private Button selectedAnswer;
    private VBox answersVB;
    private Controller controller;

    public QuestionPane(Controller controller) {
        this.controller = controller;
        this.setAlignment(Pos.TOP_CENTER);

        promptLabel = new Label(controller.getPrompt());
        promptLabel.getStyleClass().add("prompt");
        promptLabel.setWrapText(true);

        HBox promptHB = new HBox();
        promptHB.getChildren().add(promptLabel);
        promptHB.setAlignment(Pos.CENTER);
        promptHB.setPadding(new Insets(20, 20, 20, 20));
        this.getChildren().add(promptHB);

        answersVB = new VBox(10);
        answersVB.setAlignment(Pos.TOP_CENTER);
        answersVB.getStyleClass().add("answersVB");
        answersVB.setPadding(new Insets(20, 0, 20,0));

        List<String> answers = controller.getAnswers();
        for (String a : answers) {
            Button b = new Button(a);
            answerButtons.add(b);
            answersVB.getChildren().add(b);
            b.setOnAction(event -> {
                selectedAnswer = b;
                controller.answer(b.getText());
            });
        }
        this.getChildren().add(answersVB);
    }

    public void answeredWrong() {
        answersVB.getStyleClass().add("wrongVB");
        String correctAnswer = controller.getCorrectAnswer();
        for (Button b : answerButtons) {
            if (b.getText().equals(correctAnswer)) b.getStyleClass().add("rightAnswer");
            b.setDisable(true);
        }
        selectedAnswer.getStyleClass().add("selectedWrong");
    }

    public void answeredCorrect() {
        answersVB.getStyleClass().add("correctVB");
        String correctAnswer = controller.getCorrectAnswer();
        for (Button b : answerButtons) {
            if (b.getText().equals(correctAnswer)) b.getStyleClass().add("rightAnswer");
            b.setDisable(true);
        }
    }
}
