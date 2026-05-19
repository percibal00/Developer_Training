package Modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "developer_training.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla Usuarios
        String CREATE_USUARIOS_TABLE = "CREATE TABLE Usuarios (" +
                "ID_USUARIO INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOMBRE TEXT," +
                "EMAIL TEXT," +
                "CONTRASEÑA TEXT," +
                "EDAD INTEGER," +
                "FECHA_REGISTRO TEXT" +
                ")";
        db.execSQL(CREATE_USUARIOS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Usuarios");
        onCreate(db);
    }
}
