package app.model.boundary;

import app.controller.linAlg.Vector;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VisibleBoundary extends BoundaryImp
{
    public VisibleBoundary(Vector a, Vector b)
    {
        super(a, b);
        colour = Color.BLACK;
    }
}
