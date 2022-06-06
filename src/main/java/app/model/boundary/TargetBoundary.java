package app.model.boundary;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.scene.paint.Color;

public class TargetBoundary extends BoundaryImp
{
    public TargetBoundary(Vector a, Vector b)
    {
        super(a, b, FurnitureType.TARGET);
        colour = Color.GOLD;
    }
}
