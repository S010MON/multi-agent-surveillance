package app.model.boundary;

import app.controller.linAlg.Vector;

public class VisibleTransparentNonSolidBoundary extends BoundaryImp
{
    public VisibleTransparentNonSolidBoundary(Vector a, Vector b)
    {
        super(a, b, BoundaryType.VISIBLE_TRANSPARENT);
    }
}