package app.controller.soundEngine;


import app.model.Map;
import app.model.soundBoundary.SoundBoundary;
import java.util.ArrayList;

public class SoundEngine
{




    private ArrayList<SoundBoundary> collectSoundBoundaries(Map map)
    {
        ArrayList<SoundBoundary> soundBoundaries = new ArrayList<>();
        map.getFurniture()
           .forEach(furniture -> soundBoundaries.addAll(furniture.getSoundBoundaries()));
        return soundBoundaries;
    }
}
