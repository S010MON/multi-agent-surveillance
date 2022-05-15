package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Vector;
import lombok.Getter;

public class PortalBoundary extends BoundaryImp
{
    @Getter private Vector teleport;

    public PortalBoundary(Vector a, Vector b, Vector teleport)
    {
        super(a, b, true, false);
        this.teleport = teleport;
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
