package com.example.Vista;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import Controlador.PreguntaControlador;
import Controlador.EstadisticaControlador;
import Modelo.Pregunta;
import Modelo.Estadisticas;

public class Nivel1Activity extends AppCompatActivity {

    private TextView tvTimer, tvChallengeName, tvChallengeDesc;
    private LinearLayout layoutAnswers;
    private Button btnPrevious, btnNext, btnFinish;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 45000;

    private PreguntaControlador preguntaControlador;
    private EstadisticaControlador estadisticaControlador;
    private int idUsuario;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel1);

        // 1. Recibir datos
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        // 2. Inicializar Controladores
        preguntaControlador = new PreguntaControlador(this);
        estadisticaControlador = new EstadisticaControlador(this);

        // 3. Vincular vistas
        tvTimer = findViewById(R.id.tv_timer);
        tvChallengeName = findViewById(R.id.tv_challenge_name);
        tvChallengeDesc = findViewById(R.id.tv_challenge_desc);
        layoutAnswers = findViewById(R.id.layout_answers);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);

        // 4. Cargar preguntas desde MySQL
        cargarPreguntas();

        // 5. Iniciar el cronómetro
        startTimer();

        // 6. Configurar botones de navegación
        btnPrevious.setOnClickListener(v -> {
            if (indicePregunta > 0) {
                indicePregunta--;
                actualizarPreguntaEnPantalla();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (listaPreguntas != null && indicePregunta < listaPreguntas.size() - 1) {
                indicePregunta++;
                actualizarPreguntaEnPantalla();
            }
        });

        btnFinish.setOnClickListener(v -> finishGame());
    }

    private void cargarPreguntas() {
        new Thread(() -> {
            listaPreguntas = preguntaControlador.obtenerPreguntas();
            runOnUiThread(() -> {
                if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
                    actualizarPreguntaEnPantalla();
                } else {
                    tvChallengeName.setText("Sin conexión");
                    tvChallengeDesc.setText("No se han podido cargar las preguntas.");
                }
            });
        }).start();
    }

    private void actualizarPreguntaEnPantalla() {
        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Pregunta p = listaPreguntas.get(indicePregunta);
            tvChallengeName.setText(p.getTipo());
            tvChallengeDesc.setText(p.getPregunta());

            // Limpiar respuestas anteriores
            layoutAnswers.removeAllViews();

            // Crear opciones (Correcta + 2 falsas de otras preguntas)
            List<String> opciones = new ArrayList<>();
            opciones.add(p.getRespuesta()); // La correcta

            // Añadir un par de opciones falsas para que haya juego
            if (listaPreguntas.size() > 2) {
                for (Pregunta extra : listaPreguntas) {
                    if (!extra.getRespuesta().equals(p.getRespuesta()) && opciones.size() < 3) {
                        opciones.add(extra.getRespuesta());
                    }
                }
            } else {
                opciones.add("Opción incorrecta A");
                opciones.add("Opción incorrecta B");
            }

            Collections.shuffle(opciones); // Mezclar para que la correcta no sea siempre la misma

            // Crear botones para cada opción
            for (String opcion : opciones) {
                Button btnOpcion = new Button(this);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, 
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 10, 0, 10);
                btnOpcion.setLayoutParams(params);
                btnOpcion.setText(opcion);
                btnOpcion.setBackgroundResource(R.drawable.btn_secondary); // Estilo azulado
                btnOpcion.setTextColor(Color.WHITE);
                btnOpcion.setAllCaps(false);

                btnOpcion.setOnClickListener(v -> comprobarRespuesta(opcion, p.getRespuesta(), btnOpcion));

                layoutAnswers.addView(btnOpcion);
            }
        }
    }

    private void comprobarRespuesta(String seleccion, String correcta, Button btnPulsado) {
        if (seleccion.equals(correcta)) {
            btnPulsado.setBackgroundColor(Color.GREEN);
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            btnPulsado.setBackgroundColor(Color.RED);
            Toast.makeText(this, "Incorrecto. Inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                tvTimer.setText("00:00");
                finishGame();
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", seconds));
    }

    private void finishGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados("1");
            stats.setUltimoNivelJugado("Nivel 1");
            stats.setRachas(1);
            stats.setTiempoTotalJuego(45 - (int)(timeLeftInMillis/1000));
            
            estadisticaControlador.actualizarProgreso(stats);
        }

        Toast.makeText(this, "Partida finalizada", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
