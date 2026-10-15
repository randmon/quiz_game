package desktop.application;

import desktop.controller.Controller;
import javafx.application.Application;
import javafx.stage.Stage;
import model.Facade;
import desktop.view.MainView;

public class QuizApp extends Application {
    private final Facade model = new Facade("javaQuestions.txt");
    private final Controller controller = new Controller(model);

    @Override
    public void start(Stage primaryStage) {
        new MainView(controller);
    }
}
