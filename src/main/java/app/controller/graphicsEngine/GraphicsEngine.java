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
            ArrayList<Vector> bdyIntersections = getIntersections(r, boundaries);
            Vector agentIntersection = getIntersection(r, map.getAgents(), agent);

            if(bdyIntersections.size() != 0 && agentIntersection != null)
                bdyIntersections.forEach(bdy -> output.add(closestRay(origin, bdy, agentIntersection, map)));

            else if(bdyIntersections.size() != 0)
                bdyIntersections.forEach(bdy -> output.add(new Ray(origin, bdy, map.objectAt(bdy))));

            else if(agentIntersection != null)
                output.add(new Ray( origin, agentIntersection, map.objectAt(agentIntersection) ));

        }
        return output;
    }

    /**
     *  @return both the non-transparent intersection, and the transparent (but visible) intersections
     *  encountered before it
     */
    private ArrayList<Vector> getIntersections(Ray r, ArrayList<Boundary> boundaries)
    {
        ArrayList<Vector> transparentIntersections = new ArrayList<>();
        Vector nonTransparentIntersection = null;
        double closestDist = Double.MAX_VALUE;
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
                        nonTransparentIntersection = currentV;
                        closestDist = dist;
                    }
                }
                else if(BoundaryType.isVisible(obj.getBoundaryType()) && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
                {
                    transparentIntersections.add(obj.intersection(r));
                }
            }
        }

        ArrayList<Vector> intersections = new ArrayList<>();
        if(nonTransparentIntersection!=null)
            intersections.add(nonTransparentIntersection);

        for(Vector intersection: transparentIntersections)
        {
            double dist = r.getU().dist(intersection);
            if(dist < closestDist)
            {
                intersections.add(intersection);
            }
        }
        return intersections;
    }

    public Vector getIntersection(Ray r, ArrayList<Agent> agents, Agent currentAgent)
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
                    if (dist < closestDist && Double.compare(currentV.sub(r.getU()).getAngle(), r.angle()) == 0)
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

    private Ray closestRay(Vector origin, Vector bdyIntersection, Vector agentIntersection, Map map)
    {
        if(bdyIntersection.dist(origin) <= agentIntersection.dist(origin))
            return new Ray(origin, bdyIntersection, map.objectAt(bdyIntersection));
        else
            return new Ray(origin, agentIntersection, map.objectAt(agentIntersection));
    }
}