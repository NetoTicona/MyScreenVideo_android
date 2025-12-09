package com.example.my_screen_video.presentation.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.my_screen_video.R;
import java.util.ArrayList;
import java.util.Arrays;
import com.example.my_screen_video.domain.model.ColorSequence;
import com.example.my_screen_video.domain.usecase.generatorColorSequenceUseCase;
import com.example.my_screen_video.presentation.util.ColorMapper;

public class MainActivity extends AppCompatActivity {

    /* ---- UI ---- */
    private EditText etDuration, etCycles, etUser;
    private Button btnStart;
    private TextView tvSequence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Referencias */
        etDuration = findViewById(R.id.etDuration);
        etCycles   = findViewById(R.id.etCycles);
        etUser     = findViewById(R.id.etUserNumber);
        btnStart   = findViewById(R.id.btnStart);
        tvSequence = findViewById(R.id.tvSequence);

        /* Botón siempre habilitado: lanza directo */
        btnStart.setEnabled(true);

        btnStart.setOnClickListener(v -> {
            Toast.makeText(this, "Iniciando secuencia...", Toast.LENGTH_SHORT).show();

            int duration = Integer.parseInt(etDuration.getText().toString());
            int cycles   = Integer.parseInt(etCycles.getText().toString());
            String bin   = etUser.getText().toString();


            int[] vector = new int[6];
            for (int i = 0; i < 6; i++) vector[i] = bin.charAt(i) - '0';


            generatorColorSequenceUseCase useCase = new generatorColorSequenceUseCase ();
            ColorSequence seq = useCase.invoke(vector);
            StringBuilder sb = new StringBuilder("Secuencia: ");
            for (String hex : seq.getHexColors()) {
                // Convertimos hex → número 1-6 (mismo orden que ya tenías)
                int num = ColorMapper.hexToNumber(hex); // ver abajo
                sb.append(num).append(' ');
            }
            tvSequence.setText(sb.toString());


            Intent i = new Intent(MainActivity.this, SecondActivity.class);
            i.putStringArrayListExtra("colors", new ArrayList<>(seq.getHexColors()));
            i.putExtra("duration", duration);
            i.putExtra("cycles", cycles);
            startActivity(i);

        });
    }
}