package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.Map;
import app.view.Renderer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.util.ArrayList;
import java.lang.Math;

public class GameEngine {
    private Map map;
    private Renderer renderer;
    private GraphicsEngine graphicsEngine;

    public GameEngine(Map map, Renderer renderer) {
        this.map = map;
        this.renderer = renderer;
        this.graphicsEngine = new RayTracing();
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void tick() {
        for (Agent a : map.getAgents()) {
            Vector startPoint = a.getPosition();
            Vector endPoint = startPoint.add(a.move().getDeltaPos());

            if (legalMove(startPoint, endPoint))
                a.updateLocation(endPoint);
        }

        map.getAgents().forEach(a -> a.updateView(graphicsEngine.compute(map, a)));

        renderer.render();
    }

    public void handleKey(KeyEvent e) {
        switch (e.getCharacter()) {
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


    private boolean legalMove(Vector start, Vector end) {
        for (Boundary bdy : map.getBoundaries()) {
            if (bdy.validMove(start, end))
                return false;
        }
        return true;
    }


    private void agentCollisionDetection(Agent currentAgent, ArrayList<Agent> agentList) {
        Vector startPoint = currentAgent.getPosition();
        Vector endPoint = startPoint.add(currentAgent.move().getDeltaPos());
        double radius = currentAgent.getRadius();
        
        for (Agent agent: agentList){
            double tempRadius = agent.getRadius();
//            if(distanceCoordinate()){}

        }
    }


    private double distanceCoordinate(Vector a,Vector b) {
        double x_1 = a.getX();
        double y_1 = a.getY();
        double x_2 = b.getX();
        double y_2 = b.getY();
        return Math.sqrt(Math.pow((y_2 - y_1),2) + Math.pow((x_2 -x_1),2));
    }


}
