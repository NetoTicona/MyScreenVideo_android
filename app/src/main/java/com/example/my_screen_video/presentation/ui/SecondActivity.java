package com.example.my_screen_video.presentation.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    private List<String> hexColors;
    private int durationMs;
    private int totalCycles;

    private int currentCycle = 0;
    private int currentIndex = 0;
    private Handler handler = new Handler();
    private Runnable runner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Pantalla completa */
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
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

        /* Runnable ciclico */
        runner = new Runnable() {
            @Override
            public void run() {
                tv.setBackgroundColor(Color.parseColor(hexColors.get(currentIndex)));
                currentIndex++;
                if (currentIndex == hexColors.size()) {
                    currentIndex = 0;
                    currentCycle++;
                    if (currentCycle >= totalCycles) {
                        finish();   // vuelve a Main
                        return;
                    }
                }
                handler.postDelayed(this, durationMs);
            }
        };
        handler.post(runner);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runner);
    }
}