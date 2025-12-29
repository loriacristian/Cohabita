package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class CoinquiliniController {
    @FXML private Label nomeCasaLabel, ruoloLabel;
    @FXML private TextField codiceInvitoField;
    @FXML private TableView<Utente> tabellaCoinquilini;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private Button btnDashboard, btnCoinquilini, btnPulizie, btnImpostazioni, btnLogout;

    private final UtenteService utenteService = UtenteService.defaultInstance();
    private final PulizieService pulizieService = PulizieService.defaultInstance();
    private Utente utenteCorrente;

    @FXML
    public void initialize() {
        utenteCorrente = SessioneCorrente.getUtenteCorrente();

        if (utenteCorrente == null) {
            SceneNavigator.navigateTo("LoginView.fxml");
            return;
        }

        // Configura la tabella per mostrare solo lo username
        colNome.setCellValueFactory(new PropertyValueFactory<>("username"));

        inizializzaSidebar();
        caricaDatiCasa();
        caricaCoinquilini();
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

    private void caricaDatiCasa() {
        if (utenteCorrente.getIdCasa() == null) {
            SceneNavigator.navigateTo("CodiceInvitoView.fxml");
            return;
        }

        Casa casa = pulizieService.getCasa(utenteCorrente.getIdCasa());
        if (casa != null) {
            nomeCasaLabel.setText(casa.getNomeCasa());
            codiceInvitoField.setText(casa.getCodiceInvito());
        }

        if (utenteService.isAdmin(utenteCorrente)) {
            ruoloLabel.setText("Manager");
        } else {
            ruoloLabel.setText("Coinquilino");
        }
    }

    private void caricaCoinquilini() {
        List<Utente> lista = utenteService.getDaCasa(utenteCorrente.getIdCasa());
        tabellaCoinquilini.setItems(FXCollections.observableArrayList(lista));
    }
}