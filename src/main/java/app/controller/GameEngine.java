package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.boundary.Boundary;
import app.model.furniture.Furniture;
import app.model.map.Map;
import app.view.Renderer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

import java.time.Instant;
import java.time.LocalTime;
import java.util.Arrays;

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
        Timeline timeline = new Timeline(new KeyFrame( Duration.millis(10),  ae -> tick()));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void tick()
    {

        map.getAgents().forEach(a -> a.move());

        map.getAgents().stream()
                        .forEach(a -> a.updateView(graphicsEngine.compute(map, a)));
        test(map);
        renderer.render();
    }

    public void handleKey(KeyEvent e)
    {
        switch (e.getCharacter())
        {
            case "W" -> map.run(new Vector(0, -1));
            case "S" -> map.run(new Vector(0, 1));
            case "A" -> map.run(new Vector(-1, 0));
            case "D" -> map.run(new Vector(1, 0));
            case "w" -> map.walk(new Vector(0, -1));
            case "s" -> map.walk(new Vector(0, 1));
            case "a" -> map.walk(new Vector(-1, 0));
            case "d" -> map.walk(new Vector(1, 0));
        }
    }


    public void test(Map map){
//        map.getFurniture().forEach(obstacle -> Arrays.toString(obstacle.getBoundaries().toArray()));

        map.getFurniture().forEach(obstacle -> obstacle.getBoundaries());
        for (Furniture obs : map.getFurniture()) {
            System.out.println(obs.getBoundaries());
        }

        System.out.println(map.getFurniture());
    }
}
