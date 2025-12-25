package it.unical.ingsw.cohabita.ui.navigation;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneNavigator {

    private static Stage primaryStage;

    public static void initialize(Stage stage) {
        primaryStage = stage;
    }
    private static final String pathGenerale = "/it/unical/ingsw/cohabita/fxml/";

    public static void navigateTo(String fxmlPath) {
        try {
            String fullPath = pathGenerale + fxmlPath;
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fullPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento della scena: " + fxmlPath);
        }
    }

    public static void navigateTo(String fxmlPath, double width, double height) {
        try {
            String fullPath = pathGenerale + fxmlPath;
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fullPath));
            Parent root = loader.load();

            Scene scene = new Scene(root, width, height);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento della scena: " + fxmlPath);
        }
    }

    public static <T> T navigateToAndGetController(String fxmlPath) {
        try {
            String fullPath = pathGenerale + fxmlPath;
            FXMLLoader loader = new FXMLLoader(SceneNavigator.class.getResource(fullPath));
            Parent root = loader.load();

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.centerOnScreen();

            return loader.getController();

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Errore nel caricamento della scena: " + fxmlPath);
            return null;
        }
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
