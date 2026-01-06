package it.unical.ingsw.cohabita.ui.view;

import it.unical.ingsw.cohabita.application.casa.CasaService;
import it.unical.ingsw.cohabita.domain.Casa;
import it.unical.ingsw.cohabita.ui.navigation.SceneNavigator;
import it.unical.ingsw.cohabita.ui.utility.utilitaGenerale;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CreaCasaController {

    @FXML
    private TextField nomeCasaField;
    @FXML
    private TextArea codiceInvitoArea;
    @FXML
    private Button creaButton;
    @FXML
    private Button tornaInvitoButton;

    private final CasaService casaService = new CasaService();

    @FXML
    private void initialize() {
        creaButton.setOnAction(e -> creaNuovaCasa());
        tornaInvitoButton.setOnAction(event -> tornaZonaInvito());
        codiceInvitoArea.setText("");
    }
    private void creaNuovaCasa() {
        String nomeCasa = nomeCasaField.getText();
        if (nomeCasa==null||nomeCasa.trim().isEmpty()) {
            utilitaGenerale.mostraErrore("Inserisci un nome per la casa.");
            return;
        }

        Casa casa= casaService.creaCasa(nomeCasa);

        if(casa!=null) {
            codiceInvitoArea.setText(casa.getCodiceInvito());
            utilitaGenerale.mostraInfo(
                    "Casa creata",
                    "Casa \"" + casa.getNomeCasa() +
                            "\" creata con successo.\nTi porteremo alla dashboard della casa.");
            SceneNavigator.navigateTo("CasaView.fxml");
        }else {
            utilitaGenerale.mostraErrore("Impossibile creare la casa.");
        }
    }

    private void tornaZonaInvito() {
        SceneNavigator.navigateTo("CodiceInvitoView.fxml");
    }

}
