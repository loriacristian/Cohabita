package it.unical.ingsw.cohabita.ui.utility;

import javafx.scene.control.Alert;

public class utilitaGenerale {

    public static void mostraAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
