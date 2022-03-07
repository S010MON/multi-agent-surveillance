package app.model.boundary;

import app.controller.linAlg.Vector;
import lombok.Getter;

public class PortalBoundary extends BoundaryImp
{
    @Getter private Vector teleport;

    public PortalBoundary(Vector a, Vector b, Vector teleport)
    {
        super(a, b);
        this.teleport = teleport;
    }
}
