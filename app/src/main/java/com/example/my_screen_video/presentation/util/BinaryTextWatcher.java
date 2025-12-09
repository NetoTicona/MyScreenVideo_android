package com.example.my_screen_video.presentation.util;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;

import androidx.annotation.NonNull;

public class BinaryTextWatcher implements TextWatcher {
    public interface InvalidInputListener {
        void onInvalidInput();
    }

    private final InvalidInputListener listener;

    public BinaryTextWatcher() { this(null); }

    public BinaryTextWatcher(InvalidInputListener listener) {
        this.listener = listener;
    }

    /* ---------- TextWatcher ---------- */
    @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override public void onTextChanged(CharSequence s, int start, int before, int count) { }

    @Override
    public void afterTextChanged(Editable s) {
        // Recorre carácter a carácter
        for (int i = s.length() - 1; i >= 0; i--) {
            char c = s.charAt(i);
            if (c != '0' && c != '1') {
                s.delete(i, i + 1);          // lo borra
                if (listener != null) listener.onInvalidInput();
            }
        }
    }

    /* ---------- Utilidad estática para aplicar filtro rápido ---------- */
    public static InputFilter[] filters() {
        return new InputFilter[]{ (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++) {
                char c = source.charAt(i);
                if (c != '0' && c != '1') return ""; // rechaza el bloque
            }
            return null; // acepta
        }};
    }
}
