package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegistrazioneController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private PasswordField confirmPasswordField;
    @FXML
    private Button registerButton;
    @FXML
    private Button backToLoginButton;

    private final AutenteticazioneService autenticazioneService = new AutenteticazioneService();

    @FXML
    private void initialize() {
        registerButton.setOnAction(e -> onRegistrati());
        backToLoginButton.setOnAction(e -> tornaLogin());
    }

    public void onRegistrati() {
        String username = usernameField.getText();
        String password = passwordField.getText();
        String confermaPassword = confirmPasswordField.getText();

        boolean passwordOK= autenticazioneService.passwordValida(password);
        if (!passwordOK) {
            mostraAlert(Alert.AlertType.ERROR,
                    "Password non valida",
                    "La password deve contenere almeno 8 caratteri di cui almeno: 1 carattere maiuscolo, 1 carattere minuscolo, 1 numero e 1 carattere speciale");
        }

        boolean RegistrazioneOK= autenticazioneService.registrati(username, password, confermaPassword);
        if (RegistrazioneOK) {
            mostraAlert(Alert.AlertType.INFORMATION,
                    "Registrazione completata",
                    "Account creato con successo!");
            tornaLogin();
        } else{
            mostraAlert(Alert.AlertType.ERROR,
                    "Errore do registrazione",
                    "Username già esistente o passsword che non coincidono");
            tornaLogin();
        }
    }

    public void tornaLogin() {
        SceneNavigator.navigateTo("LoginView.fxml");
    }

    private void mostraAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
