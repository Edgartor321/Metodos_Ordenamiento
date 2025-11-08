module fes.unam.aragon.metodosorden {
    requires javafx.controls;
    requires javafx.fxml;
    requires Herramientas;

    opens fes.unam.aragon.metodosorden to javafx.fxml;
    exports fes.unam.aragon.metodosorden;
    exports fes.unam.aragon.metodosorden.controlador;
    opens fes.unam.aragon.metodosorden.controlador to javafx.fxml;
    exports fes.unam.aragon.metodosorden.inicio;
    opens fes.unam.aragon.metodosorden.inicio to javafx.fxml;
}