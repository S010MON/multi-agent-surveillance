package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.Map;
import app.model.boundary.Boundary;
import java.util.ArrayList;

public class GraphicsEngine
{
    private double angle = 50;

    public ArrayList<Ray> compute(Map map, Agent agent)
    {
        ArrayList<Boundary> boundaries = collectBoundaries(map);
        ArrayList<Ray> output = new ArrayList<>();
        ArrayList<Ray> rays = RayScatter.angle(agent.getPosition(), agent.getDirection(), angle);
        Vector origin = agent.getPosition();

        for(Ray r: rays)
        {
            Vector bdyIntersection = getIntersection(r, boundaries);
            Vector agentIntersection = getIntersection(r, map.getAgents(), agent);

            if(bdyIntersection != null && agentIntersection != null)
            {
                if(bdyIntersection.dist(origin) <= agentIntersection.dist(origin))
                {
                    output.add(new Ray(origin, bdyIntersection));
                }
                else
                {
                    output.add(new Ray(origin, agentIntersection));
                }
            }
            else if(bdyIntersection != null)
                output.add(new Ray(origin, bdyIntersection));
            else if(agentIntersection != null)
                output.add(new Ray(origin, agentIntersection));
        }
        return output;
    }

    private Vector getIntersection(Ray r, ArrayList<Boundary> boundaries)
    {
        Vector intersection = null;
        double closestDist = Double.MAX_VALUE;
        for (Boundary obj: boundaries)
        {
            if(obj.isHit(r))
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

    private Vector getIntersection(Ray r, ArrayList<Agent> agents, Agent currentAgent)
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

    private ArrayList<Boundary> collectBoundaries(Map map)
    {
        ArrayList<Boundary> boundaries = new ArrayList<>();
        map.getFurniture()
                .forEach(furniture -> boundaries.addAll(furniture.getBoundaries()));
        return boundaries;
    }
}
