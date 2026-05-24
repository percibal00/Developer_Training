package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Modelo.Usuarios;
import Modelo.UsuariosDAO;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etName, etEmail, etPassword, etAge;
    private Button btnRegister;
    private TextView tvBackToLogin;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);

        // Inicializar vistas
        etName = findViewById(R.id.etRegName);
        etEmail = findViewById(R.id.etRegEmail);
        etPassword = findViewById(R.id.etRegPassword);
        etAge = findViewById(R.id.etRegAge);
        btnRegister = findViewById(R.id.btnRegister);
        tvBackToLogin = findViewById(R.id.tvBackToLogin);

        // Evento botón Registrarse
        btnRegister.setOnClickListener(v -> {
            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();
            String ageStr = etAge.getText().toString().trim();

            if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || ageStr.isEmpty()) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            int age;
            try {
                age = Integer.parseInt(ageStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "La edad debe ser un número", Toast.LENGTH_SHORT).show();
                return;
            }

            Usuarios nuevoUsuario = new Usuarios();
            nuevoUsuario.setNombre(name);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setContrasena(pass);
            nuevoUsuario.setEdad(age);

            btnRegister.setEnabled(false);

            executorService.execute(() -> {
                UsuariosDAO usuariosDAO = new UsuariosDAO(this);
                boolean exito = usuariosDAO.registrarUsuario(nuevoUsuario);

                runOnUiThread(() -> {
                    btnRegister.setEnabled(true);
                    if (exito) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistroActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Vuelve al login
                    } else {
                        Toast.makeText(this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });

        tvBackToLogin.setOnClickListener(v -> {
            finish(); // Simplemente cerramos para volver a la pantalla anterior (Login)
        });
    }
}