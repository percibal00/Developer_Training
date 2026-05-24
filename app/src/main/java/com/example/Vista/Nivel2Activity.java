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

public class Nivel2Activity extends AppCompatActivity {
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
        setContentView(R.layout.nivel2);
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
            Intent intent = new Intent(this, Nivel3Activity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
            finish();
        });
    }

    private void cargarPreguntasFallback() {
        listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Pregunta("if (x > 0) { ___ }", "x++", "Condicionales"));
        listaPreguntas.add(new Pregunta("boolean isJava = ___;", "true", "Booleanos"));
        listaPreguntas.add(new Pregunta("int[] nums = {1, 2, ___};", "3", "Arrays"));
        listaPreguntas.add(new Pregunta("public ___ void main...", "static", "Modificadores"));
        listaPreguntas.add(new Pregunta("Scanner sc = new ___(System.in);", "Scanner", "Input"));
    }

        private void verificarRespuesta(int checkedId) {
        if (listaPreguntas == null || indicePregunta >= listaPreguntas.size()) return;
        RadioButton selectedRb = findViewById(checkedId);
        if (selectedRb == null) return;
        String respuestaSeleccionada = selectedRb.getText().toString();
        Pregunta p = listaPreguntas.get(indicePregunta);
        if (respuestaSeleccionada.contains(p.getRespuesta())) {
            aciertos = 1;
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
            tvChallengeDesc.setText("Nivel 2 - Completa:");
            tvCodeSnippet.setText(p.getPregunta());
            rgOptions.clearCheck();
            
            List<String> opciones = new ArrayList<>();
            opciones.add(p.getRespuesta());
            
            switch (p.getTipo()) {
                case "Condicionales":
                    opciones.add("break"); opciones.add("return"); opciones.add("continue"); opciones.add("exit");
                    break;
                case "Booleanos":
                    opciones.add("false"); opciones.add("1"); opciones.add("0"); opciones.add("null");
                    break;
                case "Arrays":
                    opciones.add("0"); opciones.add("4"); opciones.add("x"); opciones.add("nums");
                    break;
                case "Modificadores":
                    opciones.add("final"); opciones.add("public"); opciones.add("void"); opciones.add("class");
                    break;
                case "Input":
                    opciones.add("System"); opciones.add("Input"); opciones.add("Reader"); opciones.add("Print");
                    break;
                default:
                    opciones.add("opA"); opciones.add("opB"); opciones.add("opC"); opciones.add("opD");
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
            stats.setNivelesCompletados(aprobado ? "Nivel 2" : "Nivel 2 (Fallido)");
            stats.setUltimoNivelJugado("Nivel 2");
            stats.setRachas(aprobado ? 1 : 0);
            new Thread(() -> estadisticaControlador.actualizarProgreso(stats)).start();
        }
        if (aprobado) {
            btnNextLevel.setVisibility(View.VISIBLE);
            rgOptions.setEnabled(false);
            Toast.makeText(this, "¡Nivel 2 superado! Haz clic en el botón para avanzar.", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Nivel 2 fallido. Volviendo al menú...", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            }, 2000);
        }
    }
}

