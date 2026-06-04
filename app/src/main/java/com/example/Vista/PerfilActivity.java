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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        tvWelcome = findViewById(R.id.tv_welcome);
        tvDatosHistorial = findViewById(R.id.tv_datos_historial);
        layoutHistorialItems = findViewById(R.id.layout_historial_items);
        Button btnVolverMenu = findViewById(R.id.btn_volver_menu);

        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        String nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");

        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            tvWelcome.setText(String.format("Perfil de %s", nombreUsuario));
        }

        tvDatosHistorial.setText("Cargando estadísticas...");
        layoutHistorialItems.removeAllViews();

        estadisticaControlador = new EstadisticaControlador(this);
        cargarHistorial();

        btnVolverMenu.setOnClickListener(v -> finish());
    }

    private void cargarHistorial() {
        if (idUsuario == -1) {
            mostrarSinDatos();
            return;
        }

        new Thread(() -> {
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
        layoutHistorialItems.removeAllViews();
        Estadisticas ultima = historial.get(0);
        
        int victorias = 0;
        for(Estadisticas e : historial) {
            if (e.getNivelesCompletados().contains("Superado")) victorias++;
        }

        tvDatosHistorial.setText(String.format(java.util.Locale.getDefault(),
                "Entrenamientos: %d\nÉxitos: %d\nÚltima: %s\nTiempo: %ds",
                historial.size(), victorias, 
                ultima.getNivelesCompletados().contains("Superado") ? "Éxito" : "Fallo",
                ultima.getTiempoTotalJuego()));

        int count = 0;
        for (Estadisticas entry : historial) {
            if (count >= 3) break;
            TextView tvItem = new TextView(this);
            String resultado = entry.getNivelesCompletados();
            tvItem.setText(String.format(java.util.Locale.getDefault(), "● %s (%ds)", resultado, entry.getTiempoTotalJuego()));
            tvItem.setTextColor(resultado.contains("Fallido") ? Color.parseColor("#F44336") : Color.parseColor("#4CAF50"));
            tvItem.setPadding(0, 10, 0, 10);
            tvItem.setTextSize(14);
            layoutHistorialItems.addView(tvItem);
            count++;
        }
    }
}
