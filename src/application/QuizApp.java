package application;

import controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Facade;
import view.MainView;

public class QuizApp extends Application {
    private final Facade model = new Facade("src/files/questions.txt");
    private final Controller controller = new Controller(model);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        new MainView(controller);
    }
}
