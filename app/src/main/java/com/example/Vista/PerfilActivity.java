package com.example.Vista;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import Controlador.EstadisticaControlador;
import Modelo.Estadisticas;

public class PerfilActivity extends AppCompatActivity {

    private TextView tvDatosHistorial;
    private LinearLayout layoutHistorialItems;
    private EstadisticaControlador estadisticaControlador;
    private int idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil);

        // 1. Inicializar vistas
        tvDatosHistorial = findViewById(R.id.tv_datos_historial);
        layoutHistorialItems = findViewById(R.id.layout_historial_items);
        Button btnVolverMenu = findViewById(R.id.btn_volver_menu);

        // 2. Obtener datos del Intent
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        // 3. Inicializar controlador
        estadisticaControlador = new EstadisticaControlador(this);

        // 4. Cargar Historial/Estadísticas
        cargarEstadisticas();

        // Botón para volver al menú
        btnVolverMenu.setOnClickListener(v -> finish());
    }

    private void cargarEstadisticas() {
        // Limpiar historial previo antes de cargar
        layoutHistorialItems.removeAllViews();

        if (idUsuario == -1) {
            mostrarSinDatos();
            return;
        }

        new Thread(() -> {
            Estadisticas stats = estadisticaControlador.verEstadisticasUsuario(String.valueOf(idUsuario));
            runOnUiThread(() -> {
                // Si no hay estadísticas o los niveles están vacíos, mostramos "Sin datos"
                if (stats == null || stats.getNivelesCompletados() == null || stats.getNivelesCompletados().isEmpty()) {
                    mostrarSinDatos();
                } else {
                    actualizarUI(stats);
                }
            });
        }).start();
    }

    private void mostrarSinDatos() {
        tvDatosHistorial.setText("No hay partidas realizadas todavía.\n¡Empieza a jugar para ver tu progreso!");
        layoutHistorialItems.removeAllViews();
        
        TextView tvVacio = new TextView(this);
        tvVacio.setText("Tu historial está vacío.");
        tvVacio.setTextColor(getResources().getColor(R.color.text_subtitle, getTheme()));
        tvVacio.setTextSize(14);
        tvVacio.setPadding(0, 20, 0, 0);
        layoutHistorialItems.addView(tvVacio);
    }

    private void actualizarUI(Estadisticas stats) {
        String info = "Niveles completados: " + stats.getNivelesCompletados() + 
                     "\nRachas: " + stats.getRachas() + 
                     "\nTiempo total: " + stats.getTiempoTotalJuego() + "s" +
                     "\nÚltimo nivel: " + stats.getUltimoNivelJugado();
        tvDatosHistorial.setText(info);
        
        // Si hay stats, podemos limpiar el historial hardcoded o mostrar algo real
        layoutHistorialItems.removeAllViews();
        TextView tvItem = new TextView(this);
        tvItem.setText("● Nivel completado: " + stats.getUltimoNivelJugado());
        tvItem.setTextColor(getResources().getColor(R.color.victory_green, getTheme()));
        tvItem.setPadding(0, 10, 0, 10);
        layoutHistorialItems.addView(tvItem);
    }
}
