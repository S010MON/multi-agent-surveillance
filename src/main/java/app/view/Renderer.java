package app.view;

import app.controller.graphicsEngine.Ray;
import app.model.agents.Agent;
import app.model.Map;
import app.model.Placeable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Renderer extends Canvas
{
    private Map map;
    public Renderer(Map map, int width, int height)
    {
        super(width, height);
        this.map = map;
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();

        gc.setFill(Color.WHITE);
        gc.fillRect(0,0,getWidth(), getHeight());

        for(Placeable p: map.getObjects())
        {
            p.draw(gc);
        }

        for (Agent a: map.getAgents())
        {
            for(Ray r: a.getView())
            {
                r.draw(gc);
            }
            a.draw(gc);
        }
    }


}
