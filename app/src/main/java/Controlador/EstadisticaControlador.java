package Controlador;

import android.content.Context;
import Modelo.Estadisticas;
import Modelo.EstadisticasDAO;

public class EstadisticaControlador {
    private EstadisticasDAO estadisticasDAO;

    public EstadisticaControlador(Context context) {
        this.estadisticasDAO = new EstadisticasDAO();
    }

    public void actualizarProgreso(Estadisticas stats) {
        estadisticasDAO.guardarEstadisticas(stats);
    }

    public Estadisticas verEstadisticasUsuario(String idUsuario) {
        return estadisticasDAO.obtenerEstadisticas(idUsuario);
    }
}
