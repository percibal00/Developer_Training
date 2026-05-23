package com.example.Vista;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.developerTraining.R;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        // Botón para volver al menú
        Button btnVolverMenu = findViewById(R.id.btn_volver_menu);
        btnVolverMenu.setOnClickListener(v -> {
            // Cerramos esta actividad para volver a MenuActivity
            finish();
        });
    }
}
