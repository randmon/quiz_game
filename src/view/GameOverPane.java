package view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class GameOverPane extends VBox {

    public GameOverPane(int points) {
        super(20);
        getStyleClass().add("gameoverVB");
        setAlignment(Pos.CENTER);

        Label gameOverLabel = new Label("Game over");
        gameOverLabel.getStyleClass().add("prompt");
        getChildren().add(gameOverLabel);

        Label pointsLabel = new Label("Points: " + points);
        pointsLabel.getStyleClass().add("prompt");
        getChildren().add(pointsLabel);

        Button quit = new Button("Quit");
        getChildren().add(quit);
        quit.setOnAction(event -> System.exit(0));
    }
}