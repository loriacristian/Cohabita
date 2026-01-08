package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import it.unical.ingsw.cohabita.ui.utility.utilitaGenerale;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.command.Command;
import it.unical.ingsw.cohabita.application.command.LoginCommand;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;

public class LoginController {

    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button accediButton;
    @FXML
    private Button registratiButton;

    private AutenteticazioneService autenticazioneService;

    private Command loginCommand;

    @FXML
    private BorderPane root;


    @FXML
    private void initialize() {

        loginCommand = new LoginCommand(this);

        accediButton.setOnAction(e -> loginCommand.execute());

        root.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                loginCommand.execute();
            }
        });

        registratiButton.setOnAction(e -> vaiRegistrazione());

        root.setFocusTraversable(true);
        root.requestFocus();
    }


    public void onLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (autenticazioneService == null) {
            autenticazioneService = new AutenteticazioneService();
        }

        boolean loginOk = autenticazioneService.login(username, password);

        if (!loginOk) {
            utilitaGenerale.mostraErrore("Username o password errati.");
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
}
