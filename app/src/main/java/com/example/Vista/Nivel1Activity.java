package com.example.Vista;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.Locale;

import Controlador.EstadisticaControlador;
import Modelo.Estadisticas;

public class Nivel1Activity extends AppCompatActivity {

    private TextView tvTimer, tvSelectionStatus;
    private Button btnFinish;
    private Button[] optionButtons;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 45000;

    private EstadisticaControlador estadisticaControlador;
    private int idUsuario;
    private String selectedAnswer = "";
    private final String correctOption = "extends";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel1);

        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        estadisticaControlador = new EstadisticaControlador(this);

        tvTimer = findViewById(R.id.tv_timer);
        tvSelectionStatus = findViewById(R.id.tv_selection_status);
        btnFinish = findViewById(R.id.btn_finish);

        setupOptions();
        startTimer();

        btnFinish.setOnClickListener(v -> checkAnswer());
    }

    private void setupOptions() {
        int[] optionIds = {R.id.option1, R.id.option2, R.id.option3, R.id.option4, R.id.option5};
        optionButtons = new Button[optionIds.length];

        for (int i = 0; i < optionIds.length; i++) {
            final Button btn = findViewById(optionIds[i]);
            optionButtons[i] = btn;
            btn.setOnClickListener(v -> {
                selectedAnswer = btn.getText().toString();
                tvSelectionStatus.setText("Seleccionado: " + selectedAnswer);
                highlightSelectedButton(btn);
            });
        }
    }

    private void highlightSelectedButton(Button selectedBtn) {
        for (Button btn : optionButtons) {
            if (btn == selectedBtn) {
                btn.setBackgroundResource(R.drawable.btn_jugar); // Usamos un color/fondo distinto para destacar
            } else {
                btn.setBackgroundResource(R.drawable.btn_secondary);
            }
        }
    }

    private void checkAnswer() {
        if (selectedAnswer.isEmpty()) {
            Toast.makeText(this, "Selecciona una opción primero", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean isCorrect = selectedAnswer.equals(correctOption);
        if (isCorrect) {
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrecto, intenta de nuevo", Toast.LENGTH_SHORT).show();
        }
        
        finishGame(isCorrect);
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
                finishGame(false);
            }
        }.start();
    }

    private void updateCountDownText() {
        int seconds = (int) (timeLeftInMillis / 1000) % 60;
        tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", seconds));
    }

    private void finishGame(boolean isCorrect) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados(isCorrect ? "Nivel 1 Completado" : "Nivel 1 Fallido");
            stats.setUltimoNivelJugado("Nivel 1");
            stats.setRachas(isCorrect ? 1 : 0);
            
            estadisticaControlador.actualizarProgreso(stats);
        }
        
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
