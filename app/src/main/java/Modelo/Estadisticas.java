package Modelo;

public class Estadisticas {

    // Atributos (Eliminado idEstadisticas)
    private String idUsuario;
    private String nivelesCompletados;
    private int tiempoTotalJuego;

    // Getters y setters
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNivelesCompletados() {
        return nivelesCompletados;
    }

    public void setNivelesCompletados(String nivelesCompletados) {
        this.nivelesCompletados = nivelesCompletados;
    }

    public int getTiempoTotalJuego() {
        return tiempoTotalJuego;
    }

    public void setTiempoTotalJuego(int tiempoTotalJuego) {
        this.tiempoTotalJuego = tiempoTotalJuego;
    }

    @Override
    public String toString() {
        return "Estadisticas{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nivelesCompletados='" + nivelesCompletados + '\'' +
                ", tiempoTotalJuego=" + tiempoTotalJuego +
                '}';
    }
}
