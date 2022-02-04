package app.view;

import app.controller.Beam;
import app.controller.Vector;
import app.model.MapTemp;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class Renderer extends Canvas
{
    private MapTemp map;
    public Renderer(MapTemp map, int width, int height)
    {
        super(width, height);
        this.map = map;
        render();
    }

    public void render()
    {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillRect(400,250,1,1);
        gc.setLineWidth(1);
        gc.setStroke(Color.BLACK);
        for(Beam r: map.beams)
        {
            gc.strokeLine(r.getU().getX(), r.getU().getY(), r.getV().getX(), r.getV().getY());
        }
    }


}
