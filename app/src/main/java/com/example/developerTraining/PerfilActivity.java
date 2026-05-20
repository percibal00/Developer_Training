package com.example.developerTraining;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        Button btnHistorial = findViewById(R.id.Historial);
        btnHistorial.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, HistorialActivity.class);
            startActivity(intent);
        });
    }
}
