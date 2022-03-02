package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.PriorityQueue;

public class CornerAimedTracing implements SoundEngine{
    public double compute(Map map, Agent agent){
        // TODO return a vector with the soundLevel from what direction

        // each element stores an arraylist of vectors with the diffraction count equal to its index in the array
        ArrayList<HashSet<Vector>> listeners = new ArrayList<>();

        ArrayList<SoundSource> hitSources = new ArrayList<>();

        // for each sound source reachable from the current vector, add a child to the current node
        // Step 2: for diffraction aim at all the corners

        HashSet<Vector> currentListeners = new HashSet<>();

        currentListeners.add(agent.getPosition());

        int maxDiffraction = 10;

        for (int i = 0; i < maxDiffraction && !currentListeners.isEmpty(); i++) {
            listeners.add(currentListeners);
            HashSet<Vector> newListeners =  new HashSet<>();
            for (Vector listener: currentListeners) {
                // very inefficient right now since it will look for any corner in reach
                newListeners.addAll(findReachableCorners(map,listener));
            }
            currentListeners = newListeners;
        }

        // now for each layer, update each set with the soundsources that can be heard from it, maybe in the form of the loudest noise?

        return 0.0;
    }

    public ArrayList<SoundSource> computeSources(Map map, Agent agent){
        HashSet<SoundSource> heardSources = new HashSet<>();

        // do smart things here

        // direct sound:

        heardSources.addAll(audibleFrom(map, agent.getPosition()));

        // first diffraction

        ArrayList<Vector> firstLayerCorners = findReachableCorners(map, agent.getPosition());

        firstLayerCorners.forEach(pos -> {
            heardSources.addAll(audibleFrom(map, pos));
        });


        return new ArrayList<>(heardSources);
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
        HashSet<Vector> reachable = new HashSet<>();
        // make sure that each corner can actually hear the source? so maybe store the loudest source in a var

        for (SoundFurniture soundFurniture: map.getSoundFurniture()) {
            for (SoundBoundary soundBoundary: soundFurniture.getSoundBoundaries()) {
                for (Vector corner: soundBoundary.getCorners()) {
                    SoundRay rayToCorner = new SoundRay(pos, corner);
                    if(hitsAnyCorner(map.getSoundFurniture(), rayToCorner)) {
                        reachable.add(corner);
                    }
                }
            }
        }

        return new ArrayList<>(reachable);
    }

    private boolean hitsAnyCorner(ArrayList<SoundFurniture> soundFurniture, SoundRay soundRay){
        // since we aim at corners they will always intersect on the boundary of the furniture (that is at most once)
        for (SoundFurniture sf: soundFurniture) {
            if(sf.hitsCorner(soundRay)) {
                return true;
            }
        }
        return false;
    }

    private boolean intersectsAnySoundFurniture(ArrayList<SoundFurniture> soundFurniture, SoundRay soundRay){
        for (SoundFurniture sf: soundFurniture) {
            if(sf.intersectsAny(soundRay) && sf.isCorner(soundRay.getStart())) {
                return true;
            }
        }
        return false;
    }
}
