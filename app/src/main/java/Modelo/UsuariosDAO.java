package Modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UsuariosDAO {

    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    // El constructor recibe el contexto para inicializar la conexión a la base de datos
    public UsuariosDAO(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    // Abre la base de datos en modo escritura
    public void abrir() {
        db = dbHelper.getWritableDatabase();
    }

    // Cierra la base de datos
    public void cerrar() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    /**
     * COMPROBAR REGISTRO
     * Inserta un nuevo usuario en la base de datos.
     */
    public boolean registrarUsuario(Usuarios usuario) {
        abrir();
        ContentValues valores = new ContentValues();
        valores.put("NOMBRE", usuario.getNombre());
        valores.put("EMAIL", usuario.getEmail());
        valores.put("CONTRASEÑA", usuario.getContrasena());
        valores.put("EDAD", usuario.getEdad());
        valores.put("FECHA_REGISTRO", usuario.getFechaRegistro());

        long resultado = db.insert("Usuarios", null, valores);
        cerrar();

        return resultado != -1;
    }

    /**
     * COMPROBAR INICIO DE SESIÓN
     * Busca si existe un usuario con el email y contraseña introducidos.
     */
    public Usuarios comprobarLogin(String email, String contrasena) {
        abrir();
        Usuarios usuarioLogueado = null;

        String[] columnas = {"ID_USUARIO", "NOMBRE", "EMAIL", "EDAD", "FECHA_REGISTRO"};
        String seleccion = "EMAIL = ? AND CONTRASEÑA = ?";
        String[] seleccionArgs = {email, contrasena};

        Cursor cursor = db.query(
                "Usuarios",
                columnas,
                seleccion,
                seleccionArgs,
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            usuarioLogueado = new Usuarios();
            usuarioLogueado.setIdUsuario(cursor.getInt(cursor.getColumnIndexOrThrow("ID_USUARIO")));
            usuarioLogueado.setNombre(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE")));
            usuarioLogueado.setEmail(cursor.getString(cursor.getColumnIndexOrThrow("EMAIL")));
            usuarioLogueado.setEdad(cursor.getInt(cursor.getColumnIndexOrThrow("EDAD")));
            usuarioLogueado.setFechaRegistro(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_REGISTRO")));
        }

        if (cursor != null) {
            cursor.close();
        }
        cerrar();

        return usuarioLogueado;
    }
}
