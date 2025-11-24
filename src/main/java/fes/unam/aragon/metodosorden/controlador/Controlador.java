package fes.unam.aragon.metodosorden.controlador;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
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
    int numeroDatos=20;
    XYChart.Data<String, Number> primero =null;
    XYChart.Data<String, Number> segundo =null;


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
        btnOrdenar.setDisable(true);
        btnLista.setDisable(true);
        String selecciono = bxSelector.getValue();
        ordenarDatos(selecciono);

    }


    private void ordenarDatos(String metodo){
        switch (metodo){
            case "Burbuja":
                XYChart.Series<String, Number> series=chGrafica.getData().get(0);
                Task<Void> task =burbujaTask(series);

                Thread t =new Thread(task);
                t.setDaemon(true);
                t.start();
                break;
            case "Insercion":
                XYChart.Series<String, Number> series2=chGrafica.getData().get(0);
                Task<Void> taskIns =insercionTask(series2);

                Thread ts =new Thread(taskIns);
                ts.setDaemon(true);
                ts.start();
                break;
            case "Sacudida":
                XYChart.Series<String, Number> series1=chGrafica.getData().get(0);
                Task<Void> taskSac =sacudidaTask(series1);

                Thread ti =new Thread(taskSac);
                ti.setDaemon(true);
                ti.start();
                break;
            case "Quicksort":
                break;
            case "Seleccion":
                XYChart.Series<String, Number> series3=chGrafica.getData().get(0);
                Task<Void> taskSel =seleccionTask(series3);

                Thread tse =new Thread(taskSel);
                tse.setDaemon(true);
                tse.start();
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
        for (int i = 1; i <=n ; i++) {
            int al=rnd.nextInt(50);
            series.getData().add(new XYChart.Data<>(String.valueOf(i),al));
        }
        return series;
    }
    private Task<Void> burbujaTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                for (int i = data.size()-1 ; i >0 ; i--) {
                    for (int j = 0; j < i; j++) {
                        primero= data.get(j);
                        segundo= data.get(j+1);

                        Platform.runLater(()->{
                            primero.getNode().setStyle("-fx-bar-fill: red;");
                            segundo.getNode().setStyle("-fx-bar-fill: orange;");
                        });
                        Thread.sleep(tiempoRetardo);

                        double va=primero.getYValue().doubleValue();
                        double vb=segundo.getYValue().doubleValue();
                        if (va>vb){
                            Platform.runLater(()->{
                                Number tmp=primero.getYValue();
                                primero.setYValue(segundo.getYValue());
                                segundo.setYValue(tmp);
                            });
                        }
                        Platform.runLater(()->{
                            primero.getNode().setStyle("");
                            segundo.getNode().setStyle("");
                        });
                        Thread.sleep((tiempoRetardo));
                    }
                }
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }



    private Task<Void> sacudidaTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                int izq= 0;
                int der= numeroDatos-1;
                boolean intercambio=true;
                while (intercambio){
                    intercambio=false;
                    //r-l  <-
                    for (int i=der;i>izq;i--){
                        primero=data.get(i);
                        segundo=data.get(i-1);
                        Platform.runLater(()->{
                            primero.getNode().setStyle("-fx-bar-fill: red;");
                            segundo.getNode().setStyle("-fx-bar-fill: orange;");
                        });
                        Thread.sleep(tiempoRetardo);
                        double va=primero.getYValue().doubleValue();
                        double vb=segundo.getYValue().doubleValue();
                        if (vb>va){
                            Platform.runLater(()->{
                                Number tmp=primero.getYValue();
                                primero.setYValue(segundo.getYValue());
                                segundo.setYValue(tmp);
                            });
                            intercambio=true;
                        }
                        Platform.runLater(()->{
                            primero.getNode().setStyle("");
                            segundo.getNode().setStyle("");
                        });
                        Thread.sleep(tiempoRetardo);
                    }
                    izq++;
                    if (!intercambio){break;}
                    intercambio=false;
                    //L-R  -->
                    for (int i = izq; i <der; i++) {
                        primero=data.get(i);
                        segundo=data.get(i+1);
                        Platform.runLater(()->{
                            primero.getNode().setStyle("-fx-bar-fill: red;");
                            segundo.getNode().setStyle("-fx-bar-fill: orange;");
                        });
                        Thread.sleep(tiempoRetardo);
                        double va=primero.getYValue().doubleValue();
                        double vb=segundo.getYValue().doubleValue();
                        if (va>vb){
                            Platform.runLater(()->{
                                Number tmp=primero.getYValue();
                                primero.setYValue(segundo.getYValue());
                                segundo.setYValue(tmp);
                            });
                            intercambio=true;
                        }
                        Platform.runLater(()->{
                            primero.getNode().setStyle("");
                            segundo.getNode().setStyle("");
                        });
                        Thread.sleep(tiempoRetardo);
                    }
                    der--;
                }
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }



    private Task<Void> insercionTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                boolean noHayIntercambio;
                int comparaciones=0;
                for (int pasada = 1 ; pasada<data.size() ; pasada++) {
                     int j=pasada;
                    noHayIntercambio=true;

                    while ( j>0){
                        comparaciones++;
                        primero= data.get(j);
                        segundo= data.get(j-1);

                        Platform.runLater(()->{
                            primero.getNode().setStyle("-fx-bar-fill: red;");
                            segundo.getNode().setStyle("-fx-bar-fill: orange;");
                        });
                        Thread.sleep(tiempoRetardo);

                        double va=primero.getYValue().doubleValue();
                        double vb=segundo.getYValue().doubleValue();
                        if (va<=vb){
                            Platform.runLater(()->{
                                Number tmp=primero.getYValue();
                                primero.setYValue(segundo.getYValue());
                                segundo.setYValue(tmp);
                            });
                            Thread.sleep(tiempoRetardo);
                        }
                        else {
                            Platform.runLater(()->{
                                primero.getNode().setStyle("");
                                segundo.getNode().setStyle("");
                            });
                            break;
                        }
                        Platform.runLater(()->{
                            primero.getNode().setStyle("");
                            segundo.getNode().setStyle("");
                        });
                        j--;
                    }

                }
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }

    private Task<Void> seleccionTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                int out, in, min;
                for (out = 0 ; out < data.size()-1 ; out++) {
                    min=out;
                    for (in = out+1; in < data.size(); in++) {
                        primero= data.get(in);
                        segundo= data.get(min);

                        Platform.runLater(()->{
                            primero.getNode().setStyle("-fx-bar-fill: red;");
                            segundo.getNode().setStyle("-fx-bar-fill: orange;");
                        });
                        Thread.sleep(tiempoRetardo);

                        double va=primero.getYValue().doubleValue();
                        double vb=segundo.getYValue().doubleValue();
                        if (va<vb){
                            min=in;
                            Platform.runLater(()->{
                                primero.getNode().setStyle("");
                                segundo.getNode().setStyle("");
                            });
                        }
                        Thread.sleep((tiempoRetardo));
                    }
                    int finalOut = out;
                    int finalMin = min;
                    Platform.runLater(()->{
                        XYChart.Data<String, Number> salida =null;
                        XYChart.Data<String, Number> minimo =null;

                        salida=data.get(finalOut);
                        minimo=data.get(finalMin);

                        Number tmp=salida.getYValue();
                        primero.setYValue(segundo.getYValue());
                        segundo.setYValue(tmp);
                    });
                    Platform.runLater(()->{
                        primero.getNode().setStyle("");
                        segundo.getNode().setStyle("");
                    });
                }
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }
}
