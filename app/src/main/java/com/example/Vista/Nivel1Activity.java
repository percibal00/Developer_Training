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
        rgOptions.setEnabled(false); // Deshabilitar hasta que carguen las preguntas
        rb1 = findViewById(R.id.rb_option1);
        rb2 = findViewById(R.id.rb_option2);
        rb3 = findViewById(R.id.rb_option3);
        rb4 = findViewById(R.id.rb_option4);
        rb5 = findViewById(R.id.rb_option5);
        btnFinish = findViewById(R.id.btn_finish);

        // 3. Cargar preguntas reales desde MySQL
        new Thread(() -> {
            listaPreguntas = preguntaControlador.obtenerPreguntas();
            runOnUiThread(() -> {
                if (listaPreguntas == null || listaPreguntas.isEmpty()) {
                    // Cargar preguntas de respaldo si falla la BD
                    listaPreguntas = new java.util.ArrayList<>();
                    listaPreguntas.add(new Modelo.Pregunta("System.out.println(\"___\");", "Hello World", "Básico"));
                    listaPreguntas.add(new Modelo.Pregunta("int x = ___; // Asigna 5", "5", "Variables"));
                    listaPreguntas.add(new Modelo.Pregunta("// Este es un ___ de una línea", "comentario", "Sintaxis"));
                    listaPreguntas.add(new Modelo.Pregunta("String nombre = \"___\";", "Juan", "Strings"));
                    listaPreguntas.add(new Modelo.Pregunta("double pi = ___;", "3.14", "Tipos de datos"));
                    Toast.makeText(this, "Usando preguntas locales (Error BD)", Toast.LENGTH_SHORT).show();
                }
                
                java.util.Collections.shuffle(listaPreguntas);
                rgOptions.setEnabled(true);
                mostrarPregunta();
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

        btnFinish.setOnClickListener(v -> {
            finishGame();
        });
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

        // Bloquear opciones tras responder
        rgOptions.setEnabled(false);
        for (int i = 0; i < rgOptions.getChildCount(); i++) {
            rgOptions.getChildAt(i).setEnabled(false);
        }

        // Finalizar lógica de la pregunta única
        new android.os.Handler().postDelayed(this::finishGame, 500);
    }

    private void mostrarPregunta() {
        if (listaPreguntas != null && !listaPreguntas.isEmpty()) {
            Pregunta p = listaPreguntas.get(indicePregunta);
            tvChallengeName.setText(p.getTipo());
            tvChallengeDesc.setText("Completa el código:");
            tvCodeSnippet.setText(p.getPregunta());
            
            rgOptions.clearCheck();

            // Opciones dinámicas según la pregunta
            List<String> opciones = new java.util.ArrayList<>();
            opciones.add(p.getRespuesta());
            
            // Generar opciones falsas según el tipo de pregunta
            if (p.getTipo().equals("Básico")) {
                opciones.add("print"); opciones.add("echo"); opciones.add("log"); opciones.add("display");
            } else if (p.getTipo().equals("Variables")) {
                opciones.add("cinco"); opciones.add("five"); opciones.add("0.5"); opciones.add("x");
            } else if (p.getTipo().equals("Sintaxis")) {
                opciones.add("código"); opciones.add("texto"); opciones.add("valor"); opciones.add("error");
            } else {
                opciones.add("opción A"); opciones.add("opción B"); opciones.add("opción C"); opciones.add("opción D");
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

    private boolean isFinishingGame = false;

    private void finishGame() {
        if (isFinishingGame) return;
        isFinishingGame = true;

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        boolean aprobado = aciertos >= 1;
        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados(aprobado ? "Nivel 1" : "Nivel 1 (Fallido)");
            stats.setUltimoNivelJugado("Nivel 1");
            stats.setRachas(aprobado ? 1 : 0);
            new Thread(() -> estadisticaControlador.actualizarProgreso(stats)).start();
        }

        if (aprobado) {
            // Mostrar botón para pasar al siguiente nivel
            btnFinish.setText("Siguiente Nivel");
            btnFinish.setVisibility(View.VISIBLE);
            rgOptions.setEnabled(false);
            Toast.makeText(this, "¡Nivel 1 superado! Pulsa el botón para continuar.", Toast.LENGTH_LONG).show();
            
            btnFinish.setOnClickListener(v -> {
                Intent intent = new Intent(this, Nivel2Activity.class);
                intent.putExtra("ID_USUARIO", idUsuario);
                startActivity(intent);
                finish();
            });
        } else {
            Toast.makeText(this, "Nivel 1 fallido. Volviendo al menú...", Toast.LENGTH_LONG).show();
            new android.os.Handler().postDelayed(() -> {
                startActivity(new Intent(this, MenuActivity.class));
                finish();
            }, 2000);
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
