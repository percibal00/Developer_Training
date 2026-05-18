package Modelo;

public class Estadisticas {

    private String idEstadisticas;
    private String idUsuario;
    private String nivelesCompletados;
    private int tiempoTotalJuego;
    private int rachas;
    private String ultimoNivelJugado;

    public String getIdEstadisticas() {
        return idEstadisticas;
    }

    public void setIdEstadisticas(String idEstadisticas) {
        this.idEstadisticas = idEstadisticas;
    }

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

    public int getRachas() {
        return rachas;
    }

    public void setRachas(int rachas) {
        this.rachas = rachas;
    }

    public String getUltimoNivelJugado() {
        return ultimoNivelJugado;
    }

    public void setUltimoNivelJugado(String ultimoNivelJugado) {
        this.ultimoNivelJugado = ultimoNivelJugado;
    }

    @Override
    public String toString() {
        return "Estadisticas{" +
                "idEstadisticas='" + idEstadisticas + '\'' +
                ", idUsuario='" + idUsuario + '\'' +
                ", nivelesCompletados='" + nivelesCompletados + '\'' +
                ", tiempoTotalJuego=" + tiempoTotalJuego +
                ", rachas=" + rachas +
                ", ultimoNivelJugado='" + ultimoNivelJugado + '\'' +
                '}';
    }
}
