package fes.unam.aragon.metodosorden.controlador;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import unam.fes.aragon.dinamicas.listasimple.ListaSimple;
import unam.fes.aragon.orden.Burbuja;

import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;


public class Controlador implements Initializable {
    ListaSimple<Integer> listaSimple=new ListaSimple<>();
    int tiempoRetardo= 40;
    int numeroDatos=40;

    @FXML
    private Button btnLista;

    @FXML
    private Button btnOrdenar;

    @FXML
    private ChoiceBox<String> bxSelector;


    @FXML
    private BarChart<String, Number> chGrafica;

    private String[] opciones={"Burbuja","Sacudida","Seleccion","Insercion","Quicksort"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bxSelector.getItems().addAll(opciones);
        bxSelector.setValue("Burbuja");
        chGrafica.setAnimated(false);
        XYChart.Series<String,Number> series=new XYChart.Series<String,Number>();
        series= generarAleatorios(numeroDatos);
        chGrafica.getData().add(series);

    }


    @FXML
    void btnLista(ActionEvent event) {
        //System.out.println(generarAleatorios(40));
        chGrafica.getData().clear();
        XYChart.Series<String,Number> series=new XYChart.Series<String,Number>();
        series=generarAleatorios(numeroDatos);
        chGrafica.getData().add(series);


    }

    @FXML
    void btnOrdenar(ActionEvent event) {
        String selecciono = bxSelector.getValue();
        ordenarDatos(selecciono);

    }


//    private void mostrarDatosEnGrafica(){
//        chGrafica.getData().clear();
//        XYChart.Series<String, Number> series = new XYChart.Series<>();
//
//        for (int i = 0; i < listaSimple.getLongitud(); i++) {
//            String nombre = ""+listaSimple.obtenerNodo(i);
//            Number valor = listaSimple.obtenerNodo(i);
//            series.getData().add(new XYChart.Data<>(nombre, valor));
//        }
//        chGrafica.getData().add(series);
//    }
//    private void aleatorios(){
//        Random random = new Random();
//        listaSimple.vaciarLista();
//        for (int i = 0; i < numeroDatos; i++) {
//            Integer num = random.nextInt(100);
//            listaSimple.agregarEnCola(num);
//        }
//        listaSimple.imprimirLista();
//    }
    private void ordenarDatos(String metodo){
        switch (metodo){
            case "Burbuja":
                Burbuja<Integer> burbuja=new Burbuja(listaSimple);
                burbuja.ordenarAscendente();
                listaSimple=burbuja.getLista();
                listaSimple.imprimirLista();
                break;
            case "Insercion":
                break;
            case "Sacudida":
                break;
            case "Quicksort":
                break;
            case "Seleccion":
                break;
            default:
                mostrarError("Debes seleccionar un metodo de ordnamiento");
        }
    }
    private void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setHeaderText("Error");
        alerta.setContentText(mensaje+"\nRevise bien los datos ingresados");
        alerta.showAndWait();
    }

    private XYChart.Series<String, Number>generarAleatorios(int n){
        XYChart.Series<String, Number> series=new XYChart.Series<>();
        Random rnd=new Random();
        for (int i = 1; i <n ; i++) {
            int al=rnd.nextInt(50);
            series.getData().add(new XYChart.Data<>(String.valueOf(al),al));
        }
        return series;
    }
}
