package app.controller;

import app.model.Agent;
import app.model.MapTemp;
import app.view.Renderer;

public class GameEngine
{
    private MapTemp map;
    private Renderer renderer;
    private GraphicsEngine graphicsEngine;

    public GameEngine(MapTemp map, Renderer renderer)
    {
        this.map = map;
        this.renderer = renderer;
        this.graphicsEngine = new RayTracing();
        tick();
    }

    public void tick()
    {
        for(Agent a: map.getAgents())
        {
            a.updateView(graphicsEngine.compute(map, a));
        }
        renderer.render();
    }
}
