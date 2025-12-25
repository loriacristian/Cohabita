package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import it.unical.ingsw.cohabita.ui.utility.utilitaGenerale;

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
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String confermaPassword = confirmPasswordField.getText().trim();

        if(!inputValido(username, password, confermaPassword)){
            return;
        }
        boolean RegistrazioneOK= autenticazioneService.registrati(username, password, confermaPassword);
        if (RegistrazioneOK) {
            utilitaGenerale.mostraAlert(Alert.AlertType.INFORMATION,
                    "Registrazione completata",
                    "Account creato con successo!");
            tornaLogin();
        } else{
            utilitaGenerale.mostraAlert(Alert.AlertType.ERROR,
                    "Errore do registrazione",
                    "Username già esistente o passsword che non coincidono");
        }
    }

    public void tornaLogin() {
        SceneNavigator.navigateTo("LoginView.fxml");
    }

    public boolean inputValido(String username, String password, String confermaPassword ) {
        if (username == null || password == null || confermaPassword == null){
            utilitaGenerale.mostraAlert(Alert.AlertType.ERROR,
                    "Compila tutti i campi",
                    "Riempire tutti i campi e riprova");
            return false;
        }

        if(!password.equals(confermaPassword)) {
            utilitaGenerale.mostraAlert(Alert.AlertType.ERROR,
                    "Le password non coincidono",
                    "Controlla che le password siano uguali e riprova");
            return false;
        }

        boolean passwordOK= autenticazioneService.passwordValida(password);
        if (!passwordOK) {
            utilitaGenerale.mostraAlert(Alert.AlertType.ERROR,
                    "Password non valida",
                    "La password deve contenere almeno 8 caratteri di cui almeno: 1 carattere maiuscolo, 1 carattere minuscolo, 1 numero");
            return false;
        }
        return true;


    }

}
