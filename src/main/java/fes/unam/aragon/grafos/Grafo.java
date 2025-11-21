package fes.unam.aragon.grafos;

public class Grafo <E> {
    private final int verticesMaximos = 100;
    private Vertice<E> listaVertices [];
    private int matrizAdyacencia[][];
    private int numeroVertices;

    public Grafo(){
        numeroVertices = 0;
        listaVertices = new Vertice[verticesMaximos];
        matrizAdyacencia = new int[verticesMaximos][verticesMaximos];
        for(int j = 0; j<verticesMaximos; j++){
            for(int i = 0; i < verticesMaximos; i++){
                matrizAdyacencia[j][i] = 0;
            }
        }
    }

    public void añadirVertice(E valor){
        listaVertices[numeroVertices++] = new Vertice<>(valor);
    }

    public void añadirConexion(int inicio, int fin)
    {
        matrizAdyacencia[inicio][fin] = 1;
        matrizAdyacencia[fin][inicio] = 1;
    }
}
