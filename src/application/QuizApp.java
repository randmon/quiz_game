package application;

import javafx.application.Application;
import javafx.stage.Stage;
import view.MainView;

public class QuizApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainView mainView = new MainView();
    }
}
