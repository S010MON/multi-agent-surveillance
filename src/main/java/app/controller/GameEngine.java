package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.Map;
import app.model.furniture.Furniture;
import app.model.furniture.FurnitureType;
import app.model.furniture.Portal;
import app.view.simulation.Renderer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameEngine
{
    private Map map;
    private Renderer renderer;
    private GraphicsEngine graphicsEngine;

    public GameEngine(Map map, Renderer renderer)
    {
        this.map = map;
        this.renderer = renderer;
        this.graphicsEngine = new RayTracing();
        Timeline timeline = new Timeline(new KeyFrame( Duration.millis(100),  ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void tick()
    {
        for (Agent a :map.getAgents())
        {
            Vector startPoint = a.getPosition();
            Vector endPoint = startPoint.add(a.move().getDeltaPos());

            boolean teleported = false;


            List<Portal> portalsOnPath = getPortalsOnPath(startPoint, endPoint);
            if(portalsOnPath.size()==1)
            {
                teleported = true;
                Portal portal = portalsOnPath.get(0);
                Vector intersection = getIntersectionPoint(portal, startPoint, endPoint);
                if(legalMove(startPoint, intersection))
                {
                    a.updateLocation(portal.getTeleportTo());

                    Vector direction = a.getDirection();
                    Vector newDirection = direction.rotate(portal.getTeleportRotation());
                    a.updateDirection(newDirection);

                    // TODO: add left-over movement to agent after teleportation
                    double leftOverDist = intersection.dist(endPoint);
                    Vector directionDist = newDirection.normalise().scale(leftOverDist);
                    a.updateLocation(a.getPosition().add(directionDist));

                }
            }
            if(portalsOnPath.size()>1)
            {
                teleported = true;
                double minDistance = startPoint.dist(endPoint);
                Vector closestIntersection = endPoint;
                Portal closestPortal = portalsOnPath.get(0);
                for(Portal portal: portalsOnPath)
                {
                    Vector newIntersection = getIntersectionPoint(portal, startPoint, endPoint);
                    double newDistance = startPoint.dist(newIntersection);
                    if(newDistance<minDistance)
                    {
                        minDistance = newDistance;
                        closestIntersection = newIntersection;
                        closestPortal = portal;
                    }
                }

                if(legalMove(startPoint, closestIntersection))
                {
                    a.updateLocation(closestPortal.getTeleportTo());

                    Vector direction = a.getDirection();
                    Vector newDirection = direction.rotate(closestPortal.getTeleportRotation());
                    a.updateDirection(newDirection);

                    double leftOverDist = closestIntersection.dist(endPoint);
                    Vector directionDist = newDirection.normalise().scale(leftOverDist);
                    a.updateLocation(a.getPosition().add(directionDist));
                }
            }

            if (!teleported && legalMove(startPoint, endPoint))
                a.updateLocation(endPoint);
        }

        map.getAgents().forEach(a -> a.updateView(graphicsEngine.compute(map, a)));

        renderer.render();
    }

    public void handleKey(KeyEvent e)
    {
        switch (e.getCharacter())
        {
            case "W" -> map.sprint(new Vector(0, -1));
            case "S" -> map.sprint(new Vector(0, 1));
            case "A" -> map.sprint(new Vector(-1, 0));
            case "D" -> map.sprint(new Vector(1, 0));
            case "w" -> map.walk(new Vector(0, -1));
            case "s" -> map.walk(new Vector(0, 1));
            case "a" -> map.walk(new Vector(-1, 0));
            case "d" -> map.walk(new Vector(1, 0));
        }
    }


    private boolean legalMove(Vector start, Vector end)
    {
        for (Boundary bdy : map.getBoundaries())
        {
            if(bdy.validMove(start, end))
                return false;
        }
        return true;
    }

    private List<Portal> getPortalsOnPath(Vector start, Vector end)
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

    private Vector getIntersectionPoint(Portal portal, Vector start, Vector end)
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
