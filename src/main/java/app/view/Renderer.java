package app.view;

import app.controller.graphicsEngine.Ray;
import app.model.Map;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    private Map map;
    private Color backgroundColour;

    public Renderer(Map map, int width, int height)
    {
        super(width, height);
        this.map = map;
        backgroundColour = Color.WHITE;
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);

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

    private  void drawBackground(GraphicsContext gc)
    {
        gc.setFill(backgroundColour);
        gc.fillRect(0,0,getWidth(), getHeight());
    }


}
