package app.controller;

import app.model.Agent;
import app.model.MapTemp;
import app.model.Placeable;

import java.util.ArrayList;

public class GraphicsEngine
{
    private static GraphicsEngine instance;


    private int noOfRays = 360;
    private double angle = 1;


    public static GraphicsEngine getInstance()
    {
        if(instance == null)
            instance = new GraphicsEngine();
        return instance;
    }

    private GraphicsEngine()
    {
        // Singleton
    }

    public ArrayList<Beam> compute(MapTemp map, Agent agent)
    {
        ArrayList<Beam> beams = new ArrayList<>();
        ArrayList<Ray> rays = scatterRays(agent);

        for(Ray r: rays)
        {
            for (Placeable obj: map.getObjects())
            {
                if(obj.isHit(r))
                {
                    Vector intersection = obj.intersection(r);
                    beams.add(new Beam(agent.getPosition(), intersection));
                }
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
