package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button accediButton;
    @FXML
    private Button registratiButton;

    private AutenteticazioneService autenticazioneService; // ← LAZY!

    @FXML
    private void initialize() {
        accediButton.setOnAction(e -> onLogin());
        registratiButton.setOnAction(event -> vaiRegistrazione());
    }

    private void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // ← CREAZIONE SERVICE SOLO QUI!
        if (autenticazioneService == null) {
            autenticazioneService = new AutenteticazioneService();
        }

        boolean loginOk = autenticazioneService.login(username, password);

        if (!loginOk) {
            mostraAlert(Alert.AlertType.ERROR,
                    "Errore di login",
                    "Username o password errati.");
            return;
        }

        Utente utenteCorrente = SessioneCorrente.getUtenteCorrente();

        if (utenteCorrente != null && utenteCorrente.getIdCasa() != null && utenteCorrente.getIdCasa()>0) {
            SceneNavigator.navigateTo("HomeView.fxml");
        } else {
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
        }
    }

    private void vaiRegistrazione() {
        SceneNavigator.navigateTo("RegistrazioneView.fxml");
    }

    private void mostraAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
