package app.controller;

import app.controller.graphicsEngine.GraphicsEngine;
import app.controller.graphicsEngine.RayTracing;
import app.controller.linAlg.Vector;
import app.model.agents.Agent;
import app.model.Map;
import app.view.Renderer;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

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

    public void handleKey(KeyEvent e)
    {
        System.out.println(e.getCharacter());
        switch (e.getCharacter())
        {
            case "w" -> map.moveHuman(new Vector(0,-10));
            case "s" -> map.moveHuman(new Vector(0,10));
            case "a" -> map.moveHuman(new Vector(-10,0));
            case "d" -> map.moveHuman(new Vector(10,0));

        }

    }

    public void tick()
    {
        for(Agent a: map.getAgents())
        {
            a.move();
            a.updateView(graphicsEngine.compute(map, a));
        }
        renderer.render();
    }
}
