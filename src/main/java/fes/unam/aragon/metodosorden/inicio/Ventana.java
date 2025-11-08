package fes.unam.aragon.metodosorden.inicio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fes.unam.aragon.metodosorden.controlador.Controlador;

import java.io.IOException;

public class Ventana extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fes/unam/aragon/metodosorden/view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Metodos de ordenamiento");
        stage.setScene(scene);
        stage.show();
    }

}