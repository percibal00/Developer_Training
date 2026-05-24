package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.Locale;

import Controlador.PreguntaControlador;
import Controlador.EstadisticaControlador;

public class Nivel1Activity extends AppCompatActivity {

    private TextView tvTimer, tvChallengeName, tvChallengeDesc;
    private Button btnPrevious, btnNext, btnFinish;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 45000; // 45 segundos iniciales

    private PreguntaControlador preguntaControlador;
    private EstadisticaControlador estadisticaControlador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel1);

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

        // 3. Iniciar el cronómetro
        startTimer();

        // 4. Configurar botones
        btnPrevious.setOnClickListener(v -> {
            Toast.makeText(this, "Cargando reto anterior...", Toast.LENGTH_SHORT).show();
        });

        btnNext.setOnClickListener(v -> {
            Toast.makeText(this, "Siguiente reto cargado", Toast.LENGTH_SHORT).show();
        });

        btnFinish.setOnClickListener(v -> {
            finishGame();
        });
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
                Toast.makeText(Nivel1Activity.this, "¡Tiempo agotado!", Toast.LENGTH_LONG).show();
                finishGame();
            }
        }.start();
    }

    private void updateCountDownText() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        tvTimer.setText(timeLeftFormatted);
    }

    private void finishGame() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        Toast.makeText(this, "Nivel completado. Guardando estadísticas...", Toast.LENGTH_SHORT).show();
        
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
