package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;

public class TransparentBoundary extends BoundaryImp
{
    public TransparentBoundary(Vector a, Vector b, FurnitureType f)
    {
        super(a, b, f);
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
}
