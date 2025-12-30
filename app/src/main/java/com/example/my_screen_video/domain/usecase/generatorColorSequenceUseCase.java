package com.example.my_screen_video.domain.usecase;

import android.graphics.Color; // Necesario para la conversión HSV
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

        // Generamos la secuencia numérica base (índices del 1 al 6)
        List<Integer> secuenciaNumerica = new ArrayList<>(n + 1);
        secuenciaNumerica.add(COLOR_MAPS[paresSeleccionados[0][0] - 1]);
        secuenciaNumerica.add(COLOR_MAPS[paresSeleccionados[0][1] - 1]);
        for (int j = 1; j < n; j++) {
            secuenciaNumerica.add(COLOR_MAPS[paresSeleccionados[j][1] - 1]);
        }

        // --- AQUI APLICAMOS TU LÓGICA DE BLANCO Y COMPLEMENTARIOS ---
        List<String> hexFinalList = new ArrayList<>();

        // 1. Primero insertamos el BLANCO antes de nada
        hexFinalList.add("#FFFFFF");

        // 2. Recorremos los colores base y agregamos: Original -> Complementario
        for (Integer num : secuenciaNumerica) {
            String colorOriginal = mapToHex(num);
            
            // Agregamos el original
            hexFinalList.add(colorOriginal);
            
            // Calculamos y agregamos el complementario (HSV + 180°)
            String colorComplementario = getComplementaryColor(colorOriginal);
            hexFinalList.add(colorComplementario);
        }

        return new ColorSequence(hexFinalList);
    }

    // --- MÉTODOS AUXILIARES ---

    /**
     * Recibe un color Hex, lo convierte a HSV, suma 180° al Hue y devuelve el nuevo Hex.
     */
    private String getComplementaryColor(String hexColor) {
        // 1. Convertir Hex a int
        int colorInt = Color.parseColor(hexColor);
        
        // 2. Convertir a componentes HSV
        float[] hsv = new float[3];
        Color.colorToHSV(colorInt, hsv);
        
        // hsv[0] es el Hue (Matiz) [0 .. 360)
        // hsv[1] es Saturation
        // hsv[2] es Value (Brightness)

        // 3. Rotar 180 grados para el complementario
        hsv[0] = (hsv[0] + 180) % 360;

        // 4. Convertir de vuelta a Int y luego a Hex String
        int complementarioInt = Color.HSVToColor(hsv);
        
        // Formatear a string hexadecimal #RRGGBB
        return String.format("#%06X", (0xFFFFFF & complementarioInt));
    }

    private int[] buscarPrimero(int[][] mat, int val) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 6; j++) {
                if (mat[i][j] == val) {
                    return new int[]{i + 1, j + 1}; // 1-based
                }
            }
        }
        throw new RuntimeException("Valor " + val + " no encontrado en CODIN");
    }

    private int[] buscarConInicio(int[][] mat, int val, int inicio) {
        for (int j = 0; j < 6; j++) {
            if (mat[inicio - 1][j] == val) {
                return new int[]{inicio, j + 1};
            }
        }
        return null;
    }

    private String mapToHex(int num) {
        switch (num) {
            case 1: return "#FF0000"; // Rojo
            case 2: return "#00FF00"; // Verde
            case 3: return "#0000FF"; // Azul
            case 4: return "#00FFFF"; // Cian
            case 5: return "#FF00FF"; // Magenta
            case 6: return "#FFFF00"; // Amarillo
            default: throw new IllegalArgumentException("Color index fuera de rango: " + num);
        }
    }
}