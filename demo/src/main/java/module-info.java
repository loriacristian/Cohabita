module it.unical.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens it.unical.ingsw.cohabita to javafx.fxml;
    exports it.unical.ingsw.cohabita;
}