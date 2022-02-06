package app.controller.graphicsEngine;

import app.controller.Vector;

public class Ray
{
    public Vector origin;
    public Vector direction;

    public Ray(Vector origin, Vector direction)
    {
        this.origin = origin;
        this.direction = direction;
    }

    public Vector getDirection()
    {
        return direction;
    }

    public Vector getOrigin()
    {
        return origin;
    }

    public Ray rotate(double degrees)
    {
        Vector unitVector = direction.sub(origin);
        Vector rotatedVector = unitVector.rotate(degrees);
        Vector newVector = origin.add(rotatedVector);
        return new Ray(this.origin, newVector);
    }
}
