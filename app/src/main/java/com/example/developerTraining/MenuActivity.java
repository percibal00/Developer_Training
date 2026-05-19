package com.example.developerTraining;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import Modelo.Usuarios;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Opcional: Si quieres mostrar el nombre del usuario
        String nombre = getIntent().getStringExtra("NOMBRE_USUARIO");
        // Aquí podrías buscar un TextView en menu.xml para poner el nombre
    }
}
