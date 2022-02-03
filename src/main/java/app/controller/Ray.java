package app.controller;

public class Ray
{
    private Vector u;
    private Vector v;

    public Ray(Vector u, Vector v)
    {
        this.u = u;
        this.v = v;
    }

    public Vector getU()
    {
        return u;
    }

    public Vector getV()
    {
        return v;
    }
}
