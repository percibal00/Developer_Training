package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.List;
import java.util.Locale;

import Controlador.PreguntaControlador;
import Controlador.EstadisticaControlador;
import Modelo.Pregunta;
import Modelo.Estadisticas;

public class Nivel1Activity extends AppCompatActivity {

    private TextView tvTimer, tvChallengeName, tvChallengeDesc;
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

        // Recibir ID de usuario
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);

        // 1. Inicializar Controladores
        preguntaControlador = new PreguntaControlador(this);
        estadisticaControlador = new EstadisticaControlador(this);

        // 2. Vincular vistas
        tvTimer = findViewById(R.id.tv_timer);
        tvChallengeName = findViewById(R.id.tv_challenge_name);
        tvChallengeDesc = findViewById(R.id.tv_challenge_desc);
        btnPrevious = findViewById(R.id.btn_previous);
        btnNext = findViewById(R.id.btn_next);
        btnFinish = findViewById(R.id.btn_finish);

        // 3. Cargar preguntas reales desde MySQL
        new Thread(() -> {
            listaPreguntas = preguntaControlador.obtenerPreguntas();
            runOnUiThread(() -> {
                if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
                    mostrarPregunta();
                } else {
                    tvChallengeName.setText("No hay preguntas");
                    tvChallengeDesc.setText("Asegúrate de tener conexión a la base de datos.");
                }
            });
        }).start();

        // 4. Iniciar el cronómetro
        startTimer();

        // 5. Configurar botones
        btnPrevious.setOnClickListener(v -> {
            if (indicePregunta > 0) {
                indicePregunta--;
                mostrarPregunta();
            }
        });

        btnNext.setOnClickListener(v -> {
            if (listaPreguntas != null && indicePregunta < listaPreguntas.size() - 1) {
                indicePregunta++;
                mostrarPregunta();
            }
        });

        btnFinish.setOnClickListener(v -> {
            finishGame();
        });
    }

    private void mostrarPregunta() {
        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Pregunta p = listaPreguntas.get(indicePregunta);
            tvChallengeName.setText(p.getTipo());
            tvChallengeDesc.setText(p.getPregunta());
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
        String timeLeftFormatted = String.format(Locale.getDefault(), "00:%02d", seconds);
        tvTimer.setText(timeLeftFormatted);
    }

    private void finishGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Guardar estadísticas si el usuario está identificado
        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados("Nivel 1");
            stats.setUltimoNivelJugado("Nivel 1");
            stats.setRachas(1);
            
            // Llamar al controlador para guardar en MySQL
            estadisticaControlador.actualizarProgreso(stats);
            Toast.makeText(this, "Progreso guardado", Toast.LENGTH_SHORT).show();
        }
        
        // Volver al menú
        Intent intent = new Intent(Nivel1Activity.this, MenuActivity.class);
        startActivity(intent);
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
