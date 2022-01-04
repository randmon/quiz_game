package view;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class QuestionPane extends VBox {
    private Label promptLabel;
    private List<Button> answerButtons = new ArrayList<>();
    private Button selectedAnswer, submitButton;
    private VBox answersVB;
    private Controller controller;
    private String type;
    private TextField writtenAnswer;

    public QuestionPane(Controller controller, String type) {
        this.controller = controller;
        this.setAlignment(Pos.TOP_CENTER);
        this.type = type;

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
        answersVB.setPadding(new Insets(20, 0, 20,0));
        getChildren().add(answersVB);

        if (type.equals("m")) {
            showMultiple();
        } else if (type.equals("w")) {
            showWritten();
        }
    }

    private void showMultiple() {
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
    }

    private void showWritten() {
        writtenAnswer = new TextField();
        answersVB.getChildren().add(writtenAnswer);

        submitButton = new Button("Submit");
        answersVB.getChildren().add(submitButton);
        submitButton.setOnAction(event -> {
            controller.answer(writtenAnswer.getText());
            answersVB.getChildren().remove(submitButton);
        });
    }

    public void answeredWrong() {
        String correctAnswer = controller.getCorrectAnswer();
        if (type.equals("m")) {
            for (Button b : answerButtons) {
                if (b.getText().equals(correctAnswer)) b.getStyleClass().add("rightAnswer");
                b.setDisable(true);
            }
            selectedAnswer.getStyleClass().add("selectedWrong");
        } else if (type.equals("w")) {
            writtenAnswer.setDisable(true);
            Label correctLabel = new Label("Correct answer: " + correctAnswer);
            correctLabel.getStyleClass().add("correctLabel");
            answersVB.getChildren().add(correctLabel);
        }
    }

    public void answeredCorrect() {
        if (type.equals("m")) {
            String correctAnswer = controller.getCorrectAnswer();
            for (Button b : answerButtons) {
                if (b.getText().equals(correctAnswer)) b.getStyleClass().add("rightAnswer");
                b.setDisable(true);
            }
        } else if (type.equals("w")) {
            writtenAnswer.setDisable(true);
        }
    }
}
