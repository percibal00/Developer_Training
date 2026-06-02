package com.example.Vista;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.List;

import Controlador.EstadisticaControlador;
import Modelo.Estadisticas;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvDatosHistorial, tvWelcome;
    private LinearLayout layoutHistorialItems;
    private EstadisticaControlador estadisticaControlador;
    private int idUsuario;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        // 1. Inicializar vistas
        tvWelcome = findViewById(R.id.tv_welcome);
        tvDatosHistorial = findViewById(R.id.tv_datos_historial);
        layoutHistorialItems = findViewById(R.id.layout_historial_items);
        Button btnVolverMenu = findViewById(R.id.btn_volver_menu);

        // 2. Obtener datos del Intent
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");

        // Mostrar nombre si lo tenemos
        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            tvWelcome.setText("Perfil de " + nombreUsuario);
        }

        // Limpiar placeholders iniciales
        tvDatosHistorial.setText("Cargando estadísticas...");
        layoutHistorialItems.removeAllViews();

        // 3. Inicializar controlador
        estadisticaControlador = new EstadisticaControlador(this);

        // 4. Cargar Historial completo
        cargarHistorial();

        // Botón para volver al menú
        btnVolverMenu.setOnClickListener(v -> finish());
    }

    private void cargarHistorial() {
        if (idUsuario == -1) {
            mostrarSinDatos();
            return;
        }

        new Thread(() -> {
            // Obtenemos la lista de todas las partidas
            List<Estadisticas> historial = estadisticaControlador.obtenerHistorialCompleto(String.valueOf(idUsuario));
            
            runOnUiThread(() -> {
                if (historial == null || historial.isEmpty()) {
                    mostrarSinDatos();
                } else {
                    actualizarUI(historial);
                }
            });
        }).start();
    }

    private void mostrarSinDatos() {
        tvDatosHistorial.setText("No hay partidas realizadas todavía.");
        layoutHistorialItems.removeAllViews();
        
        TextView tvVacio = new TextView(this);
        tvVacio.setText("El historial está vacío.");
        tvVacio.setTextColor(Color.GRAY);
        tvVacio.setPadding(0, 20, 0, 0);
        layoutHistorialItems.addView(tvVacio);
    }

    private void actualizarUI(List<Estadisticas> historial) {
        // Limpiar contenedor
        layoutHistorialItems.removeAllViews();

        // Al haber añadido ORDER BY DESC en el DAO, la posición 0 es la más reciente
        Estadisticas ultima = historial.get(0);
        
        int victorias = 0;
        for(Estadisticas e : historial) {
            if (e.getNivelesCompletados().contains("Superado")) victorias++;
        }

        String info = "Entrenamientos realizados: " + historial.size() + 
                     "\nÉxitos: " + victorias +
                     "\nÚltima sesión: " + (ultima.getNivelesCompletados().contains("Superado") ? "Éxito" : "Fallo") +
                     "\nTiempo: " + ultima.getTiempoTotalJuego() + "s";
        tvDatosHistorial.setText(info);

        // Llenar el historial (mostramos los últimos 3 para no saturar)
        int count = 0;
        for (Estadisticas entry : historial) {
            if (count >= 3) break;

            TextView tvItem = new TextView(this);
            String resultado = entry.getNivelesCompletados();
            
            tvItem.setText("● " + resultado + " (" + entry.getTiempoTotalJuego() + "s)");
            
            if (resultado.contains("Fallido")) {
                tvItem.setTextColor(Color.parseColor("#F44336")); // Rojo
            } else {
                tvItem.setTextColor(Color.parseColor("#4CAF50")); // Verde
            }
            
            tvItem.setPadding(0, 10, 0, 10);
            tvItem.setTextSize(14);
            layoutHistorialItems.addView(tvItem);
            count++;
        }
    }
}
