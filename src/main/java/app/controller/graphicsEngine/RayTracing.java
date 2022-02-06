package app.controller.graphicsEngine;

import app.controller.Beam;
import app.controller.Vector;
import app.model.agents.Agent;
import app.model.Map;
import app.model.Placeable;

import java.util.ArrayList;

public class RayTracing implements GraphicsEngine
{
    private int noOfRays = 360;
    private double angle = 1;

    public ArrayList<Beam> compute(Map map, Agent agent)
    {
        ArrayList<Beam> beams = new ArrayList<>();
        ArrayList<Ray> rays = scatterRays(agent);
        Vector origin = agent.getPosition();

        for(Ray r: rays)
        {
            Vector intersection = null;
            double closestDist = Double.MAX_VALUE;
            for (Placeable obj: map.getObjects())
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
                beams.add(new Beam(origin, intersection));
        }
        return beams;
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
}
