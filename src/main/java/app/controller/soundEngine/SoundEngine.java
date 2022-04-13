package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.soundBoundary.SoundBoundary;
import java.util.ArrayList;
import java.util.Stack;

public class SoundEngine
{
    final static int noOfRays = 10;
    final static int noOfBounces = 4;

    public static ArrayList<SoundRay> compute(Map map, Vector origin)
    {
        ArrayList<SoundRay> output = new ArrayList<>();
        Stack<SoundRay> rays = SoundRayScatter.angle360(origin, noOfRays, 1000, noOfBounces);

        while(!rays.isEmpty())
        {
            SoundRay r = rays.pop();
            Vector agentIntersection = getAgentIntersection(r, map.getAgents());
            Vector bdyIntersection = getIntersection(r, map.getSoundBoundaries());

            if(bdyIntersection != null && agentIntersection != null)
            {
                output.add(new SoundRay(r.getU(), bdyIntersection));
                if(r.getBounces() > 0)
                {
                    Vector new_origin = bouncePoint(bdyIntersection, r.getU());
                    rays.addAll(SoundRayScatter.angle360(new_origin, noOfRays, 1000, r.getBounces()));
                }
                output.add(new SoundRay(r.getU(), agentIntersection));
            }
            else if(bdyIntersection != null)
            {
                if(r.getBounces() > 0)
                {
                    Vector new_origin = bouncePoint(bdyIntersection, r.getU());
                    rays.addAll(SoundRayScatter.angle360(new_origin, noOfRays, 1000, r.getBounces()));
                }
                output.add(new SoundRay(r.getU(), bdyIntersection));
            }
            else if(agentIntersection != null)
                output.add(new SoundRay(r.getU(), agentIntersection));
        }
        return output;
    }

    private static void diffuse(Map map, ArrayList<SoundRay> rays, ArrayList<SoundBoundary> boundaries)
    {
        for(SoundRay r: rays)
        {
            Vector agentIntersection = getAgentIntersection(r, map.getAgents());
            Vector bdyIntersection = getIntersection(r, boundaries);
        }
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

    private static Vector closestPoint(Vector origin, Vector a, Vector b)
    {
        if(a.dist(origin) <= b.dist(origin))
            return a;
        else
            return b;
    }

    /**
     * @return the intersection vector with a miniscule shift back to the origin
     */
    private static Vector bouncePoint(Vector intersection, Vector origin)
    {
        Vector u = intersection.sub(origin);
        u = u.normalise();
        return intersection.sub(u);
    }
}
