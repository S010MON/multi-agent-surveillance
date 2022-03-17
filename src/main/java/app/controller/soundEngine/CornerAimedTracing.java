package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import app.model.soundFurniture.SoundFurniture;
import app.model.soundSource.SoundSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class CornerAimedTracing implements SoundEngine
{
    public HashMap<SoundSource, Sound> compute(Map map, Agent agent)
    {
        HashMap<SoundSource, Sound> soundTable = new HashMap<>();
        int maxDiffraction = 1;

        Vector agentPos = agent.getPosition();

        ArrayList<Vector> nextToCheck =  new ArrayList<>();
        nextToCheck.add(agentPos);

        for (int i = 0; i < maxDiffraction + 1; i++)
        {

            ArrayList<Vector> corners = nextToCheck;
            nextToCheck = new ArrayList<>();

            for (Vector pos: corners)
            {
                ArrayList<SoundSource> audible = audibleFrom(map, pos);

                for (SoundSource heard: audible)
                {
                    if(!soundTable.containsKey(heard))
                    {
                        if(i == 0) {
                            soundTable.put(heard, new Sound(heard, agentPos, heard.getPosition(), 0));
                            continue;
                        }
                        soundTable.put(heard, new Sound(heard, agentPos, pos, i));
                        continue;
                    }
                    soundTable.get(heard).update(pos, i);
                }

                ArrayList<Vector> nextLayer = findReachableCorners(map, agentPos);

                nextToCheck.addAll(nextLayer);
            }
        }

        return soundTable;
    }

    private ArrayList<SoundSource> audibleFrom(Map map, Vector pos)
    {
        ArrayList<SoundSource> audible = new ArrayList<>();

        for (SoundSource soundSource: map.getSoundSources())
        {
            SoundRay rayToSource = new SoundRay(pos, soundSource.getPosition());

            if(!intersectsAnySoundFurniture(map.getSoundFurniture(), rayToSource))
            {
                audible.add(soundSource);
            }
        }

        return audible;
    }

    private ArrayList<Vector> findReachableCorners(Map map, Vector pos)
    {
        // linked hashset, because order is important
        LinkedHashSet<Vector> reachable = new LinkedHashSet<>();

        // make sure that each corner can actually hear the source? so maybe store the loudest source in a var
        for (SoundFurniture soundFurniture: map.getSoundFurniture())
        {
            for (SoundBoundary soundBoundary: soundFurniture.getSoundBoundaries())
            {
                for (Vector corner: soundBoundary.getCorners())
                {
                    SoundRay rayToCorner = new SoundRay(pos, corner);
                    if(hitsAnyCorner(map.getSoundFurniture(), rayToCorner))
                    {
                        reachable.add(corner);
                    }
                }
            }
        }
        return new ArrayList<>(reachable);
    }

    private boolean hitsAnyCorner(ArrayList<SoundFurniture> soundFurniture, SoundRay soundRay)
    {
        // since we aim at corners they will always intersect on the boundary of the furniture (that is at most once)
        for (SoundFurniture sf: soundFurniture)
        {
            if(sf.hitsCorner(soundRay))
            {
                return true;
            }
        }
        return false;
    }

    private boolean intersectsAnySoundFurniture(ArrayList<SoundFurniture> soundFurniture, SoundRay soundRay)
    {
        // TODO currently there is a problem for rays starting from points inside a furniture
        // TODO need a way of checking if the ray starts from or ends in an "outside facing" boundary

        for (SoundFurniture sf: soundFurniture)
        {
            if(sf.intersects(soundRay))
            {
                return true;
            }
        }
        return false;
    }
}
