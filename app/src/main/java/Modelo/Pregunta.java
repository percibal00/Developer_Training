package modelo;

public class Pregunta {

    // Atributos
    private String pregunta;
    private String respuesta;
    private String tipo;

    // Constructor
    public Pregunta(String pregunta, String respuesta, String tipo) {
        this.pregunta = pregunta;
        this.respuesta = respuesta;
        this.tipo = tipo;
    }

    // Getters
    public String getPregunta() {
        return pregunta;
    }

    public String getRespuesta() {
        return respuesta;
    }

    public String getTipo() {
        return tipo;
    }

    // Setters
    public void setPregunta(String pregunta) {
        this.pregunta = pregunta;
    }

    public void setRespuesta(String respuesta) {
        this.respuesta = respuesta;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}