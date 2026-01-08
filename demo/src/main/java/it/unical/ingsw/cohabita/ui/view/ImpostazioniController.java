package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.CicloPulizie;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import it.unical.ingsw.cohabita.ui.utility.utilitaGenerale;
import javafx.fxml.FXML;
import javafx.scene.control.*;



public class ImpostazioniController {
    @FXML
    private PasswordField vecchiaPasswordField;
    @FXML
    private PasswordField nuovaPasswordField;
    @FXML
    private PasswordField confermaPasswordField;

    @FXML
    private Button btnCambiaPassword;
    @FXML
    private Button btnLasciaCasa;

    private final PulizieService pulizieService= PulizieService.defaultInstance();
    private final AutenteticazioneService autenteticazioneService = new AutenteticazioneService();
    private CicloPulizie cicloAttivo;

    boolean isAdmin;

    private final UtenteService utenteService = UtenteService.defaultInstance();

    private Utente utenteCorrente;



    @FXML
    private void initialize() {
        btnCambiaPassword.setOnAction(e -> onCambiaPassword());

        utenteCorrente = SessioneCorrente.getUtenteCorrente();
        isAdmin = utenteService.isAdmin(utenteCorrente);

        if(isAdmin){
            btnLasciaCasa.setVisible(false);
            btnLasciaCasa.setManaged(false);
        }
        else{
            btnLasciaCasa.setVisible(true);
            btnLasciaCasa.setManaged(true);
            btnLasciaCasa.setOnAction(e -> onLasciaCasa());
        }
    }

    private void onCambiaPassword() {
        String vecchia = vecchiaPasswordField.getText();
        String nuova = nuovaPasswordField.getText();
        String conferma = confermaPasswordField.getText();

        if (vecchia.isEmpty() || nuova.isEmpty() || conferma.isEmpty()) {
            utilitaGenerale.mostraErrore("Compila tutti i campi.");
            return;
        }

        if (!nuova.equals(conferma)) {
            utilitaGenerale.mostraErrore("Le nuove password non coincidono.");
            return;
        }

        Utente utenteCorrente = SessioneCorrente.getUtenteCorrente();
        boolean cambioOk = autenteticazioneService.cambiaPassword(utenteCorrente, vecchia, nuova,conferma);

        if (cambioOk) {
            utilitaGenerale.mostraInfo("Successo", "Password cambiata correttamente.");
            vecchiaPasswordField.clear();
            nuovaPasswordField.clear();
            confermaPasswordField.clear();
        } else {
            utilitaGenerale.mostraErrore("Password attuale errata o errore nel cambio.");
        }
    }

    private void onLasciaCasa() {
        Utente utenteCorrente = SessioneCorrente.getUtenteCorrente();
        cicloAttivo = pulizieService.getCiclo(utenteCorrente.getIdCasa());

        boolean confermato = utilitaGenerale.mostraConferma(
                "Conferma uscita casa",
                "Vuoi davvero lasciare la casa?",
                "Perderai l'accesso a tutti i dati condivisi. Questa azione è irreversibile."
        );

        utilitaGenerale.mostraErrore("Vuoi davvero lasciare la casa?" );

        if (confermato) {
            cicloAttivo = pulizieService.getCiclo(utenteCorrente.getIdCasa());

            if (cicloAttivo != null) {
                pulizieService.cancellaCiclo(utenteCorrente.getIdCasa(), cicloAttivo.getIdCiclo());
            }

            autenteticazioneService.lasciaCasa(utenteCorrente);

            utilitaGenerale.mostraInfo("Uscita effettuata", "Hai lasciato la casa. Verrai riportato alla schermata iniziale.");
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
        }
    }

}