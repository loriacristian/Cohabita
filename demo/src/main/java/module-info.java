module it.unical.ingsw.cohabita {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires jbcrypt;

    opens it.unical.ingsw.cohabita to javafx.fxml;
    opens it.unical.ingsw.cohabita.ui.view to javafx.fxml;

    opens it.unical.ingsw.cohabita.ui.model to javafx.base;

    exports it.unical.ingsw.cohabita;
    exports it.unical.ingsw.cohabita.ui.view;
}
