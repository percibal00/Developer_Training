package Controlador;

import android.content.Context;
import java.util.List;
import Modelo.Niveles;
import Modelo.NivelesDAO;

public class NivelControlador {
    private NivelesDAO nivelesDAO;

    public NivelControlador(Context context) {
        this.nivelesDAO = new NivelesDAO();
    }

    public List<Niveles> listarTodos() {
        return nivelesDAO.obtenerTodosLosNiveles();
    }

    public List<Niveles> listarPorSeccion(String seccion) {
        return nivelesDAO.obtenerNivelesPorSeccion(seccion);
    }
}
