package it.unical.ingsw.cohabita;

import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneNavigator.initialize(primaryStage);

        primaryStage.setTitle("Cohabita");
        primaryStage.setResizable(false);
        try {
            Image icona = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/it/unical/ingsw/cohabita/assets/logoSenzaScrittaVerde.png")
            ));
            primaryStage.getIcons().add(icona);
        } catch (Exception e) {
            System.err.println("Attenzione: Impossibile caricare l'icona dell'app.");
            e.printStackTrace();
        }

        SceneNavigator.navigateTo("RegistrazioneView.fxml");

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}