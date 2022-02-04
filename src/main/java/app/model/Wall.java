package app.model;

import app.controller.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Wall extends Border
{

    public Wall(Vector a, Vector b)
    {
        super(a, b);
    }

    @Override
    public void draw(GraphicsContext gc)
    {
        gc.setStroke(Color.BLACK);
        gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
    }
}
