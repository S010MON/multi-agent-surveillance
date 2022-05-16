package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import javafx.scene.paint.Color;

public class TargetBoundary extends TransparentBoundary
{
    public TargetBoundary(Vector a, Vector b)
    {
        super(a, b);
        colour = Color.GOLD;
    }
}