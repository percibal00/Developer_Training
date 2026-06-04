package com.example.Vista;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import Controlador.EstadisticaControlador;
import Modelo.Pregunta;
import Modelo.Estadisticas;

public class JuegoActivity extends AppCompatActivity {

    private TextView tvTimer, tvChallengeName, tvChallengeDesc, tvCodeSnippet;
    private RadioGroup rgOptions;
    private final RadioButton[] rbOptions = new RadioButton[5];
    private Button btnNext;
    
    private EstadisticaControlador estadisticaControlador;
    
    private int idUsuario;
    private List<Pregunta> listaPreguntas;
    private int indicePregunta = 0;
    private int aciertosTotal = 0;
    private long timeLeftInMillis = 45000;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nivel1);

        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        estadisticaControlador = new EstadisticaControlador(this);

        tvTimer = findViewById(R.id.tv_timer);
        tvChallengeName = findViewById(R.id.tv_challenge_name);
        tvChallengeDesc = findViewById(R.id.tv_challenge_desc);
        tvCodeSnippet = findViewById(R.id.tv_code_snippet);
        rgOptions = findViewById(R.id.rg_options);
        rbOptions[0] = findViewById(R.id.rb_option1);
        rbOptions[1] = findViewById(R.id.rb_option2);
        rbOptions[2] = findViewById(R.id.rb_option3);
        rbOptions[3] = findViewById(R.id.rb_option4);
        rbOptions[4] = findViewById(R.id.rb_option5);
        btnNext = findViewById(R.id.btn_finish);

        btnNext.setText("Siguiente");
        cargarPreguntas();
        btnNext.setOnClickListener(v -> procesarRespuesta());
    }

    private void cargarPreguntas() {
        listaPreguntas = new ArrayList<>();
        listaPreguntas.add(new Pregunta("System.out.println(\"___\");", "Hello World", "Básico"));
        listaPreguntas.add(new Pregunta("if (x > 0) { ___ }", "x++", "Condicionales"));
        listaPreguntas.add(new Pregunta("for (int i=0; i<5; ___)", "i++", "Bucles"));
        listaPreguntas.add(new Pregunta("int[] nums = {1, 2, ___};", "3", "Arrays"));
        listaPreguntas.add(new Pregunta("public class ___ { }", "Main", "POO"));
        
        Collections.shuffle(listaPreguntas);
        mostrarPregunta();
        startTimer();
    }

    private void mostrarPregunta() {
        if (listaPreguntas == null || indicePregunta >= listaPreguntas.size()) {
            finalizarJuego();
            return;
        }

        Pregunta p = listaPreguntas.get(indicePregunta);
        tvChallengeName.setText(String.format(Locale.getDefault(), "Nivel %d", indicePregunta + 1));
        tvChallengeDesc.setText(p.getTipo());
        tvCodeSnippet.setText(p.getPregunta());
        rgOptions.clearCheck();

        List<String> opciones = generarOpciones(p);
        for (int i = 0; i < 5; i++) {
            rbOptions[i].setText(opciones.get(i));
        }
        
        if (indicePregunta == listaPreguntas.size() - 1) {
            btnNext.setText("Finalizar");
        }
    }

    private List<String> generarOpciones(Pregunta p) {
        List<String> ops = new ArrayList<>();
        ops.add(p.getRespuesta());
        switch (p.getTipo()) {
            case "Básico": ops.add("print"); ops.add("echo"); ops.add("log"); ops.add("display"); break;
            case "Condicionales": ops.add("break"); ops.add("return"); ops.add("continue"); ops.add("exit"); break;
            case "Bucles": ops.add("i--"); ops.add("false"); ops.add("if"); ops.add("for"); break;
            case "Arrays": ops.add("0"); ops.add("4"); ops.add("x"); ops.add("nums"); break;
            case "POO": ops.add("main"); ops.add("void"); ops.add("static"); ops.add("String"); break;
            default: ops.add("opA"); ops.add("opB"); ops.add("opC"); ops.add("opD"); break;
        }
        Collections.shuffle(ops);
        return ops;
    }

    private void procesarRespuesta() {
        int selectedId = rgOptions.getCheckedRadioButtonId();
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona una opción", Toast.LENGTH_SHORT).show();
            return;
        }

        RadioButton rb = findViewById(selectedId);
        if (rb.getText().toString().equals(listaPreguntas.get(indicePregunta).getRespuesta())) {
            aciertosTotal++;
        }

        indicePregunta++;
        if (indicePregunta < listaPreguntas.size()) {
            mostrarPregunta();
        } else {
            finalizarJuego();
        }
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                int seconds = (int) (timeLeftInMillis / 1000) % 60;
                tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", seconds));
            }
            @Override
            public void onFinish() { finalizarJuego(); }
        }.start();
    }

    private void finalizarJuego() {
        if (countDownTimer != null) countDownTimer.cancel();
        boolean aprobado = aciertosTotal >= 3;
        
        if (idUsuario != -1) {
            Estadisticas stats = new Estadisticas();
            stats.setIdUsuario(String.valueOf(idUsuario));
            stats.setNivelesCompletados(aprobado ? "Entrenamiento Superado" : "Entrenamiento Fallido");
            stats.setTiempoTotalJuego((int)((45000 - timeLeftInMillis)/1000));
            new Thread(() -> estadisticaControlador.actualizarProgreso(stats)).start();
        }

        Toast.makeText(this, (aprobado ? "¡Felicidades!" : "Sigue practicando.") + " Acertaste " + aciertosTotal + "/5", Toast.LENGTH_LONG).show();
        new android.os.Handler().postDelayed(this::finish, 3000);
    }
}
