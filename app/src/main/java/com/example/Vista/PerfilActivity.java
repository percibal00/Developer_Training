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

        // Mostrar resumen basado en la partida más reciente
        Estadisticas ultima = historial.get(0);
        String info = "Niveles superados: " + historial.size() + 
                     "\nÚltimo nivel: " + ultima.getUltimoNivelJugado() +
                     "\nTiempo última partida: " + ultima.getTiempoTotalJuego() + "s";
        tvDatosHistorial.setText(info);

        // Llenar el historial con todas las partidas
        for (Estadisticas entry : historial) {
            TextView tvItem = new TextView(this);
            String resultado = entry.getNivelesCompletados(); // Contiene "Nivel 1" o "Nivel 1 (Fallido)"
            
            tvItem.setText("● " + resultado + " - Tiempo: " + entry.getTiempoTotalJuego() + "s");
            
            // Color según si fue exitoso o fallido
            if (resultado.contains("Fallido")) {
                tvItem.setTextColor(Color.parseColor("#F44336")); // Rojo
            } else {
                tvItem.setTextColor(Color.parseColor("#4CAF50")); // Verde
            }
            
            tvItem.setPadding(0, 10, 0, 10);
            tvItem.setTextSize(14);
            layoutHistorialItems.addView(tvItem);
        }
    }
}
