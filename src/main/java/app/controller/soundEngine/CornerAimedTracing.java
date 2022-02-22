package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;

import java.util.ArrayList;

public class CornerAimedTracing implements SoundEngine{
    public double compute(Map map, Agent agent){
        ArrayList<SoundRay> soundRays = new ArrayList<>();

        // Step 1: aim at all the soundsources
        // right now this means ignore obstactles and return the loudest audible sound from agent

        double maxSoundLevel = 0.0;

        for (SoundSource soundSource: map.getSoundSources()) {
            double currentSoundLevel = soundSource.soundLevelFrom(agent.getPosition());
            if(currentSoundLevel > maxSoundLevel) {
                maxSoundLevel = currentSoundLevel;
            }
        }

        return maxSoundLevel;

        /*
        for (SoundSource soundSource: map.getSoundSources()){
            soundRays.add(new SoundRay(agent.getPosition(), soundSource.getPosition()));
        }
         */


        // Step 2: for diffraction aim at all the corners
        /*
        for (SoundFurniture soundFurniture: map.getSoundFurniture()) {
            for (SoundBoundary soundBoundary: soundFurniture.getSoundBoundaries()) {
                for (Vector corner: soundBoundary.getCorners()) {
                    soundRays.add(new SoundRay(agent.getPosition(), corner));
                }
            }
        }
        */
    }
}
