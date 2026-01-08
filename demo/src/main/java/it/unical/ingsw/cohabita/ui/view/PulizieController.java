package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.authentication.SessioneCorrente;
import it.unical.ingsw.cohabita.application.pulizie.PulizieService;
import it.unical.ingsw.cohabita.application.utente.UtenteService;
import it.unical.ingsw.cohabita.domain.*;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import it.unical.ingsw.cohabita.ui.utility.utilitaGenerale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import java.util.List;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

public class PulizieController {
    @FXML private Button btnCreaCiclo;
    @FXML private Button btnEliminaCiclo;
    @FXML private VBox noCicloBox;
    @FXML private StackPane turniBox;
    @FXML private ComboBox<String> filtroCombo;
    @FXML private TableView<TurnoConDettagli> turniTable;
    @FXML private TableColumn<TurnoConDettagli, LocalDate> colData;
    @FXML private TableColumn<TurnoConDettagli, String> colAssegnato;
    @FXML private TableColumn<TurnoConDettagli, String> colStato;
    @FXML private TableColumn<TurnoConDettagli, Number> colValutazione;
    @FXML private TableColumn<TurnoConDettagli, Void> colAzioni;

    private final PulizieService pulizieService = PulizieService.defaultInstance();
    private final UtenteService utenteService = UtenteService.defaultInstance();

    private Utente utenteCorrente;
    private boolean isAdmin;
    private CicloPulizie cicloAttivo;

    private ObservableList<TurnoConDettagli> turniMaster = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        utenteCorrente = SessioneCorrente.getUtenteCorrente();
        isAdmin = utenteService.isAdmin(utenteCorrente);

        btnCreaCiclo.setOnAction(e -> mostraDialogCreaCiclo());
        btnEliminaCiclo.setOnAction(e-> eliminaCiclo());


        inizializzaFiltri();
        inizializzaTabella();
        verificaCicloECaricaTurni();
    }

    private void inizializzaFiltri() {
        filtroCombo.setItems(FXCollections.observableArrayList(
                "Tutti", "Passati", "Futuri", "Non valutati"
        ));
        filtroCombo.setValue("Tutti");
        filtroCombo.setOnAction(e -> applicaFiltro());
    }

    private void applicaFiltro() {
        String filtro = filtroCombo.getValue();
        if (filtro == null) return;

        ObservableList<TurnoConDettagli> filtrati = turniMaster;

        if ("Passati".equals(filtro)) {
            filtrati = turniMaster.filtered(t -> "Passato".equals(t.getStato()));
        } else if ("Futuri".equals(filtro)) {
            filtrati = turniMaster.filtered(t ->
                    "Futuro".equals(t.getStato()) || "Oggi".equals(t.getStato()));
        } else if ("Non valutati".equals(filtro)) {
            filtrati = turniMaster.filtered(t ->
                    "Passato".equals(t.getStato()) && !t.isValutato());
        }

        turniTable.setItems(filtrati);
    }


    private void inizializzaTabella() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colAssegnato.setCellValueFactory(new PropertyValueFactory<>("nomeUtente"));
        colStato.setCellValueFactory(new PropertyValueFactory<>("stato"));
        colStato.setStyle("-fx-alignment: CENTER;");


        colValutazione.setCellValueFactory(new PropertyValueFactory<>("valutazione"));
        colValutazione.setCellFactory(col -> new TableCell<TurnoConDettagli, Number>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.doubleValue() < 0) {
                    setText("");
                } else {
                    setText(String.format("%.1f / 5,0", item.doubleValue()));
                }
            }
        });
        colValutazione.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold; -fx-text-fill: green;");

        colAzioni.setCellFactory(col -> new TableCell<TurnoConDettagli, Void>() {

            private final Button btnValuta = new Button("Valuta");
            private final Button btnCambia = new Button("Cambia");

            {

                btnValuta.getStyleClass().add("table-action-primary");
                btnCambia.getStyleClass().add("table-action-secondary");

                btnValuta.setOnAction(e -> {
                    TurnoConDettagli turno = getTableRow().getItem();
                    if (turno != null) {
                        mostraDialogValuta(turno);
                    }
                });

                btnCambia.setOnAction(e -> {
                    TurnoConDettagli turno = getTableRow().getItem();
                    if (turno != null) {
                        mostraDialogCambiaUtente(turno);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    TurnoConDettagli turno = getTableRow().getItem();
                    if (turno == null) {
                        setGraphic(null);
                        return;
                    }

                    HBox box = new HBox(8);
                    box.setAlignment(Pos.CENTER);

                    if ("Passato".equals(turno.getStato()) && !turno.isValutato() && utenteCorrente.getId()!=turno.getIdUtente()) {
                        box.getChildren().add(btnValuta);
                    }

                    if (isAdmin && "Futuro".equals(turno.getStato())) {
                        box.getChildren().add(btnCambia);
                    }

                    setGraphic(box.getChildren().isEmpty() ? null : box);
                }
            }
        });

        turniTable.setItems(FXCollections.observableArrayList());
    }

    private void verificaCicloECaricaTurni() {
        cicloAttivo = pulizieService.getCiclo(utenteCorrente.getIdCasa());

        if (cicloAttivo == null) {
            noCicloBox.setVisible(true);
            noCicloBox.setManaged(true);
            turniBox.setVisible(false);
            turniBox.setManaged(false);

            if (isAdmin) {
                btnEliminaCiclo.setVisible(false);
                btnEliminaCiclo.setManaged(false);
                btnCreaCiclo.setVisible(true);
                btnCreaCiclo.setManaged(true);
            }

        } else {
            noCicloBox.setVisible(false);
            noCicloBox.setManaged(false);
            turniBox.setVisible(true);
            turniBox.setManaged(true);

            btnCreaCiclo.setVisible(false);
            btnCreaCiclo.setManaged(false);

            if(isAdmin){
                btnEliminaCiclo.setVisible(true);
                btnEliminaCiclo.setManaged(true);
            }

            caricaTurni();
        }
    }


    private void caricaTurni() {
        List<TurnoConDettagli> dettagli = pulizieService.getTurniDettagliati(cicloAttivo.getIdCiclo());

        LocalDate oggi = LocalDate.now();
        for (TurnoConDettagli t : dettagli) {
            if (t.getData().isBefore(oggi)) {
                t.setStato("Passato");
            } else if (t.getData().isEqual(oggi)) {
                t.setStato("Oggi");
            } else {
                t.setStato("Futuro");
            }
        }

        turniMaster.setAll(dettagli);
        applicaFiltro();
    }

    private void mostraDialogValuta(TurnoConDettagli turno) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Valuta turno");
        dialog.setHeaderText("Valuta il turno di " + turno.getNomeUtente() +
                " del " + turno.getData());
        dialog.setContentText("Voto (1-5):");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(voto -> {
            try {
                short valutazione = Short.parseShort(voto);
                if (valutazione < 1 || valutazione > 5) {
                    utilitaGenerale.mostraErrore("Il voto deve essere tra 1 e 5.");
                    return;
                }

                Valutazione val = new Valutazione();
                val.setIdTurno(turno.getIdTurno());
                val.setIdUtentePulitore(turno.getIdUtente());
                val.setIdUtenteValutatore(utenteCorrente.getId());
                val.setVoto(valutazione);

                pulizieService.salvaValutazione(val);
                utilitaGenerale.mostraInfo("Valutazione salvata", "Grazie per il tuo feedback.");
                caricaTurni();

            } catch (NumberFormatException e) {
                utilitaGenerale.mostraErrore("Inserisci un numero valido.");
            }
        });
    }

    private void mostraDialogCambiaUtente(TurnoConDettagli turno){
        List<Utente> coinquilini = pulizieService.getUtentiCasa(utenteCorrente.getIdCasa());
        List<String> nomi = coinquilini.stream()
                .map(Utente::getUsername)
                .collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(turno.getNomeUtente(), nomi);
        dialog.setTitle("Cambia assegnazione");
        dialog.setHeaderText("Seleziona il nuovo responsabile per il turno del " + turno.getData());
        dialog.setContentText("Utente:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(nuovoNome -> {
            Optional<Utente> nuovoUtente = coinquilini.stream()
                    .filter(u -> u.getUsername().equals(nuovoNome))
                    .findFirst();

            if (nuovoUtente.isPresent()) {
                TurnoPulizie t = pulizieService.getTurno(turno.getIdTurno());
                t.setIdUtente(nuovoUtente.get().getId());
                pulizieService.aggiornaTurno(t);

                utilitaGenerale.mostraInfo("Turno aggiornato", "Il turno è stato riassegnato.");
                caricaTurni();
            }
        });
    }

    private void mostraDialogCreaCiclo() {
        Dialog <CicloPulizie> dialog = new Dialog<>();
        dialog.setTitle("Crea nuovo ciclo di pulizie");
        dialog.setHeaderText("Imposta i parametri del ciclo");

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(
                getClass().getResource("/it/unical/ingsw/cohabita/css/PulizieStyle.css").toExternalForm()
        );
        dialogPane.getStyleClass().add("ciclo-dialog");
        dialogPane.setPrefWidth(420);
        dialogPane.setPrefHeight(260);

        ButtonType confermaBtn = new ButtonType("Crea", ButtonBar.ButtonData.OK_DONE);
        dialogPane.getButtonTypes().addAll(confermaBtn, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(14);
        grid.setPadding(new Insets(20, 20, 10, 20));
        grid.getStyleClass().add("ciclo-grid");

        DatePicker dataInizio = new DatePicker(LocalDate.now());
        TextField cadenzaField = new TextField("7");
        TextField turniCadaunoField = new TextField("2");

        dataInizio.setPrefWidth(220);
        cadenzaField.setPrefWidth(80);
        turniCadaunoField.setPrefWidth(80);

        grid.add(new Label("Data inizio:"), 0, 0);
        grid.add(dataInizio, 1, 0);
        grid.add(new Label("Cadenza (giorni):"), 0, 1);
        grid.add(cadenzaField, 1, 1);
        grid.add(new Label("Turni per utente:"), 0, 2);
        grid.add(turniCadaunoField, 1, 2);

        dialogPane.setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confermaBtn) {
                try {
                    CicloPulizie ciclo = new CicloPulizie();
                    ciclo.setIdCasa(utenteCorrente.getIdCasa());
                    ciclo.setDataCiclo(dataInizio.getValue());
                    ciclo.setCadenza(Short.parseShort(cadenzaField.getText()));
                    ciclo.setTurniCadauno(Short.parseShort(turniCadaunoField.getText()));
                    return ciclo;
                } catch (Exception e) {
                    utilitaGenerale.mostraErrore("Dati non validi.");
                    return null;
                }
            }
            return null;
        });

        Optional<CicloPulizie> result = dialog.showAndWait();
        result.ifPresent(ciclo -> {
            pulizieService.creaCiclo(ciclo);
            utilitaGenerale.mostraInfo("Ciclo creato", "Ora puoi generare e visualizzare i turni.");
            verificaCicloECaricaTurni();
        });
    }

    private void eliminaCiclo(){
        Alert conferma = new Alert(Alert.AlertType.CONFIRMATION);
        conferma.setTitle("Conferma eliminazione ciclo");
        conferma.setHeaderText("Vuoi davvero eliminare il ciclo di pulizie presente?");
        conferma.setContentText("Eliminando il ciclo di pulizie presente perderai anche tutti i turni ancora da effettuare");

        Optional<ButtonType> risultato = conferma.showAndWait();

        if (risultato.isPresent() && risultato.get() == ButtonType.OK) {
            pulizieService.cancellaCiclo(utenteCorrente.getIdCasa(),cicloAttivo.getIdCiclo());
            utilitaGenerale.mostraInfo("Ciclo eliminato", "Hai eliminato il ciclo presente, adesso potrai crearne uno nuovo");
            SceneNavigator.navigateTo("PulizieView.fxml");
        }
    }



}