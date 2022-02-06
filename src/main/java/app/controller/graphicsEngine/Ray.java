package app.controller.graphicsEngine;

import app.controller.Vector;

public class Ray
{
    public Vector origin;
    public Vector endPoint;

    public Ray(Vector origin, Vector endPoint)
    {
        this.origin = origin;
        this.endPoint = endPoint;
    }

    public Vector getEndPoint()
    {
        return endPoint;
    }

    public Vector getOrigin()
    {
        return origin;
    }

    public Ray rotate(double degrees)
    {
        Vector unitVector = endPoint.sub(origin);
        Vector rotatedVector = unitVector.rotate(degrees);
        Vector newVector = origin.add(rotatedVector);
        return new Ray(this.origin, newVector);
    }
}
