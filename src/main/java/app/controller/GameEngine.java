package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.settings.SettingsObject;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.Map;
import app.model.furniture.FurnitureType;
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

            List<SettingsObject> portalsOnPath = portalsOnPath(startPoint, endPoint);
            if(portalsOnPath.size()==1)
            {
                teleported = true;
                SettingsObject portal = portalsOnPath.get(0);
                a.updateLocation(portal.getTeleportTo());

                Vector direction = a.getDirection().copy();
                Vector newDirection = direction.rotate(portal.getTeleportRotation());
                a.updateDirection(newDirection);
            }
            // TODO: determine closest portal
            // TODO: add check if no objects between agent and portal
            // TODO: add left-over movement to agent after teleportation

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

    private List<SettingsObject> portalsOnPath(Vector start, Vector end)
    {
        List<SettingsObject> portalsOnPath = new ArrayList<>();
        List<SettingsObject> portals = map.getSettings().getFurniture(FurnitureType.PORTAL);
        for(SettingsObject portal: portals)
        {
            if(!Intersection.findIntersection(start, end, new Vector(portal.getMinX(), portal.getMinY()), new Vector(portal.getMinX(), portal.getMaxY()))
                    && Intersection.findIntersection(start, end, new Vector(portal.getMinX(), portal.getMinY()), new Vector(portal.getMaxX(), portal.getMinY()))
                    && Intersection.findIntersection(start, end, new Vector(portal.getMaxX(), portal.getMaxY()),new Vector(portal.getMinX(), portal.getMaxY()))
                    && Intersection.findIntersection(start, end, new Vector(portal.getMaxX(), portal.getMaxY()), new Vector(portal.getMaxX(), portal.getMinY())))
            {
                portalsOnPath.add(portal);
            }
        }
        return portals;
    }
}
