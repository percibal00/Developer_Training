package Modelo;

public class Niveles {

    private String idNiveles;
    private String seccion;
    private int numeroNivel;

    public Niveles() {}

    public Niveles(String idNiveles, String seccion, int numeroNivel) {
        this.idNiveles = idNiveles;
        this.seccion = seccion;
        this.numeroNivel = numeroNivel;
    }

    public String getIdNiveles() {
        return idNiveles;
    }

    public void setIdNiveles(String idNiveles) {
        this.idNiveles = idNiveles;
    }

    public String getSeccion() {
        return seccion;
    }

    public void setSeccion(String seccion) {
        this.seccion = seccion;
    }

    public int getNumeroNivel() {
        return numeroNivel;
    }

    public void setNumeroNivel(int numeroNivel) {
        this.numeroNivel = numeroNivel;
    }

    @Override
    public String toString() {
        return "Niveles{" +
                "idNiveles='" + idNiveles + '\'' +
                ", seccion='" + seccion + '\'' +
                ", numeroNivel=" + numeroNivel +
                '}';
    }
}
