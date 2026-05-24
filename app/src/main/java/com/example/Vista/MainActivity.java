package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.developertraining.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Controlador.UsuarioControlador;
import Modelo.Usuarios;

public class MainActivity extends AppCompatActivity {

    //Atributos
    private TextInputEditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private UsuarioControlador usuarioControlador;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.iniciosesion);
        
        // Ajustar el padding para el diseño EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Inicializar el Controlador
        usuarioControlador = new UsuarioControlador();

        // 2. Referenciar los elementos de la vista (UI)
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etConfirm);
        btnLogin = findViewById(R.id.btnLogin);
        tvRegister = findViewById(R.id.tvRegister);

        // 3. Configurar el evento del botón de Login
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String pass = etPassword.getText().toString().trim();

            if (email.isEmpty() || pass.isEmpty()) {
                Toast.makeText(this, "Por favor, rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!usuarioControlador.validarEmail(email)) {
                Toast.makeText(this, "Email no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Deshabilitar botón para evitar múltiples clics
            btnLogin.setEnabled(false);

            // Ejecutar el login en un hilo de fondo (MySQL requiere red)
            executorService.execute(() -> {
                Usuarios usuario = usuarioControlador.login(email, pass);

                // Volver al hilo principal para actualizar la UI
                runOnUiThread(() -> {
                    btnLogin.setEnabled(true);
                    if (usuario != null) {
                        Toast.makeText(this, "Bienvenido " + usuario.getNombre(), Toast.LENGTH_SHORT).show();
                        
                        // Ir a la pantalla de Menú
                        Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                        intent.putExtra("NOMBRE_USUARIO", usuario.getNombre());
                        startActivity(intent);
                        finish(); 
                    } else {
                        Toast.makeText(this, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show();
                    }
                });
            });
        });

        // 4. Configurar el enlace a la pantalla de registro
        tvRegister.setOnClickListener(v -> {
            Toast.makeText(this, "Ir a pantalla de registro...", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, RegistroActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
