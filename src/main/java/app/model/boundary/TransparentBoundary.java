package app.model.boundary;

import app.controller.graphicsEngine.Ray;
import app.controller.linAlg.Intersection;
import app.controller.linAlg.Vector;
import javafx.scene.canvas.GraphicsContext;

public class TransparentBoundary extends InvisibleBoundary
{
    public TransparentBoundary(Vector a, Vector b)
    {
        super(a, b);
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
