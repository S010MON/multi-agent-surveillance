package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.controller.soundEngine.SoundEngine;
import app.model.Move;
import app.model.Trail;
import app.model.agents.ACO.AcoAgent;
import app.model.agents.Agent;
import app.model.agents.DirectionFollowAgent.DirectionFollowAgent;
import app.model.boundary.Boundary;
import app.model.Map;
import app.model.boundary.PortalBoundary;
import app.view.simulation.Renderer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;
import lombok.Getter;

public class GameEngine
{
    @Getter private long tics;
    private Map map;
    private Renderer renderer;
    private GraphicsEngine graphicsEngine;
    private Timeline timeline;

    public GameEngine(Map map, Renderer renderer)
    {
        this.tics = 0;
        this.map = map;
        this.renderer = renderer;
        this.graphicsEngine = new GraphicsEngine();
        this.timeline = new Timeline(new KeyFrame( Duration.millis(100),  ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void tick()
    {
        map.getSoundSources().forEach(s -> s.setRays(SoundEngine.buildTree(map, s)));
        map.getSoundSources().forEach(s -> s.decay());
        for(Agent agent: map.getAgents())
        {
            agent.clearHeard();
            map.getSoundSources().forEach(s -> agent.addHeard(s.heard(agent)));
        }

        map.getAgents().forEach(a -> a.updateView(graphicsEngine.compute(map, a)));
        map.getAgents().forEach(a -> a.getView().forEach(ray -> a.updateSeen(ray.getV())));
        map.getAgents().forEach(a -> map.updateAllSeen(a));
        map.getAgents().forEach(a -> map.checkForCapture(a));
        map.updateStates();

        for (Agent a : map.getAgents())
        {
            Vector startPoint = a.getPosition();
            Move move = a.move();
            Vector endPoint = startPoint.add(move.getDeltaPos());

            Vector teleportTo = checkTeleport(startPoint, endPoint);
            if (teleportTo != null)
            {
                a.updateLocation(teleportTo);
                a.setDirection(move.getEndDir());
                a.setMoveFailed(false);
                renderer.addTrail(new Trail(teleportTo, tics));
            }
            else if (legalMove(startPoint, endPoint) &&
                    legalMove(a, endPoint) &&
                    legalMove(a, startPoint) && legalMove(a, startPoint, endPoint))
            {
                if(!a.getPosition().equals(endPoint))
                {
                    map.addSoundSource(endPoint, a.getType());
                }
                a.updateLocation(endPoint);
                a.setDirection(move.getEndDir());
                a.setMoveFailed(false);
                renderer.addTrail(new Trail(endPoint, tics));
            }
            else
            {
                a.setMoveFailed(true);
            }
        }
        tics++;
        renderer.render();
        map.garbageCollection();
    }

    public void handleKey(KeyEvent e)
    {
        if(e.getCharacter().equals(" "))
            pausePlay();

        if(map.getHuman() != null)
        {
            switch(e.getCharacter())
            {
                case "W" -> map.getHuman().sprint(new Vector(0, -1));
                case "S" -> map.getHuman().sprint(new Vector(0, 1));
                case "A" -> map.getHuman().sprint(new Vector(-1, 0));
                case "D" -> map.getHuman().sprint(new Vector(1, 0));
                case "w" -> map.getHuman().walk(new Vector(0, -1));
                case "s" -> map.getHuman().walk(new Vector(0, 1));
                case "a" -> map.getHuman().walk(new Vector(-1, 0));
                case "d" -> map.getHuman().walk(new Vector(1, 0));
                case "q" -> map.getHuman().rotateLeft();
                case "e" -> map.getHuman().rotateRight();
            }
        }
    }

    public void pausePlay()
    {
        if(timeline.getStatus()== Animation.Status.RUNNING)
            timeline.pause();
        else
            timeline.play();
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
}
