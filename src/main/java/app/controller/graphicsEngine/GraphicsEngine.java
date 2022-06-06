package app.controller.graphicsEngine;

import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.Map;
import app.model.Type;
import app.model.boundary.Boundary;
import app.model.boundary.BoundaryType;

import java.util.ArrayList;

public class GraphicsEngine
{
    private double angle = 91; // The +/- for field of view.  180 will look 180 deg lef and 180 degree right

    public GraphicsEngine(){}

    public GraphicsEngine(double angle)
    {
        this.angle = angle;
    }

    public ArrayList<Ray> compute(Map map, Agent agent)
    {
        ArrayList<Boundary> boundaries = collectBoundaries(map);
        ArrayList<Ray> output = new ArrayList<>();
        ArrayList<Ray> rays = RayScatter.angle(agent.getPosition(), agent.getDirection(), angle);
        Vector origin = agent.getPosition();

        for(Ray r: rays)
        {
            ArrayList<Ray> intersectionRays = getRays(r, boundaries, map.getAgents(), agent);
            intersectionRays.forEach(ray -> output.add(ray));
        }
        return output;
    }

    /**
     *  @return both the non-transparent intersectionRays, and the transparent (but visible) intersectionRays
     *  encountered before it, including agents
     */
    public ArrayList<Ray> getRays(Ray r, ArrayList<Boundary> boundaries, ArrayList<Agent> agents, Agent currentAgent)
    {
        ArrayList<Ray> transparentRays = new ArrayList<>();
        Ray nonTransparentRay = null;
        double closestDist = Double.MAX_VALUE;


        // check all the agents
        for (Agent agent: agents)
        {
            if(!agent.equals(currentAgent))
            {
                if (agent.isHit(r))
                {
                    Vector currentV = agent.intersection(r);
                    double dist = r.getU().dist(currentV);
                    if (dist < closestDist && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                    {
                        nonTransparentRay = new Ray(r.getU(), currentV, agent.getType());

                        closestDist = dist;
                    }
                }
            }
        }

        // check all the furniture
        for (Boundary obj: boundaries)
        {
            if(obj.isHit(r))
            {
                Vector currentV = obj.intersection(r);
                if(!BoundaryType.isTransparent(obj.getBoundaryType()))
                {
                    double dist = r.getU().dist(currentV);
                    if(dist < closestDist && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                    {
                        nonTransparentRay = new Ray(r.getU(), currentV, obj.getType());
                        closestDist = dist;
                    }
                }
                else if(BoundaryType.isVisible(obj.getBoundaryType()) && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                {
                    transparentRays.add(new Ray(r.getU(), currentV, obj.getType()));
                }
            }
        }

        ArrayList<Ray> rays = new ArrayList<>();
        if(nonTransparentRay != null)
            rays.add(nonTransparentRay);
        else
            rays.add(new Ray(r.getU(), r.getV()));

        for(Ray ray: transparentRays)
        {
            if(ray.length() < closestDist)
            {
                rays.add(ray);
            }
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