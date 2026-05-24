package Controlador;

import android.content.Context;
import java.util.List;
import Modelo.Pregunta;
import Modelo.PreguntaDAO;

public class PreguntaControlador {
    private PreguntaDAO preguntaDAO;

    public PreguntaControlador(Context context) {
        preguntaDAO = new PreguntaDAO(context);
    }

    public List<Pregunta> obtenerPreguntas() {
        return preguntaDAO.obtenerPreguntas();
    }
}
