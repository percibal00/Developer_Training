package com.example.Vista;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.developertraining.R;

public class MenuActivity extends AppCompatActivity {

    private int idUsuario;
    private String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        // Recibir datos del Intent
        idUsuario = getIntent().getIntExtra("ID_USUARIO", -1);
        nombreUsuario = getIntent().getStringExtra("NOMBRE_USUARIO");

        if (nombreUsuario != null) {
            Toast.makeText(this, "Hola, " + nombreUsuario, Toast.LENGTH_SHORT).show();
        }

        // Botón Jugar
        findViewById(R.id.button2).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, JuegoActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            startActivity(intent);
        });

        // Botón Perfil
        findViewById(R.id.button3).setOnClickListener(v -> {
            Intent intent = new Intent(MenuActivity.this, PerfilActivity.class);
            intent.putExtra("ID_USUARIO", idUsuario);
            intent.putExtra("NOMBRE_USUARIO", nombreUsuario);
            startActivity(intent);
        });

        // Botón Ajustes
        SeekBar sbVolume = findViewById(R.id.sb_volume);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        // Configurar el SeekBar con el volumen actual
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        sbVolume.setMax(maxVolume);
        sbVolume.setProgress(currentVolume);

        findViewById(R.id.btn_settings).setOnClickListener(v -> {
            if (sbVolume.getVisibility() == View.GONE) {
                sbVolume.setVisibility(View.VISIBLE);
            } else {
                sbVolume.setVisibility(View.GONE);
            }
        });

        sbVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }
}
