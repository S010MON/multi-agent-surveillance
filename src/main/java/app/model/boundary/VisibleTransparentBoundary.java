package app.model.boundary;

import app.controller.linAlg.Vector;

public class VisibleTransparentBoundary extends BoundaryImp
{
    public VisibleTransparentBoundary(Vector a, Vector b)
    {
        super(a, b, true, false);
    }
}
