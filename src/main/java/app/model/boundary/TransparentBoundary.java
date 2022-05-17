package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;

public class TransparentBoundary extends BoundaryImp
{
    public TransparentBoundary(Vector a, Vector b)
    {
        super(a, b, BoundaryType.TRANSPARENT_SOLID);
    }

    @Override
    public boolean isHit(Ray ray)
    {
        return false; // Overrides super -> light goes through it
    }

    @Override
    public Vector intersection(Ray ray)
    {
        return null; // Overrides super -> light goes through it
    }

    @Override
    public boolean isCrossed(Vector centre, double radius)
    {
        return false; // Overrides super -> light goes through it
    }

}
