package app.view;

import app.controller.graphicsEngine.Ray;
import app.model.agents.Agent;
import app.model.Map;
import app.model.boundary.Placeable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

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

        map.getTextures()
                .stream()
                .forEach(e -> e.draw(gc));

        map.getObjects()
                .stream()
                .forEach(e -> e.draw(gc));

        map.getAgents()
                .stream()
                .forEach(e -> drawRays(gc, e.getView()));

        map.getAgents()
                .stream()
                .forEach(e -> e.draw(gc));
    }

    private void drawRays(GraphicsContext gc, ArrayList<Ray> rays)
    {
        rays.stream()
                .forEach(e -> e.draw(gc));
    }


}
