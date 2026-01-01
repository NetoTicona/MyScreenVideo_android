package com.example.my_screen_video.presentation.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import android.view.View;

public class SecondActivity extends AppCompatActivity {

    private ArrayList<String> hexColors;
    private ArrayList<Integer> durations;
    private int durationMs;
    private int totalCycles;

    private int currentCycle = 0;
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private Runnable runner;
    private android.widget.TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Pantalla completa */
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        // Mantiene la pantalla encendida mientras se muestran los colores
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        /* Layout vacío: solo fondo */
        android.widget.TextView tv = new android.widget.TextView(this);
        tv.setBackgroundColor(Color.BLACK);
        setContentView(tv);

        /* Extras */
        hexColors   = getIntent().getStringArrayListExtra("colors");
        durationMs  = getIntent().getIntExtra("duration", 1000);
        totalCycles = getIntent().getIntExtra("cycles", 1);
        durations   = getIntent().getIntegerArrayListExtra("durations");

        /* Runnable ciclico */
        runner = new Runnable() {
            @Override
            public void run() {
                if (hexColors != null && !hexColors.isEmpty()) {
                    // 1. Pintar el color actual
                    tv.setBackgroundColor(Color.parseColor(hexColors.get(currentIndex)));

                    // 2. Obtener la duración específica para ESTE color actual
                    int timeForThisColor = durations.get(currentIndex);

                    currentIndex++;

                    // 3. Control de Ciclos
                    if (currentIndex == hexColors.size()) {
                        currentIndex = 0;
                        currentCycle++;
                        if (currentCycle >= totalCycles) {
                            finish();
                            return;
                        }
                    }

                    // 4. Programar el siguiente cambio usando el tiempo dinámico
                    handler.postDelayed(this, timeForThisColor);
                }
            }
        };
        handler.post(runner);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        // Habilita el modo inmersivo "sticky" (si tocas la pantalla aparecen y desaparecen solas)
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        // Contenido debajo de las barras para que no salte al aparecer/desaparecer
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Ocultar barras de navegación y estado
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runner);
    }
}