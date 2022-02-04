package app.controller;

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
        return new Ray(this.direction.rotate(degrees), this.origin);
    }
}
