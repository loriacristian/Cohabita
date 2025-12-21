package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.casa.CasaService;
import it.unical.ingsw.cohabita.application.classifica.VoceClassifica;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.domain.TurnoPulizie;
import it.unical.ingsw.cohabita.domain.Utente;
import it.unical.ingsw.cohabita.ui.model.VoceClassificaFX;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {
    @FXML
    private Label nomeCasaLabel;
    @FXML private Label ruoloLabel;
    @FXML private Button btnDashboard;
    @FXML private Button btnCoinquilini;
    @FXML private Button btnPulizie;
    @FXML private Button btnImpostazioni;
    @FXML private Button btnLogout;

    // Card superiori
    @FXML private Label oggiToccaLabel;
    @FXML private Label prossimoTurnoLabel;

    // Classifica
    @FXML private TableView<VoceClassificaFX> classificaTable;
    @FXML private TableColumn<VoceClassificaFX, String> colNome;
    @FXML private TableColumn<VoceClassificaFX, Number> colPunti;
    @FXML private TableColumn<VoceClassificaFX, Number> colTurni;

    private final PulizieService pulizieService = PulizieService.defaultInstance();
    private final UtenteService utenteService = UtenteService.defaultInstance();

    private Utente utenteCorrente;

    @FXML
    private void initialize() {
        utenteCorrente= SessioneCorrente.getUtenteCorrente();

        inizializzaSidebar();
        inizializzaTabellaClassifica();

        caricaDatiCasa();
        caricaPulizie();
        caricaClassifica();
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

    private void inizializzaTabellaClassifica() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colPunti.setCellValueFactory(new PropertyValueFactory<>("mediaVoti"));
        colTurni.setCellValueFactory(new PropertyValueFactory<>("numeroVoti"));

        colNome.setCellFactory(col -> new TableCell<VoceClassificaFX, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    int index = getIndex();
                    String badge = "";
                    if (index == 0) badge = "🥇 ";
                    else if (index == 1) badge = "🥈 ";
                    else if (index == 2) badge = "🥉 ";
                    setText(badge + item);
                    setStyle("-fx-font-weight: 700; -fx-text-fill: #2d3436;");
                }
            }
        });

        colPunti.setCellFactory(col -> new TableCell<VoceClassificaFX, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f", item.doubleValue()));
                }
            }
        });

        classificaTable.setItems(FXCollections.observableArrayList());
    }

    private void caricaDatiCasa() {

        if (utenteCorrente == null || utenteCorrente.getIdCasa() == null) {
            mostraErrore("Nessuna casa associata.");
            SceneNavigator.navigateTo("InviteCodeView.fxml");
            return;
        }

        Casa casa = pulizieService.getCasa(utenteCorrente.getIdCasa());
        if (casa != null) {
            nomeCasaLabel.setText(casa.getNomeCasa());
        } else {
            nomeCasaLabel.setText("Casa sconosciuta");
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

    private void caricaPulizie() {
        LocalDate oggi = LocalDate.now();

        TurnoPulizie turnoOggi = pulizieService.getTurnoOggi(utenteCorrente.getIdCasa(), oggi);
        if (turnoOggi != null && turnoOggi.getIdUtente() != null) {
            Utente responsabile = utenteService.getDaId(turnoOggi.getIdUtente());
            String nome = responsabile != null ? responsabile.getUsername() : "Sconosciuto";
            oggiToccaLabel.setText(nome);
        } else {
            oggiToccaLabel.setText("Nessun turno oggi");
        }

        TurnoPulizie prossimoTurno = pulizieService.getProssimoTurnoUtente(utenteCorrente.getId(), oggi);
        if (prossimoTurno != null) {
            String data = prossimoTurno.getDataTurno().toString();
            prossimoTurnoLabel.setText(data);
        } else {
            prossimoTurnoLabel.setText("Nessun turno assegnato");
        }
    }

    private void caricaClassifica() {

        List<VoceClassifica> dtoList = pulizieService.getClassificaCasa(utenteCorrente.getIdCasa());

        List<VoceClassificaFX> vociJavaFX = dtoList.stream()
                .map(dto -> new VoceClassificaFX(
                        dto.getIdUtente(),
                        dto.getNomeCompleto(),
                        dto.getMediaVoti(),
                        dto.getNumeroVoti()
                ))
                .collect(Collectors.toList());

        ObservableList<VoceClassificaFX> items = FXCollections.observableArrayList(vociJavaFX);
        classificaTable.setItems(items);

    }

    private void mostraErrore(String messaggio) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Errore");
        alert.setHeaderText(null);
        alert.setContentText(messaggio);
        alert.showAndWait();
    }

}
