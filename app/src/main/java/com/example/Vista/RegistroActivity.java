package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developerTraining.R;

public class RegistroActivity extends AppCompatActivity {

    private TextView tvBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializar el enlace para volver al login
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        tvBackToLogin.setOnClickListener(v -> {
            Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}