package app.view.simulation;

import app.controller.linAlg.Vector;
import app.model.Map;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.furniture.Portal;
import app.model.map.Move;
import java.util.ArrayList;
import java.util.List;

public abstract class TeleportCoordinator {

    /** make legal to be able to teleport*/
    public static Move teleportIfLegal(Agent a, Vector start, Vector end, Map map)
    {
        Move move = null;
        List<Portal> portalsOnPath = getPortalsOnPath(map, start, end);
        if(portalsOnPath.size()==1)
        {
            Portal portal = portalsOnPath.get(0);
            Vector intersection = getIntersectionPoint(portal, start, end);
            move = getTeleportMove(map, a, portal, intersection, start, end);
        }
        if(portalsOnPath.size()>1)
        {
            double minDistance = start.dist(end);
            Vector closestIntersection = end;
            Portal closestPortal = portalsOnPath.get(0);
            for(Portal portal: portalsOnPath)
            {
                Vector newIntersection = getIntersectionPoint(portal, start, end);
                double newDistance = start.dist(newIntersection);
                if(newDistance<minDistance)
                {
                    minDistance = newDistance;
                    closestIntersection = newIntersection;
                    closestPortal = portal;
                }
            }

            move = getTeleportMove(map, a, closestPortal, closestIntersection, start, end);
        }
        return move;
    }

    private static boolean legalMove(Map map, Vector start, Vector end)
    {
        for (Boundary bdy : map.getBoundaries())
        {
            if(bdy.validMove(start, end))
                return false;
        }
        return true;
    }

    private static Move getTeleportMove(Map map, Agent a, Portal portal, Vector intersection, Vector start, Vector end)
    {
        Move move = null;
        if(legalMove(map, start, intersection))
        {
            //a.updateLocation(portal.getTeleportTo());

            Vector direction = a.getDirection();
            Vector newDirection = direction.rotate(portal.getTeleportRotation());
            //a.updateDirection(newDirection);

            double leftOverDist = intersection.dist(end);
            Vector directedDist = newDirection.normalise().scale(leftOverDist);
            //a.updateLocation(a.getPosition().add(directionDist));
            Vector finalPosition = portal.getTeleportTo().add(directedDist);
            Vector changePosition = finalPosition.sub(a.getPosition());
            move = new Move(newDirection, changePosition);
        }
        return move;
    }

    private static List<Portal> getPortalsOnPath(Map map, Vector start, Vector end)
    {
        List<Portal> portalsOnPath = new ArrayList<>();
        List<Furniture> portals = map.getPortals();
        for(Furniture portal: portals)
        {
            List<Boundary> bounds = portal.getBoundaries();
            Boolean intersects = false;
            for (Boundary bdy : bounds)
            {
                if(!bdy.validMove(start, end))
                    portalsOnPath.add((Portal) portal);
            }
        }
        return portalsOnPath;
    }

    private static Vector getIntersectionPoint(Portal portal, Vector start, Vector end)
    {
        List<Boundary> bounds = portal.getBoundaries();
        Vector intersection = end;
        for (Boundary bdy : bounds)
        {
            Vector newIntersection = bdy.intersection(start, end);
            if(newIntersection!=null)
                if(start.dist(newIntersection)<start.dist(intersection))
                    intersection = newIntersection;
        }
        return intersection;
    }
}
