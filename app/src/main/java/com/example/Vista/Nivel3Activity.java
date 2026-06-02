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
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import Controlador.PreguntaControlador;
import Controlador.EstadisticaControlador;
import Modelo.Pregunta;
import Modelo.Estadisticas;

public class Nivel3Activity extends AppCompatActivity {
    private TextView tvTimer, tvChallengeName, tvChallengeDesc, tvCodeSnippet;
    private RadioGroup rgOptions;
    private RadioButton rb1, rb2, rb3, rb4, rb5;
    private Button btnNextLevel;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 45000;
    private PreguntaControlador preguntaControlador;
    private EstadisticaControlador estadisticaControlador;
    private int idUsuario;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta = 0;
    private int aciertos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel3);
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        preguntaControlador = new PreguntaControlador(this);
        estadisticaControlador = new EstadisticaControlador(this);
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
        btnNextLevel = findViewById(R.id.btn_next_level);

        new Thread(() -> {
            listaPreguntas = preguntaControlador.obtenerPreguntas();
            runOnUiThread(() -> {
                if (listaPreguntas == null || listaPreguntas.isEmpty()) {
                    cargarPreguntasFallback();
                    Toast.makeText(this, "Usando preguntas locales (Error BD)", Toast.LENGTH_SHORT).show();
                }
                java.util.Collections.shuffle(listaPreguntas);
                mostrarPregunta();
            });
        }).start();

        startTimer();
        rgOptions.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId != -1) verificarRespuesta(checkedId);
        });

        btnNextLevel.setOnClickListener(v -> {
            Intent intent = new Intent(this, Nivel4Activity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        });
    }

    private void cargarPreguntasFallback() {
        listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Pregunta("for (int i=0; i<5; ___) { }", "i++", "Bucles"));
        listaPreguntas.add(new Pregunta("while (___) { break; }", "true", "Bucles"));
        listaPreguntas.add(new Pregunta("do { } ___ (x < 10);", "while", "Bucles"));
        listaPreguntas.add(new Pregunta("for (String s : ___) { }", "lista", "Iteración"));
        listaPreguntas.add(new Pregunta("if (i == 3) ___", "continue", "Control"));
    }

        private void verificarRespuesta(int checkedId) {
        if (listaPreguntas == null || indicePregunta >= listaPreguntas.size()) return;
        RadioButton selectedRb = findViewById(checkedId);
        if (selectedRb == null) return;
        String respuestaSeleccionada = selectedRb.getText().toString();
        Pregunta p = listaPreguntas.get(indicePregunta);
        if (respuestaSeleccionada.contains(p.getRespuesta())) {
            aciertos = 1;
            Toast.makeText(this, "�Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            aciertos = 0;
        }
        rgOptions.setEnabled(false);
        for (int i = 0; i < rgOptions.getChildCount(); i++) {
            rgOptions.getChildAt(i).setEnabled(false);
        }
        new android.os.Handler().postDelayed(this::finishGame, 500);
    }

    private void mostrarPregunta() {
        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Pregunta p = listaPreguntas.get(indicePregunta);
            tvChallengeName.setText(p.getTipo());
            tvChallengeDesc.setText("Nivel 3 - Completa:");
            tvCodeSnippet.setText(p.getPregunta());
            rgOptions.clearCheck();
            
            List<String> opciones = new ArrayList<>();
            opciones.add(p.getRespuesta());
            
            switch (p.getTipo()) {
                case "Bucles":
                    opciones.add("i--"); opciones.add("false"); opciones.add("if"); opciones.add("for");
                    break;
                case "Iteración":
                    opciones.add("String"); opciones.add("int"); opciones.add("new"); opciones.add("array");
                    break;
                case "Control":
                    opciones.add("break"); opciones.add("stop"); opciones.add("exit"); opciones.add("return");
                    break;
                default:
                    opciones.add("op1"); opciones.add("op2"); opciones.add("op3"); opciones.add("op4");
                    break;
            }
            
            java.util.Collections.shuffle(opciones);
            rb1.setText(opciones.get(0));
            rb2.setText(opciones.get(1));
            rb3.setText(opciones.get(2));
            rb4.setText(opciones.get(3));
            rb5.setText(opciones.get(4));
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", seconds));
            }
            @Override public void onFinish() { finishGame(); }
        }.start();
    }

    private void finishGame() {
        if (countDownTimer != null) countDownTimer.cancel();
        boolean aprobado = aciertos >= 1;
        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados(aprobado ? "Nivel 3" : "Nivel 3 (Fallido)");
            stats.setUltimoNivelJugado("Nivel 3");
            stats.setRachas(aprobado ? 1 : 0);
            new Thread(() -> estadisticaControlador.actualizarProgreso(stats)).start();
        }
        if (aprobado) {
            btnNextLevel.setVisibility(View.VISIBLE);
            rgOptions.setEnabled(false);
        } else {
            new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            }, 2000);
        }
    }
}

