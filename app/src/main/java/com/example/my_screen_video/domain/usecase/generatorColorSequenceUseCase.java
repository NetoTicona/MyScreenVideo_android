package com.example.my_screen_video.domain.usecase;

import java.util.ArrayList;
import java.util.List;
import androidx.annotation.NonNull;
import com.example.my_screen_video.domain.model.ColorSequence;



public class generatorColorSequenceUseCase {

    private static final int[] COLOR_MAPS = {1, 2, 3, 4, 5, 6};
    private static final int[][] CODIN = {
            {Integer.MIN_VALUE, 0, 1, 2, 3, 4},
            {4, Integer.MIN_VALUE, 0, 1, 2, 3},
            {3, 4, Integer.MIN_VALUE, 0, 1, 2},
            {2, 3, 4, Integer.MIN_VALUE, 0, 1},
            {1, 2, 3, 4, Integer.MIN_VALUE, 0},
            {0, 1, 2, 3, 4, Integer.MIN_VALUE}
    };

    @NonNull
    public ColorSequence invoke(@NonNull int[] vectorCodigos) {

        if (vectorCodigos.length != 6) {

            throw new IllegalArgumentException("El vector debe tener exactamente 6 elementos");
        }

        int n = 6;
        int[][] paresSeleccionados = new int[n][2];

        // Paso 1: primer par
        int valor = vectorCodigos[0];
        int[] filaCol = buscarPrimero(CODIN, valor);
        paresSeleccionados[0] = filaCol;

        for (int i = 1; i < n; i++) {
            valor = vectorCodigos[i];
            int finAnterior = paresSeleccionados[i - 1][1];
            int[] siguiente = buscarConInicio(CODIN, valor, finAnterior);
            if (siguiente == null) {
                throw new RuntimeException("No se encontró par válido para valor " + valor + " en posición " + i);
            }
            paresSeleccionados[i] = siguiente;
        }

        List<Integer> secuencia = new ArrayList<>(n + 1);
        secuencia.add(COLOR_MAPS[paresSeleccionados[0][0] - 1]); // índice 0-based
        secuencia.add(COLOR_MAPS[paresSeleccionados[0][1] - 1]);
        for (int j = 1; j < n; j++) {
            secuencia.add(COLOR_MAPS[paresSeleccionados[j][1] - 1]);
        }


        // Mapear a hex
        List<String> hex = new ArrayList<>(secuencia.size());
        for (Integer num : secuencia) {
            hex.add(mapToHex(num));
        }


        return new ColorSequence(hex);

    }

    private int[] buscarPrimero ( int[][] mat, int val){
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (mat[i][j] == val) {
                    return new int[]{i + 1, j + 1}; // 1-based
                }
            }
        }
        throw new RuntimeException("Valor " + val + " no encontrado en CODIN");
    }

    private int[] buscarConInicio ( int[][] mat, int val, int inicio){
        for (int j = 0; j < 6; j++) {
            if (mat[inicio - 1][j] == val) {
                return new int[]{inicio, j + 1};
            }
        }
        return null;
    }

    private String mapToHex ( int num){
        switch (num) {
            case 1:
                return "#FF0000"; // rojo
            case 2:
                return "#00FF00"; // verde
            case 3:
                return "#0000FF"; // azul
            case 4:
                return "#00FFFF"; // cian
            case 5:
                return "#FF00FF"; // magenta
            case 6:
                return "#FFFF00"; // amarillo
            default:
                throw new IllegalArgumentException("Color index fuera de rango: " + num);
        }
    }



}