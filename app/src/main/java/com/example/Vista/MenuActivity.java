package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

public class MenuActivity extends AppCompatActivity {

    private int idUsuario;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Recibir datos del Intent
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");

        if (nombreUsuario != null) {
            Toast.makeText(this, "Hola, " + nombreUsuario, Toast.LENGTH_SHORT).show();
        }

        // Botón Jugar
        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, Nivel1Activity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
        });

        // Botón Perfil
        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario);
            startActivity(intent);
        });

        // Botón Ajustes
        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, AjustesActivity.class);
            startActivity(intent);
        });
    }
}
