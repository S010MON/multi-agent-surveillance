package app.controller;

import app.model.Agent;
import app.model.MapTemp;
import app.model.Placeable;

import java.util.ArrayList;

public class RayTracing implements GraphicsEngine
{
    private int noOfRays = 360;
    private double angle = 1;
    private double maxLoS = 200;

    public void setMaxLoS(double maxLoS1)
    {
        this.maxLoS = maxLoS1;
    }

    public double getMaxLoS()
    {
        return this.maxLoS;
    }

    public ArrayList<Beam> compute(MapTemp map, Agent agent)
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
                    if(origin.dist(endPoint) < closestDist)
                    {
                        intersection = endPoint;
                        closestDist = Math.abs(endPoint.sub(origin).length());
                    }
                }
            }
            if(intersection != null && origin.dist(intersection) < maxLoS)
            {
                beams.add(new Beam(origin, intersection));
            }
            else
            {
                Vector direction = r.getEndPoint().sub(origin);
                double directionLength = direction.length();
                Vector rayDirectionLength = direction.scale(maxLoS/directionLength);
                Vector endPoint = origin.add(rayDirectionLength);
                beams.add(new Beam(origin, endPoint));
            }
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
