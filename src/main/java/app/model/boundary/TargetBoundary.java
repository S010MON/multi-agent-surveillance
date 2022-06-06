package app.model.boundary;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;
import javafx.scene.paint.Color;

public class TargetBoundary extends TransparentBoundary
{
    public TargetBoundary(Vector a, Vector b)
    {
        super(a, b, FurnitureType.TARGET);
        colour = Color.GOLD;
    }

    @Override
    public boolean isCrossed(Vector centre, double radius)
    {
        return false;
    }

    @Override public boolean isCrossed(Vector startPoint, Vector endPoint)
    {
        return false;
    }
}
