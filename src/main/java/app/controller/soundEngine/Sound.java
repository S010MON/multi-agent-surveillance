package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import java.util.ArrayList;

import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import lombok.Getter;

public class Sound
{
    private final int bounceLimit = 3;
    private final int noOfRays = 11;
    private final double maxDist = 1000;

    @Getter private Vector location;
    @Getter private double amplitude;
    @Getter private int bounces;
    @Getter private Sound parent;
    @Getter private ArrayList<Sound> children;
    private ArrayList<SoundRay> rays;
    private Map map;

    public Sound(Map map, Vector location, double amplitude, Sound parent)
    {
        this.location = location;
        this.amplitude = amplitude;
        this.parent = parent;
        this.map = map;
        children = new ArrayList<>();

        if(parent == null)
            this.bounces = 0;
        else
            this.bounces = parent.getBounces() + 1;
    }

    private ArrayList<Sound> scatter()
    {
        double rand = 360 * Math.random();
        Vector dir = new Vector(0,maxDist).rotate(rand);
        int increment = 360/noOfRays;

        for(int i = 0; i < 360; i += increment)
        {
            rays.add(new SoundRay(location, location.add(dir.rotate(i))));
        }

        for(SoundRay ray: rays)
        {
            Vector agentIntersection = getAgentIntersection(ray, map.getAgents());
            Vector bdyIntersection = getIntersection(ray, map.getSoundBoundaries());

            if(agentIntersection != null && bdyIntersection != null)
            {

            }

        }
        return null;
    }

    private static Vector getIntersection(SoundRay r, ArrayList<SoundBoundary> boundaries)
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

    private static Vector getAgentIntersection(SoundRay r, ArrayList<Agent> agents)
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

}
