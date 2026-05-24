package com.example.Vista;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
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
            Intent intent = new Intent(MenuActivity.this, Nivel1Activity.class);
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
        findViewById(R.id.btn_settings).setOnClickListener(this::showVolumePopup);
    }

    private void showVolumePopup(View anchorView) {
        View popupView = getLayoutInflater().inflate(R.layout.dialog_settings, (ViewGroup) anchorView.getParent(), false);
        
        // Ajustar el tamaño del layout para que quepa bien como popup
        PopupWindow popupWindow = new PopupWindow(popupView, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                ViewGroup.LayoutParams.WRAP_CONTENT, 
                true);

        SeekBar sbVolume = popupView.findViewById(R.id.sb_volume);

        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        if (audioManager != null && sbVolume != null) {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            
            sbVolume.setMax(maxVolume);
            sbVolume.setProgress(currentVolume);

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

        // Mostrar debajo del botón
        popupWindow.showAsDropDown(anchorView, 0, 10);
    }
}
