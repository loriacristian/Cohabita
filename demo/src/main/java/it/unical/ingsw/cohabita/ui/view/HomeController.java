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
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class HomeController {

    @FXML private Label oggiToccaLabel;
    @FXML private Label prossimoTurnoLabel;

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

        inizializzaTabellaClassifica();

        caricaPulizie();
        caricaClassifica();
    }



    private void inizializzaTabellaClassifica() {
        colNome.setCellValueFactory(new PropertyValueFactory<>("nomeCompleto"));
        colPunti.setCellValueFactory(new PropertyValueFactory<>("mediaVoti"));
        colTurni.setCellValueFactory(new PropertyValueFactory<>("numeroVoti"));

        colNome.setCellFactory(col -> new TableCell<VoceClassificaFX, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setAlignment(Pos.CENTER);
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
                setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f / 5", item.doubleValue()));
                }
            }
        });
        colPunti.setStyle(" -fx-font-weight: bold; -fx-text-fill: green;");
        colTurni.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");

        classificaTable.setItems(FXCollections.observableArrayList());
    }



    private void caricaPulizie() {
        LocalDate oggi = LocalDate.now();
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
            String data = prossimoTurno.getDataTurno().format(formatoData);
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


}
