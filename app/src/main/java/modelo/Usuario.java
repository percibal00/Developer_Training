package modelo;

public class Usuario {

    //Atributos
    private String nombre;
    private String password;
    private int puntuacion;

    //Constructor
    public Usuario(String nombre, String password) {
        this.nombre = nombre;
        this.password = password;
        this.puntuacion = 0;
    }

    //Getters y setters
    public String getNombre() {
        return nombre;
    }

    public String getPassword() {
        return password;
    }

    public int getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(int puntuacion) {
        this.puntuacion = puntuacion;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //ToString
    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", password='" + password + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }
}