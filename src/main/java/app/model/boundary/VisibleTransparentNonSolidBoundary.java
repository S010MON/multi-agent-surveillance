package app.model.boundary;

import app.controller.linAlg.Vector;
import app.model.furniture.FurnitureType;

public class VisibleTransparentNonSolidBoundary extends BoundaryImp
{
    public VisibleTransparentNonSolidBoundary(Vector a, Vector b, FurnitureType f)
    {
        super(a, b, f);
    }
}