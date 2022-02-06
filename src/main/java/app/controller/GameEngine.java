package app.controller;

import app.model.Agent;
import app.model.MapTemp;
import app.view.Renderer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameEngine
{
    private MapTemp map;
    private Renderer renderer;
    private GraphicsEngine graphicsEngine;
    private Timer clock;
    private int delay = 100;

    public GameEngine(MapTemp map, Renderer renderer)
    {
        this.map = map;
        this.renderer = renderer;
        this.graphicsEngine = new RayTracing();
        clock = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tick();
            }
        });
        clock.start();
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
