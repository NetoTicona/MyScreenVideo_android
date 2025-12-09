package com.example.my_screen_video.presentation.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.my_screen_video.domain.model.ColorSequence;
import com.example.my_screen_video.domain.usecase.generatorColorSequenceUseCase;

public class MainViewModel extends ViewModel {

    // Entradas de UI
    private final MutableLiveData<Integer> duration = new MutableLiveData<>(1000);
    private final MutableLiveData<Integer> cycles   = new MutableLiveData<>(1);
    private final MutableLiveData<String> userNumber= new MutableLiveData<>("");

    // Salidas
    private final MediatorLiveData<Boolean> startEnabled = new MediatorLiveData<>();
    private final MutableLiveData<ColorSequence> sequenceReady = new MutableLiveData<>();

    // Caso de uso
    private final generatorColorSequenceUseCase useCase;

    /* ====== Constructor ====== */
    public MainViewModel() {
        useCase = new generatorColorSequenceUseCase();

        // Inicializar con false
        startEnabled.setValue(false);

        // Usar MediatorLiveData en lugar de Transformations anidados
        startEnabled.addSource(duration, d -> updateStartEnabled());
        startEnabled.addSource(cycles, c -> updateStartEnabled());
        startEnabled.addSource(userNumber, u -> updateStartEnabled());
    }

    /* ====== Método auxiliar para calcular startEnabled ====== */
    private void updateStartEnabled() {
        Integer d = duration.getValue();
        Integer c = cycles.getValue();
        String u = userNumber.getValue();

        boolean enabled = (d != null && d > 0) &&
                (c != null && c > 0) &&
                (u != null && u.matches("[01]{6}"));

        startEnabled.setValue(enabled);
    }

    /* ====== Getters (solo lectura) ====== */
    public LiveData<Integer> getDuration() { return duration; }
    public LiveData<Integer> getCycles()   { return cycles; }
    public LiveData<String> getUserNumber(){ return userNumber; }
    public LiveData<Boolean> getStartEnabled() { return startEnabled; }
    public LiveData<ColorSequence> getSequenceReady() { return sequenceReady; }

    /* ====== Setters (escritura desde UI) ====== */
    public void setDuration(Integer d) { duration.setValue(d); }
    public void setCycles(Integer c)   { cycles.setValue(c); }
    public void setUserNumber(String s){ userNumber.setValue(s); }

    /* ====== Acción botón Iniciar ====== */
    public void onStartClicked() {
        String raw = userNumber.getValue();
        if (raw == null || raw.length() != 6) return;

        int[] vector = new int[6];
        for (int i = 0; i < 6; i++) vector[i] = raw.charAt(i) - '0';

        ColorSequence seq = useCase.invoke(vector);
        sequenceReady.setValue(seq);
    }
}