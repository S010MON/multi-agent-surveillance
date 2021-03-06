package app.controller.soundEngine;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.boundary.SoundBoundary;
import java.util.ArrayList;
import java.util.Stack;

public class SoundEngine
{
    final static int noOfRays = 4;
    final static int noOfBounces = 2;
    final static int maxDist = 1000;

    public static ArrayList<SoundRay> buildTree(Map map, SoundSource source)
    {
        ArrayList<SoundBoundary> boundaries = collectSoundBoundaries(map);
        ArrayList<SoundRay> output = new ArrayList<>();
        Stack<SoundRay> stack = SoundRayScatter.angle360(source.getPosition(), noOfRays, maxDist, noOfBounces);

        while(!stack.isEmpty())
        {
            SoundRay r = stack.pop();
            Vector agentIntersection = getAgentIntersection(r, map.getAgents());
            Vector bdyIntersection = getIntersection(r, boundaries);
            Vector origin = source.getPosition();
            Vector endPoint = null;

            if(bdyIntersection != null && agentIntersection != null)
                endPoint = closestPoint(origin, agentIntersection, bdyIntersection);

            else if(agentIntersection != null)
                endPoint = agentIntersection;

            else if(bdyIntersection != null)
                endPoint = bdyIntersection;

            if( endPoint != null)
            {
                if(r.getBounces() > 0 && endPoint.dist(r.getU()) > 0.01)
                {
                    Vector new_origin = bouncePoint(endPoint, r.getU());
                    stack.addAll(SoundRayScatter.angle360(new_origin, noOfRays, maxDist, r.getBounces()));
                }
                output.add(new SoundRay(r.getU(), endPoint, r.getBounces()));
            }
        }
        return output;
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
        for (Agent agent: agents)
        {
            if (agent.isHit(r))
            {
                intersection = agent.intersection(r);
            }
        }
        return intersection;
    }


    private static ArrayList<SoundBoundary> collectSoundBoundaries(Map map)
    {
        ArrayList<SoundBoundary> soundBoundaries = new ArrayList<>();
        map.getFurniture()
           .forEach(furniture -> soundBoundaries.addAll(furniture.getSoundBoundaries()));
        return soundBoundaries;
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
