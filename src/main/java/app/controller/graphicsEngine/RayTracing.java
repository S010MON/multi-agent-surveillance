package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.map.Map;
import app.model.boundary.Boundary;

import java.util.ArrayList;

public class RayTracing implements GraphicsEngine
{
    private int noOfRays = 360;
    private double angle = 1;

    public ArrayList<Ray> compute(Map map, Agent agent)
    {
        ArrayList<Boundary> boundaries = collectBoundaries(map);
        ArrayList<Ray> output = new ArrayList<>();
        ArrayList<Ray> rays = scatterRays(agent);
        Vector origin = agent.getPosition();

        for(Ray r: rays)
        {
            Vector intersection = null;
            double closestDist = Double.MAX_VALUE;
            for (Boundary obj: boundaries)
            {
                    if(obj.isHit(r))
                    {
                        Vector endPoint = obj.intersection(r);
                        double dist = origin.dist(endPoint);
                        if(dist < closestDist)
                        {
                            intersection = endPoint;
                            closestDist = dist;
                        }
                    }
            }
            if(intersection != null)
                output.add(new Ray(origin, intersection));
        }
        return output;
    }

    private ArrayList<Ray> scatterRays(Agent agent)
    {
        ArrayList<Ray> rays = new ArrayList<>();
        Vector origin = agent.getPosition();
        Vector direction = agent.getDirection();
        Ray ray = new Ray(origin, direction);
        double theta = 0;
        for(int i = 0; i < noOfRays; i++)
        {
            rays.add(ray.rotate(theta));
            theta += angle;
        }
        return rays;
    }

    private ArrayList<Boundary> collectBoundaries(Map map)
    {
        ArrayList<Boundary> boundaries = new ArrayList<>();
        map.getFurniture()
                .forEach(furniture -> boundaries.addAll(furniture.getBoundaries()));
        return boundaries;
    }
}
