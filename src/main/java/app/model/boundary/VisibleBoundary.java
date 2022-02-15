package app.model.boundary;

import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VisibleBoundary extends InvisibleBoundary
{

    public VisibleBoundary(Vector a, Vector b)
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
