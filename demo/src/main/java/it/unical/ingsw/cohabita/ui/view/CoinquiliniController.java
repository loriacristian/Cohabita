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
    @FXML private TableView<Utente> tabellaCoinquilini;
    @FXML private TableColumn<Utente, String> colNome;
    @FXML private TextField codiceInvitoField;

    private final UtenteService utenteService = UtenteService.defaultInstance();
    private Utente utenteCorrente;
    private final PulizieService pulizieService = PulizieService.defaultInstance();


    @FXML
    public void initialize() {
        utenteCorrente = SessioneCorrente.getUtenteCorrente();

        if (utenteCorrente == null) {
            SceneNavigator.navigateTo("LoginView.fxml");
            return;
        }

        colNome.setCellValueFactory(new PropertyValueFactory<>("username"));
        caricaCoinquilini();
        caricaCodiceInvito();

    }


    private void caricaCoinquilini() {
        List<Utente> lista = utenteService.getDaCasa(utenteCorrente.getIdCasa());
        tabellaCoinquilini.setItems(FXCollections.observableArrayList(lista));
    }

    private void caricaCodiceInvito(){
        Casa casa = pulizieService.getCasa(utenteCorrente.getIdCasa());
        if (casa != null) {
            codiceInvitoField.setText(casa.getCodiceInvito());
        }
    }


}