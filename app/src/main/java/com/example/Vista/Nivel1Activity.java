package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    private TextView tvTimer, tvChallengeName, tvChallengeDesc, tvCodeSnippet;
    private RadioGroup rgOptions;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private Button btnFinish;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 45000;

    private PreguntaControlador preguntaControlador;
    private EstadisticaControlador estadisticaControlador;
    
    private int idUsuario;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta = 0;
    private int aciertos = 0;
    private boolean isFinishingGame = false;

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
        tvCodeSnippet = findViewById(R.id.tv_code_snippet);
        rgOptions = findViewById(R.id.rg_options);
        rb1 = findViewById(R.id.rb_option1);
        rb2 = findViewById(R.id.rb_option2);
        rb3 = findViewById(R.id.rb_option3);
        rb4 = findViewById(R.id.rb_option4);
        rb5 = findViewById(R.id.rb_option5);
        btnFinish = findViewById(R.id.btn_finish);

        // 3. Cargar preguntas
        new Thread(() -> {
            listaPreguntas = preguntaControlador.obtenerPreguntas();
            runOnUiThread(() -> {
                if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
                    mostrarPregunta();
                }
            });
        }).start();

        // 4. Iniciar el cronómetro
        startTimer();

        // 5. Configurar lógica de respuesta
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) {
                verificarRespuesta(checkedId);
            }
        });

        btnFinish.setOnClickListener(v -> finishGame());
    }

    private void verificarRespuesta(int checkedId) {
        if (listaPreguntas == null || indicePregunta >= listaPreguntas.size()) return;

        RadioButton selectedRb = findViewById(checkedId);
        String respuestaSeleccionada = selectedRb.getText().toString();
        Pregunta p = listaPreguntas.get(indicePregunta);

        if (respuestaSeleccionada.contains(p.getRespuesta())) {
            aciertos = 1;
            Toast.makeText(this, "¡Respuesta Correcta!", Toast.LENGTH_SHORT).show();
        } else {
            aciertos = 0;
            Toast.makeText(this, "Respuesta Incorrecta", Toast.LENGTH_SHORT).show();
        }

        // Bloquear opciones tras responder
        for (int i = 0; i < rgOptions.getChildCount(); i++) {
            rgOptions.getChildAt(i).setEnabled(false);
        }

        // Pequeño delay antes de cerrar automáticamente o permitir finalizar
        new android.os.Handler().postDelayed(this::finishGame, 1000);
    }

    private void mostrarPregunta() {
        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Pregunta p = listaPreguntas.get(indicePregunta);
            tvChallengeName.setText(p.getTipo());
            tvCodeSnippet.setText(p.getPregunta());
            
            rb1.setText(p.getRespuesta());
            rb2.setText("Opcion B (Falsa)");
            rb3.setText("Opcion C (Falsa)");
            rb4.setText("Opcion D (Falsa)");
            rb5.setText("Opcion E (Falsa)");
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
                finishGame();
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", seconds));
    }

    private void finishGame() {
        if (isFinishingGame) return;
        isFinishingGame = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados(aciertos > 0 ? "Nivel 1 Superado" : "Nivel 1 Fallido");
            stats.setUltimoNivelJugado("Nivel 1");
            stats.setRachas(aciertos);
            stats.setTiempoTotalJuego((int) (45000 - timeLeftInMillis) / 1000);
            
            // Guardar en MySQL
            new Thread(() -> {
                estadisticaControlador.actualizarProgreso(stats);
                runOnUiThread(() -> {
                    Toast.makeText(this, "Progreso guardado en historial", Toast.LENGTH_SHORT).show();
                    finish();
                });
            }).start();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
