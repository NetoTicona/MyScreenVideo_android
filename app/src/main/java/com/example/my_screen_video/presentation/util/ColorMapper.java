package com.example.my_screen_video.presentation.util;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

public class ColorMapper {

    private ColorMapper(){};

    @NonNull
    public static String toHex( @IntRange( from = 1 , to = 6 ) int index ){
        switch (index) {
            case 1: return "#FF0000"; // rojo
            case 2: return "#00FF00"; // verde
            case 3: return "#0000FF"; // azul
            case 4: return "#00FFFF"; // cian
            case 5: return "#FF00FF"; // magenta
            case 6: return "#FFFF00"; // amarillo
            default:
                throw new IllegalArgumentException("Índice debe estar entre 1 y 6");
        }
    }

    public static int hexToNumber(String hex) {
        switch (hex) {
            case "#FF0000": return 1; // rojo
            case "#00FF00": return 2; // verde
            case "#0000FF": return 3; // azul
            case "#00FFFF": return 4; // cian
            case "#FF00FF": return 5; // magenta
            case "#FFFF00": return 6; // amarillo
            default: return 0;
        }
    }


}
