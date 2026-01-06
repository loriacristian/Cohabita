package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SidebarController {

    @FXML
    private Button btnDashboard, btnCoinquilini, btnPulizie, btnImpostazioni, btnLogout;

    @FXML
    private Label nomeCasaLabel, ruoloLabel;

    private final PulizieService pulizieService = PulizieService.defaultInstance();
    private final UtenteService utenteService = UtenteService.defaultInstance();
    private Utente utenteCorrente;

    @FXML
    public void initialize() {
        utenteCorrente = SessioneCorrente.getUtenteCorrente();

        if (utenteCorrente != null) {
            inizializzaAzioniNavigazione();
            caricaInformazioniProfilo();
        }
    }

    private void inizializzaAzioniNavigazione() {
        btnDashboard.setOnAction(e -> SceneNavigator.navigateTo("HomeView.fxml"));
        btnCoinquilini.setOnAction(e -> SceneNavigator.navigateTo("CoinquiliniView.fxml"));
        btnPulizie.setOnAction(e -> SceneNavigator.navigateTo("PulizieView.fxml"));
        btnImpostazioni.setOnAction(e -> SceneNavigator.navigateTo("ImpostazioniView.fxml"));
        btnLogout.setOnAction(e -> {
            SessioneCorrente.rimuoviUtenteCorrente();
            SceneNavigator.navigateTo("LoginView.fxml");
        });
    }

    private void caricaInformazioniProfilo() {
        if (utenteCorrente.getIdCasa() == null) {
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
            return;
        }

        Casa casa = pulizieService.getCasa(utenteCorrente.getIdCasa());
        if (casa != null) {
            nomeCasaLabel.setText(casa.getNomeCasa());
        } else {
            nomeCasaLabel.setText("Casa non trovata");
        }

        if (utenteService.isAdmin(utenteCorrente)) {
            ruoloLabel.setText("Manager");
            btnCoinquilini.setVisible(true);
            btnCoinquilini.setManaged(true);
        } else {
            ruoloLabel.setText("Coinquilino");
            btnCoinquilini.setVisible(false);
            btnCoinquilini.setManaged(false);
        }
    }
}