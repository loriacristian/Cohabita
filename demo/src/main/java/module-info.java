module it.unical.demo {
    requires javafx.controls;
    requires javafx.fxml;


    opens it.unical.ingsw.cohabita to javafx.fxml;
    exports it.unical.ingsw.cohabita;
}