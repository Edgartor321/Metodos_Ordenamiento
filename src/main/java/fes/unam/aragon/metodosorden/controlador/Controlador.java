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

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;


public class Controlador implements Initializable {
    int tiempoRetardo= 40;
    int numeroDatos=30;
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

    //Esto sirve para el selector en el menu, más adelante.
    private String[] opciones={"Burbuja","Sacudida","Seleccion","Insercion","Quicksort", "Mezcla"};

    //Todo lo que este dentro de aquí se va a inicializar por defecto (el desplegable y los primeros datos aleatorios, para evitar errores)
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bxSelector.getItems().addAll(opciones);
        bxSelector.setValue("Burbuja");
        chGrafica.setAnimated(false);
        XYChart.Series<String,Number> series=new XYChart.Series<String,Number>();
        series= generarAleatorios(numeroDatos);
        chGrafica.getData().add(series);

    }


    //Genera nuevos datos aleatorios y los coloca en la gráfica de barras.
    @FXML
    void btnLista(ActionEvent event) {
        //System.out.println(generarAleatorios(40));
        chGrafica.getData().clear();
        XYChart.Series<String,Number> series=new XYChart.Series<String,Number>();
        series=generarAleatorios(numeroDatos);
        chGrafica.getData().add(series);


    }

    //Aquí se da la instrucción para iiciaizar el metodo seleccionado, se llama a la función ordenarDatos y con el desplegable y el SwCs se define que metodo se utiliza.
    @FXML
    void btnOrdenar(ActionEvent event) {
        btnOrdenar.setDisable(true);
        btnLista.setDisable(true);
        String selecciono = bxSelector.getValue();
        ordenarDatos(selecciono);

    }

//Metodo que cintiene el SwCs que obtiene la información de la grafica impresa, inicializa la tarea, el hilo y el demonio
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
                XYChart.Series<String, Number> series4=chGrafica.getData().get(0);
                Task<Void> taskqs =quicksortTask(series4);

                Thread tqs =new Thread(taskqs);
                tqs.setDaemon(true);
                tqs.start();
                break;
            case "Seleccion":
                XYChart.Series<String, Number> series3=chGrafica.getData().get(0);
                Task<Void> taskSel =seleccionTask(series3);

                Thread tse =new Thread(taskSel);
                tse.setDaemon(true);
                tse.start();
                break;
            case "Mezcla":
                XYChart.Series<String, Number> series5=chGrafica.getData().get(0);
                Task<Void> taskMS =MergeSortTask(series5);

                Thread tms =new Thread(taskMS);
                tms.setDaemon(true);
                tms.start();
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
//Genra valores aleatorios que se colocan en la gráfica de barras.
    private XYChart.Series<String, Number>generarAleatorios(int n){
        XYChart.Series<String, Number> series=new XYChart.Series<>();
        Random rnd=new Random();
        for (int i = 1; i <=n ; i++) {
            int al=rnd.nextInt(50);
            series.getData().add(new XYChart.Data<>(String.valueOf(i),al));
        }
        return series;
    }

    //Aquí vienen las cosas insanas, los metodos dentro de los tasks.
    /*Método burbuja:
    * Va de forma ascendente
    * Rojo: elemento que se desplaza (más grande sin ordenar)
    * */
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


    /*Método sacudida (shaker):
     * Va de forma bidireccional, empieza de der a izq, despues en la otra direccion, a la der se ponen los elementos más grandes.
     * Rojo: elemento que se desplaza (más grande o más pequenio sin ordenar)
     * */
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


    /*Método insercion (insertion):
     * Del lado izquieerdo se aglutinan los valores ya ordenados y a la derecha se toman los valores y se INSERTAN en la posición donde van.
     * Rojo: elementos ordenados
     * Naranja: Elementos sin ordenar
     * Amarillo: Elemento que se inserta
     * */
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

    /*Método seleccion:
     * Almacena valores ya ordenads a la izquierda, busca en el conjunto desordenado el valor mśs pequenio y lo coloca en la ultima poscion ordenada+1
     * Rojo: Comparaciones no aceptadas
     * Naranja: Comparaciones aceptadas
     * Verde: Elementos ordenados
     * */
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
                        }
                        Platform.runLater(()->{
                            primero.getNode().setStyle("");
                            segundo.getNode().setStyle("");
                        });
                        //Thread.sleep((tiempoRetardo));
                    }
                    int finalOut = out;
                    int finalMin = min;
                    Platform.runLater(()->{
                        XYChart.Data<String, Number> salida =data.get(finalOut);
                        XYChart.Data<String, Number> minimo =data.get(finalMin);

                        Number tmp=salida.getYValue();
                        salida.setYValue(minimo.getYValue());
                        minimo.setYValue(tmp);

                        salida.getNode().setStyle("-fx-bar-fill: green;");
                    });
                    Thread.sleep(tiempoRetardo);
                }
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }

    /*Método Quicksort (recursivo):
     * Aqui ponemos tres metodos diferentes, el que inicializa el ordenamiento, el metddo que hace las llamadas recursivas y el que hace las particiones y los intercambios, admenas contiene los cambios de color
     * Aqui busquen la explicación, no sé cómo hacerla, SUERTE
     * Rojo: Elemento comparado
     * Azul: Pivotes
     * Naranja: Elementos que se comparan
     * Verde: Elementos ordenados
     * */
    private Task<Void> quicksortTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                quicksortRec(data,0, (data.size()-1));
                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }
    private void quicksortRec(ObservableList<XYChart.Data<String, Number>> datos, int inicio, int fin) throws InterruptedException {
        if(inicio < fin){
            int indicePiv=particion(datos, inicio,fin);
            quicksortRec(datos,inicio,indicePiv-1);
            quicksortRec(datos, indicePiv+1, fin);
        }
    }

    private int particion(ObservableList<XYChart.Data<String, Number>> data, int inicio, int fin) throws InterruptedException {
        XYChart.Data<String,Number> piv= data.get(fin);

        Platform.runLater(()-> piv.getNode().setStyle("-fx-bar-fill: blue;"));
        Thread.sleep(tiempoRetardo);

        int i=inicio-1;
        for (int j = inicio; j < fin; j++) {
            XYChart.Data<String,Number> valAct= data.get(j);
            Platform.runLater(() -> valAct.getNode().setStyle("-fx-bar-fill: red;"));
            Thread.sleep(tiempoRetardo);

            double actualValue=valAct.getYValue().doubleValue();
            double pivValue=piv.getYValue().doubleValue();
            if(actualValue<=pivValue){
                i++;
                XYChart.Data<String,Number> menor= data.get(i);
                Platform.runLater(() -> {
                    menor.getNode().setStyle("-fx-bar-fill: orange;");
                });
                Thread.sleep(tiempoRetardo);

                Platform.runLater(() -> {
                    Number tmp = menor.getYValue();
                    menor.setYValue(valAct.getYValue());
                    valAct.setYValue(tmp);
                });
                Thread.sleep(tiempoRetardo);

                Platform.runLater(()->{
                    menor.getNode().setStyle("");
                });
            }
            Platform.runLater(()->valAct.getNode().setStyle(""));
        }

        XYChart.Data<String,Number> aux= data.get(i+1);
        Platform.runLater(() -> {
            aux.getNode().setStyle("-fx-bar-fill: orange;");
        });
        Thread.sleep(tiempoRetardo);

        Platform.runLater(() -> {
            Number tmp = aux.getYValue();
            aux.setYValue(piv.getYValue());
            piv.setYValue(tmp);
        });
        Thread.sleep(tiempoRetardo);

        Platform.runLater(() -> {
            aux.getNode().setStyle("-fx-bar-fill: green;");
            piv.getNode().setStyle("");
        });
        return i+1;
    }

    private Task<Void> MergeSortTask(XYChart.Series<String, Number>series){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                ObservableList<XYChart.Data<String, Number>> data = series.getData();
                mergeSortRec(data, 0, data.size()-1);

                Platform.runLater(()->{
                    btnLista.setDisable(false);
                    btnOrdenar.setDisable(false);
                });
                return null;
            }
        };
    }

    private void mergeSortRec(ObservableList<XYChart.Data<String, Number>> datos, int inicio, int fin) throws InterruptedException {
        if(inicio >= fin){
            return;   //evitamos un ciclado
        }
            int mitad=(inicio + fin)/2;
            mergeSortRec(datos,inicio,mitad);
            mergeSortRec(datos, mitad+1, fin);
            burbujaM(datos, inicio, fin);
            merge(datos,inicio,mitad,fin);

    }

    private void merge(ObservableList<XYChart.Data<String, Number>> datos, int inicio, int mitad, int fin) throws InterruptedException {
        List<Number> tmp = new ArrayList<>();

        int i = inicio;
        int j = mitad + 1;

        while (i <= mitad && j <= fin) {
            double a = datos.get(i).getYValue().doubleValue();
            double b = datos.get(j).getYValue().doubleValue();

            if (a <= b) {
                tmp.add(a);
                i++;
            } else {
                tmp.add(b);
                j++;
            }
        }

            while (i <= mitad) {
                tmp.add(datos.get(i).getYValue());
                i++;
            }
            while (j <= fin) {
                tmp.add(datos.get(j).getYValue());
                j++;
            }
            for (int k = 0; k < tmp.size(); k++) {
                Number valor = tmp.get(k);

                XYChart.Data<String, Number> d = datos.get(inicio + k);
                d.setYValue(valor);

                int finalK = inicio + k;
                Platform.runLater(() ->
                        datos.get(finalK).getNode().setStyle("-fx-bar-fill: blue;")
                );

                Thread.sleep(tiempoRetardo);
            }

        }
    private void burbujaM(ObservableList<XYChart.Data<String, Number>> datos,
                          int inicio, int fin) throws InterruptedException {

        for (int i = fin; i > inicio; i--) {
            for (int j = inicio; j < i; j++) {

                XYChart.Data<String, Number> a = datos.get(j);
                XYChart.Data<String, Number> b = datos.get(j + 1);

                Platform.runLater(() -> {
                    a.getNode().setStyle("-fx-bar-fill: red;");
                    b.getNode().setStyle("-fx-bar-fill: orange;");
                });
                Thread.sleep(tiempoRetardo);

                double va = a.getYValue().doubleValue();
                double vb = b.getYValue().doubleValue();

                if (va > vb) {
                    Number tmp = va;

                    Platform.runLater(() -> {
                        a.setYValue(vb);
                        b.setYValue(tmp);
                    });
                }

                Platform.runLater(() -> {
                    a.getNode().setStyle("");
                    b.getNode().setStyle("");
                });

                Thread.sleep(tiempoRetardo);
            }
        }
    }

}

