package app.view.simulation;

import app.controller.graphicsEngine.Ray;
import app.model.Map;
import app.view.ScreenSize;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    private Map map;
    private Color backgroundColour;

    public Renderer(Map map)
    {
        super(ScreenSize.width, ScreenSize.height);
        this.map = map;
        backgroundColour = Color.WHITE;
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        drawBackground(gc);

        map.drawIntruderSpawn(gc);
        map.drawGuardSpawn(gc);
        map.getFurniture().forEach(e -> e.draw(gc));
        map.getAgents().forEach(e -> drawRays(gc, e.getView()));
        map.getAgents().forEach(e -> e.draw(gc));
    }

    private void drawRays(GraphicsContext gc, ArrayList<Ray> rays)
    {
        rays.forEach(e -> e.draw(gc));
    }

    private  void drawBackground(GraphicsContext gc)
    {
        gc.setFill(backgroundColour);
        gc.fillRect(0,0,getWidth(), getHeight());
    }
}
