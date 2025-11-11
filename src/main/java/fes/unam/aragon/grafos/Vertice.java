package fes.unam.aragon.grafos;

public class Vertice<E> {
    public E valor;
    public boolean visitado;

    public Vertice(E valor){
        this.valor = valor;
        this.visitado = false;
    }

}
