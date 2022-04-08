package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import java.util.ArrayList;

public class SoundEngine
{
    public ArrayList<SoundRay> compute(Map map, Agent agent)
    {
        ArrayList<SoundBoundary> boundaries = collectSoundBoundaries(map);
        ArrayList<SoundRay> output = new ArrayList<>();
        ArrayList<SoundRay> rays = SoundRayScatter.angle360(agent.getPosition(), 10, 5);
        Vector origin = agent.getPosition();

        for(SoundRay r: rays)
        {
            Vector agentIntersection = getIntersection(r, map.getAgents(), agent);

            if(agentIntersection != null)
                output.add(new SoundRay(origin, agentIntersection));
        }
        return output;
    }

    private Vector getIntersection(SoundRay r, ArrayList<Agent> agents, Agent currentAgent)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (Agent agent: agents)
        {
            if(!agent.equals(currentAgent))
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
