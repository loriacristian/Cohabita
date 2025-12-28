package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.AutenteticazioneService;
import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.CicloPulizie;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.CasaDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.dao.PulizieDao;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.CasaDaoImpl;
import it.unical.ingsw.cohabita.technicalservice.persistence.impl.PulizieDaoImpl;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.Optional;

public class ImpostazioniController {

    @FXML
    private Label nomeCasaLabel;
    @FXML
    private Label ruoloLabel;

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnCoinquilini;
    @FXML
    private Button btnPulizie;
    @FXML
    private Button btnImpostazioni;
    @FXML
    private Button btnLogout;

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

    private final CasaDao casaDao = new CasaDaoImpl();
    private final PulizieService pulizieService= PulizieService.defaultInstance();
    private final AutenteticazioneService autenteticazioneService = new AutenteticazioneService();
    private CicloPulizie cicloAttivo;



    @FXML
    private void initialize() {
        caricaDatiCasa();
        inizializzaSidebar();

        btnCambiaPassword.setOnAction(e -> onCambiaPassword());
        btnLasciaCasa.setOnAction(e -> onLasciaCasa());
    }

    private void caricaDatiCasa() {

        Utente utenteCorrente= SessioneCorrente.getUtenteCorrente();
        if (utenteCorrente == null || utenteCorrente.getIdCasa() == null) {
            mostraErrore("Nessuna casa associata.");
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
            return;
        }

        Casa casa = casaDao.trovaCasaId(utenteCorrente.getIdCasa());
        if (casa == null) {
            mostraErrore("Casa non trovata.");
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
            return;
        }

        nomeCasaLabel.setText(casa.getNomeCasa());

        Integer ruolo = utenteCorrente.getRuolo();
        if (ruolo != null && ruolo == 0) {
            ruoloLabel.setText("Manager");
            btnCoinquilini.setVisible(true);
            btnCoinquilini.setManaged(true);
            btnLasciaCasa.setVisible(false);
        } else {
            ruoloLabel.setText("Coinquilino");
            btnCoinquilini.setVisible(false);
            btnCoinquilini.setManaged(false);
            btnLasciaCasa.setVisible(true);
        }
    }

    private void inizializzaSidebar() {
        btnDashboard.setOnAction(e -> SceneNavigator.navigateTo("HomeView.fxml"));
        btnCoinquilini.setOnAction(e -> SceneNavigator.navigateTo("CoinquiliniView.fxml"));
        btnPulizie.setOnAction(e -> SceneNavigator.navigateTo("PulizieView.fxml"));
        btnImpostazioni.setOnAction(e -> SceneNavigator.navigateTo("ImpostazioniView.fxml"));
        btnLogout.setOnAction(e -> {
            SessioneCorrente.rimuoviUtenteCorrente();
            SceneNavigator.navigateTo("LoginView.fxml");
        });
    }

    private void onCambiaPassword() {
        String vecchia = vecchiaPasswordField.getText();
        String nuova = nuovaPasswordField.getText();
        String conferma = confermaPasswordField.getText();

        if (vecchia.isEmpty() || nuova.isEmpty() || conferma.isEmpty()) {
            mostraErrore("Compila tutti i campi.");
            return;
        }

        if (!nuova.equals(conferma)) {
            mostraErrore("Le nuove password non coincidono.");
            return;
        }

        Utente utenteCorrente = SessioneCorrente.getUtenteCorrente();
        boolean cambioOk = autenteticazioneService.cambiaPassword(utenteCorrente, vecchia, nuova,conferma);

        if (cambioOk) {
            mostraInfo("Successo", "Password cambiata correttamente.");
            vecchiaPasswordField.clear();
            nuovaPasswordField.clear();
            confermaPasswordField.clear();
        } else {
            mostraErrore("Password attuale errata o errore nel cambio.");
        }
    }

    private void onLasciaCasa() {
        Utente utenteCorrente = SessioneCorrente.getUtenteCorrente();
        cicloAttivo = pulizieService.getCiclo(utenteCorrente.getIdCasa());

        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma uscita casa");
        conferma.setHeaderText("Vuoi davvero lasciare la casa?");
        conferma.setContentText("Perderai l'accesso a tutti i dati condivisi. Questa azione è irreversibile.");

        Optional<ButtonType> risultato = conferma.showAndWait();
        if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
            pulizieService.cancellaCiclo(utenteCorrente.getIdCasa(), cicloAttivo.getIdCiclo());
            autenteticazioneService.lasciaCasa(utenteCorrente);

            mostraInfo("Uscita", "Hai lasciato la casa. Verrai riportato alla scelta casa.");
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
        }
    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

    private void mostraInfo(String titolo, String messaggio) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titolo);
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }
}