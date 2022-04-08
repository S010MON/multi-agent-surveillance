package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import java.util.ArrayList;

public class SoundEngine
{
    public ArrayList<SoundRay> compute(Map map, Vector soundSource)
    {
        ArrayList<SoundBoundary> boundaries = collectSoundBoundaries(map);
        ArrayList<SoundRay> output = new ArrayList<>();
        ArrayList<SoundRay> rays = SoundRayScatter.angle360(soundSource, 10, 5);
        Vector origin = soundSource;

        for(SoundRay r: rays)
        {
            Vector agentIntersection = getIntersection(r, map.getAgents(), null);
            Vector bdyIntersection = getIntersection(r, boundaries);

            if(bdyIntersection != null && agentIntersection != null)
            {
                if(bdyIntersection.dist(origin) <= agentIntersection.dist(origin))
                {
                    output.add(new SoundRay(origin, bdyIntersection));
                }
                else
                {
                    output.add(new SoundRay(origin, agentIntersection));
                }
            }
            else if(bdyIntersection != null)
                output.add(new SoundRay(origin, bdyIntersection));

            if(agentIntersection != null)
                output.add(new SoundRay(origin, agentIntersection));
        }
        return output;
    }

    private Vector getIntersection(SoundRay r, ArrayList<SoundBoundary> boundaries)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (SoundBoundary obj: boundaries)
        {
            if(obj.intersects(r))
            {
                Vector currentV = obj.intersection(r);
                double dist = r.getU().dist(currentV);
                if(dist < closestDist)
                {
                    intersection = currentV;
                    closestDist = dist;
                }
            }
        }
        return intersection;
    }

    private Vector getIntersection(SoundRay r, ArrayList<Agent> agents, Agent leaveNull)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (Agent agent: agents)
        {
            if (agent.isHit(r))
            {
                Vector currentV = agent.intersection(r);
                double dist = r.getU().dist(currentV);
                if (dist < closestDist && currentV.getAngle() == r.angle())
                {
                    intersection = currentV;
                    closestDist = dist;
                }
            }
        }
        return intersection;
    }

    private ArrayList<SoundBoundary> collectSoundBoundaries(Map map)
    {
        ArrayList<SoundBoundary> soundBoundaries = new ArrayList<>();
        map.getFurniture()
           .forEach(furniture -> soundBoundaries.addAll(furniture.getSoundBoundaries()));
        return soundBoundaries;
    }
}
