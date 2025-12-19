package it.unical.ingsw.cohabita;

import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneNavigator.initialize(primaryStage);

        primaryStage.setTitle("Cohabita");
        primaryStage.setResizable(false);

        SceneNavigator.navigateTo("LoginView.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}