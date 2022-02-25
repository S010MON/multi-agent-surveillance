package app.model.boundary;

import app.controller.linAlg.Vector;
import app.view.simulation.Info;
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
        gc.strokeLine(a.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      a.getY() * Info.getInfo().zoom + Info.getInfo().offsetY,
                      b.getX() * Info.getInfo().zoom + Info.getInfo().offsetX,
                      b.getY() * Info.getInfo().zoom + Info.getInfo().offsetY);
    }
}
