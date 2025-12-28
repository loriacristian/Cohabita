package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.casa.CasaService;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class CodiceInvitoController {

    @FXML
    private TextField codiceInvitoField;
    @FXML
    private Button uniscitiButton;
    @FXML
    private Button creaCasaButton;

    private final CasaService casaService=new CasaService();

    @FXML
    void initialize() {
        uniscitiButton.setOnAction(e -> uniscitiCasa());
        creaCasaButton.setOnAction(e -> vaiCreaCasa());
    }

     private void uniscitiCasa() {
        String codiceInvito=codiceInvitoField.getText();

        if(codiceInvito==null||codiceInvito.trim().isEmpty()){
            mostraAlert(Alert.AlertType.WARNING,
                    "Codice mancante",
                    "Inserisci un codice invito.");
            return;
        }

        boolean ok= casaService.accediCasa(codiceInvito);

        if(ok){
            mostraAlert(Alert.AlertType.INFORMATION,
                    "Benvenuto",
                    "Sei entrato correttamente nella casa.");
            SceneNavigator.navigateTo("HomeView.fxml");
        }else {
            mostraAlert(Alert.AlertType.ERROR,
                    "Codice non valido",
                    "Nessuna casa trovata con questo codice invito.");
        }
     }

    void vaiCreaCasa() {
        SceneNavigator.navigateTo("CreaCasaView.fxml");
    }

    private void mostraAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
