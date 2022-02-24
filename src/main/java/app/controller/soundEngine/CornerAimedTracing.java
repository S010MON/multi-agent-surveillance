package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class CornerAimedTracing implements SoundEngine{
    public double compute(Map map, Agent agent){
        PriorityQueue<VectorPQEntry> vectorPQ = new PriorityQueue<VectorPQEntry>(new VectorPQComparator());

        ArrayList<SoundSource> hitSources = new ArrayList<>();

        // for each sound source reachable from the current vector, add a child to the current node
        // Step 2: for diffraction aim at all the corners

        /*
        for (int i = 0; i < 10; i++) {
            
        }
        */
        // first take the current node, then aim at all the soundsources


        return 0.0;
    }

    private ArrayList<SoundSource> audibleFrom(Map map, Vector pos){
        ArrayList<SoundSource> audible = new ArrayList<>();

        for (SoundSource soundSource: map.getSoundSources()) {
            SoundRay rayToSource = new SoundRay(pos, soundSource.getPosition());

            if(!intersectsAnySoundFurniture(map.getSoundFurniture(), rayToSource)){
                audible.add(soundSource);
            }
        }

        return audible;
    }

    private ArrayList<Vector> findReachableCorners(Map map, Vector pos){
        ArrayList<Vector> reachable = new ArrayList<>();

        for (SoundFurniture soundFurniture: map.getSoundFurniture()) {
            for (SoundBoundary soundBoundary: soundFurniture.getSoundBoundaries()) {
                for (Vector corner: soundBoundary.getCorners()) {
                    SoundRay rayToCorner = new SoundRay(pos, corner);
                    if(!intersectsAnySoundFurniture(map.getSoundFurniture(), rayToCorner)) {
                        reachable.add(corner);
                    }
                }
            }
        }

        return reachable;
    }

    private boolean intersectsAnySoundFurniture(ArrayList<SoundFurniture> soundFurniture, SoundRay soundRay){
        for (SoundFurniture sf: soundFurniture) {
            if(sf.intersectsAny(soundRay)) {
                return true;
            }
        }
        return false;
    }
}
