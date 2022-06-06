package app.model.boundary;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import app.view.simulation.Info;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class VisibleBoundary extends BoundaryImp
{

    public VisibleBoundary(Vector a, Vector b, FurnitureType f)
    {
        super(a, b, f);
        colour = Color.BLACK;
    }
}
