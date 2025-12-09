package com.example.my_screen_video.domain.model;

import java.util.List;

public class ColorSequence {

    private final List<String> hexColors;

    public ColorSequence( List<String> hexColors ){
        this.hexColors = java.util.Collections.unmodifiableList( hexColors );
    }

    public List<String> getHexColors(){
        return hexColors;
    }

    public int size(){
        return hexColors.size();
    }


}
