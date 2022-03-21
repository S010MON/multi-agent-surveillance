package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.agents.Team;
import app.model.boundary.Boundary;
import app.model.Map;
import app.model.boundary.PortalBoundary;
import lombok.Getter;

public class TestingEngine
{
    @Getter private int tics;
    private Map map;
    private GraphicsEngine graphicsEngine;
    private String test_name;

    public TestingEngine(Map map, String test_name)
    {
        this.tics = 0;
        this.map = map;
        this.test_name = test_name;
        this.graphicsEngine = new GraphicsEngine();
    }

    public int[] run()
    {
        double prevPercentage = 0;
        double currentPercentage;
        int[] times = new int[100];

        while(!complete() && tics < 100000 )
        {
            tick();

             currentPercentage = map.percentageComplete(Team.GUARD);
            if(currentPercentage != prevPercentage)
            {
                updatePercentageBar(currentPercentage);

                int index = (int) (currentPercentage *  100);
                times[index] = tics;

                prevPercentage = currentPercentage;
            }
        }
        System.out.println(" done");
        return times;
    }

    public void tick()
    {
        map.getAgents().forEach(a -> a.updateView(graphicsEngine.compute(map, a)));
        map.getAgents().forEach(a -> a.getView().forEach(ray -> a.updateSeen(ray.getV())));
        map.getAgents().forEach(a -> map.updateAllSeen(a));

        for (Agent a : map.getAgents())
        {
            Vector startPoint = a.getPosition();
            Vector endPoint = startPoint.add(a.move().getDeltaPos());

            Vector teleportTo = checkTeleport(startPoint, endPoint);
            if (teleportTo != null)
            {
                a.updateLocation(teleportTo);
                a.setMoveFailed(false);
            }
            else if (legalMove(startPoint, endPoint) &&
                    legalMove(a, endPoint) &&
                    legalMove(a, startPoint) && legalMove(a, startPoint, endPoint))
            {
                a.updateLocation(endPoint);
                a.setMoveFailed(false);
            }
            else
            {
                a.setMoveFailed(true);
            }
        }
        tics++;
    }

    private boolean complete()
    {
        return  map.percentageComplete(Team.GUARD) > 0.85;
    }

    private Vector checkTeleport(Vector start, Vector end)
    {
        for (Boundary bdy : map.getBoundaries())
        {
            if(bdy.isCrossed(start, end) && bdy instanceof PortalBoundary)
                return bdy.getTeleport();
        }
        return null;
    }

    private boolean legalMove(Vector start, Vector end)
    {
        for (Boundary bdy : map.getBoundaries())
        {
            if(bdy.isCrossed(start, end))
                return false;
        }
        return true;
    }

    private boolean legalMove(Agent currentAgent, Vector end)
    {
        for(Agent otherAgent: map.getAgents())
        {
            double dist = otherAgent.getPosition().dist(end);
            if(!currentAgent.equals(otherAgent) && dist <= currentAgent.getRadius())
                return false;
        }
        return true;
    }

    private boolean legalMove(Agent currentAgent, Vector start,Vector end)
    {
        double radius = currentAgent.getRadius();
        for(Agent otherAgent: map.getAgents())
        {
            Vector positionOther = currentAgent.getPosition();
            double radiusOther = otherAgent.getRadius();
            if(!currentAgent.equals(otherAgent) && !Intersection.hasDirectionIntersect(start, end, radius, positionOther, radiusOther))
                return false;
        }
        return true;
    }

    private void updatePercentageBar(double percent)
    {
        StringBuilder bar = new StringBuilder();
        bar.append(test_name);
        for(double d = 0; d < percent; d += 0.01)
        {
            bar.append("#");
        }
        bar.append(" ").append(percent).append("%");
        System.out.print("\r" + bar);
    }
}
